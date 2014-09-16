#include "bayesian.h"
#include <stdio.h>


BayesianMatting::BayesianMatting(cv::Mat img, cv::Mat trimap)
{
    this->img = img.clone();
    
    // Convert the trimap into a single channel image
    if(trimap.channels()==3)
    {
        this->trimap = Mat(img.size(), CV_8UC1);
        cv::cvtColor(trimap, this->trimap, CV_BGR2GRAY, 1);
    }
    else
    {
        this->trimap = trimap.clone();
    }
    
    initialize();
    setParameters();
}

BayesianMatting::~BayesianMatting()
{

}

void BayesianMatting::initialize()
{
    cv::Size imgSize = trimap.size();
    
    fgImg = Mat(imgSize, CV_8UC3, cv::Scalar(0, 0, 0));
	unKnownImg = Mat(imgSize, CV_8UC3, cv::Scalar(0, 0, 0));
    bgImg = Mat(imgSize, CV_8UC3, cv::Scalar(0, 0, 0));
    maskFg = Mat(imgSize, CV_8UC1, cv::Scalar(0));
    maskBg = Mat(imgSize, CV_8UC1, cv::Scalar(0));
    maskUnknown = Mat(imgSize, CV_8UC1, cv::Scalar(0));
    maskUnsolved = Mat(imgSize, CV_8UC1, cv::Scalar(0));
    alphamap = Mat(imgSize, CV_32FC1, cv::Scalar(0));
    
    for(int y=0;y<imgSize.height;y++)
    {
        for(int x=0;x<imgSize.width;x++)
        {
            uchar v = trimap.at<uchar>(y, x);
            if(v==0)
                maskBg.at<uchar>(y, x) = 255;
            else if(v == 255)
                maskFg.at<uchar>(y, x) = 255;
            else
                maskUnknown.at<uchar>(y, x) = 255;
        }
    }
    
    img.copyTo(fgImg, maskFg);
    img.copyTo(bgImg, maskBg);
	img.copyTo(unKnownImg,maskUnknown);
    maskUnknown.copyTo(maskUnsolved);
    
    //cv::imshow("maskFg", maskFg);
    //cv::imshow("maskBg", maskBg);
    //cv::imshow("maskUnknown", maskUnknown);
    //cv::waitKey(0);
    
    return;
}

vector<cv::Point> BayesianMatting::getContour(Mat img)
{
    vector<cv::Point> ret;
    vector<vector<cv::Point> > contours;
    
    cv::findContours(img, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);
    int numContours = contours.size();
    for(int i=0;i<numContours;i++)
    {
        int count = contours[i].size();
        for(int j=0;j<count;j++)
        {
            ret.push_back(contours[i][j]);
        }
    }
    
    return ret;
}

void BayesianMatting::setParameters(int N, double sigma, double sigmaC)
{
    this->nearest = N;
    this->sigma = sigma;
    this->sigmaC = sigmaC;
}

double BayesianMatting::solve()
{
    printf("Starting to solve\n");
    int p=0,x=0,y=0, i=0, j=0, iter=0, fgClus=0, bgClus=0;
    int outer;
    float L=0, maxL=0;
    
    Mat shownImg = Mat(img.size(), 8, 3);
    Mat solveAgainMask = Mat(maskUnsolved.size(), 8, 1);
    
    vector<float> fg_weight(BAYESIAN_MAX_CLUS, 0);
    vector<float> bg_weight(BAYESIAN_MAX_CLUS, 0);
    vector<Mat>   fg_mean(BAYESIAN_MAX_CLUS);
    vector<Mat>   bg_mean(BAYESIAN_MAX_CLUS);
    vector<Mat>   inv_fg_cov(BAYESIAN_MAX_CLUS);
    vector<Mat>   inv_bg_cov(BAYESIAN_MAX_CLUS);
    
    for(i=0;i<BAYESIAN_MAX_CLUS;i++)
    {
        fg_mean[i] = Mat(3, 1, CV_32FC1);
        bg_mean[i] = Mat(3, 1, CV_32FC1);
        inv_fg_cov[i] = Mat(3, 3, CV_32FC1);
        inv_bg_cov[i] = Mat(3, 3, CV_32FC1);
    }
    
    printf("Starting iterations\n");
    
    for(int iteration=0;iteration<1;iteration++)
    {
        if(iteration)
            maskUnsolved.copyTo(solveAgainMask);
            
        outer = 0;
        for(;;)
        {
            vector<cv::Point> toSolveList;
            
            if(!iteration)
                toSolveList = getContour(maskUnsolved);
            else
                toSolveList = getContour(solveAgainMask);
                
            if(toSolveList.size()==0)
                break;
            //else
                //printf("There are %d pixels to solve\n", toSolveList.size());
                
//            img.copyTo(shownImg);
//            for(int k=0;k<toSolveList.size();k++)
//                cv::circle(shownImg, toSolveList[k], 0, CV_RGB(128,128,128));
//                
            //cv::imshow("points to solve", shownImg);
            //cv::waitKey(0);
            
            // Solve the points
            printf("Solve %d pixels\n", toSolveList.size());
            for(p=0;p<toSolveList.size();p++)
            {
				if (p%1000 ==0) 
				{
					printf("Pixel %d/%d - (%d, %d)\n", p, toSolveList.size(), toSolveList[p].x, toSolveList[p].y);
				}
                
                x = toSolveList[p].x;
                y = toSolveList[p].y;

              //  printf("Trying to get GMM for pixel (%d,%d) - ", x, y);
                GetGMMModel(x, y, fg_weight, fg_mean, inv_fg_cov, bg_weight, bg_mean, inv_bg_cov);
                //printf("fg_mean[0] = {%f, %f, %f}\n", fg_mean[0].at<float>(0, 0), fg_mean[0].at<float>(1, 0), fg_mean[0].at<float>(2, 0));
                //printf("fg_mean[1] = {%f, %f, %f}\n", fg_mean[1].at<float>(0, 0), fg_mean[1].at<float>(1, 0), fg_mean[1].at<float>(2, 0));
                //printf("fg_mean[2] = {%f, %f, %f}\n", fg_mean[2].at<float>(0, 0), fg_mean[2].at<float>(1, 0), fg_mean[2].at<float>(2, 0));
                //printf("Got GMM\n");
                maxL = -1000.0f;
                //printf("maxL = %f\n", maxL);
                
                for(i=0;i<BAYESIAN_MAX_CLUS;i++)
                {
                    for(j=0;j<BAYESIAN_MAX_CLUS;j++)
                    {
                        if(!iteration)
                            InitializeAlpha(x, y, maskUnsolved);
                        else
                            InitializeAlpha(x, y, solveAgainMask);
                            
                        
                    
                        for(iter=0;iter<3;iter++)
                        {
                          //  printf("Trying BF\n");
                            SolveBF(x, y, fg_mean[i], inv_fg_cov[i], bg_mean[j], inv_bg_cov[j]);
                           // printf("Trying Alpha\n");
                            SolveAlpha(x, y);
                        }
                        
                        //printf("Got out\n");
                        L = computeLikelihood(x, y, fg_mean[i], inv_fg_cov[i], bg_mean[j], inv_bg_cov[j]);
                        //printf("Likelihood = %f\n", L);
                        //printf("Got out and done\n");
                        if(L>maxL)
                        {
                            maxL = L;
                            fgClus = i;
                            bgClus = j;
                          //  printf("the condition got true\n");
                        }
                        //printf("The loop ends here\n");
                    }
                }
                
                //printf("Got out of the final iteration\n");
                
                if(!iteration)
                    InitializeAlpha(x, y, maskUnsolved);
                else
                    InitializeAlpha(x, y, solveAgainMask);
                    
                //printf("Got done with the initialization\n");
                //printf("fgClus=%d, bgClus=%d", fgClus, bgClus);
                    
                for(iter=0;iter<5;iter++)
                {
                  //  printf("Iteration #%d with fgClus=%d, bgClus=%d", iter, fgClus, bgClus);
                    fflush(stdout);
                    SolveBF(x, y, fg_mean[fgClus], inv_fg_cov[fgClus], bg_mean[bgClus], inv_bg_cov[bgClus]);
                    SolveAlpha(x, y);
                }
                
                //printf("Done here too\n");
                
                if(!iteration)
                    maskUnsolved.at<uchar>(y, x) = 0;
                else
                    solveAgainMask.at<uchar>(y, x) = 0;
            }
            
            
            
        }
    }
    
    //cv::bitwise_or(alphamap, maskFg, alphamap);
    //cv::imshow("img", img);
    
    Mat scaledAlpha = Mat(alphamap.size(), CV_8UC1);
    alphamap.convertTo(scaledAlpha, 8, 255.0);
    cv::bitwise_or(maskFg, scaledAlpha, scaledAlpha);
    //cv::imshow("alpha", scaledAlpha);

    
   // cv::waitKey(0);
}

void BayesianMatting::Composite(Mat src, Mat alpha,Mat &dst)
{
	cv::Size imgSize = src.size();
	for(int y=0;y<imgSize.height;y++)
    {
        for(int x=0;x<imgSize.width;x++)
        {
            uchar v = alpha.at<uchar>(y, x);

			cv::Vec3b srcpoint =  src.at<cv::Vec3b>(y, x);
			cv::Vec4b point;
			point[0] = v;//srcpoint[0];
			point[1] = v;//srcpoint[1];
			point[2] = v;//srcpoint[2];
			point[3] = v;
			dst.at<cv::Vec4b>(y,x) = point;
		}
	}
}

void BayesianMatting::InitializeAlpha(int x, int y, Mat unsolvedMask)
{
    int min_x = max(0, x-WIN_SIZE);
    int min_y = max(0, y-WIN_SIZE);
    int max_x = min(img.cols-1, x+WIN_SIZE);
    int max_y = min(img.rows-1, y+WIN_SIZE);
    
    int count = 0;
    float sum = 0;
    for(int j=min_y;j<=max_y;j++)
    {
        for(int i=min_x;i<=max_x;i++)
        {
            if(unsolvedMask.at<uchar>(j, i)==0)
            {
                sum+=alphamap.at<float>(j, i);
                count++;
            }
        }
    }
    alphamap.at<float>(y, x) = count ? sum/count:0;
}

void BayesianMatting::SolveAlpha(int x, int y)
{
    cv::Vec3b color = img.at<cv::Vec3b>(y, x);
    cv::Vec3b bg = bgImg.at<cv::Vec3b>(y, x);
    cv::Vec3b fg = fgImg.at<cv::Vec3b>(y, x);

    alphamap.at<float>(y, x) = std::max(0.0, std::min(1.0, (double)((color[0] - bg[0]) * (fg[0] - bg[0]) + (color[1] - bg[1]) * (fg[1] - bg[1]) + (color[2] - bg[2]) * (fg[2] - bg[2])) / ((fg[0] - bg[0]) * (fg[0] - bg[0]) + (fg[1] - bg[1]) * (fg[1] - bg[1]) + (fg[2] - bg[2]) * (fg[2] - bg[2]))));
}

void BayesianMatting::SolveBF(int x, int y, Mat fg_mean, Mat inv_fg_cov, Mat bg_mean, Mat inv_bg_cov)
{
    Mat A = Mat(6, 6, CV_32FC1, cv::Scalar(0));
    Mat X = Mat(6, 1, CV_32FC1, cv::Scalar(0));
    Mat b = Mat(6, 1, CV_32FC1, cv::Scalar(0));
    Mat I = Mat(3, 3, CV_32FC1, cv::Scalar(0));
    
    Mat work_3x3 = Mat(3, 3, CV_32FC1, cv::Scalar(0));
    Mat work_3x1 = Mat(3, 1, CV_32FC1, cv::Scalar(0));
    
    float alpha = alphamap.at<float>(y, x);
    cv::Vec3b  c_color = img.at<cv::Vec3b>(y, x);
    cv::Vec3b fg_color = fgImg.at<cv::Vec3b>(y, x);
    cv::Vec3b bg_color = bgImg.at<cv::Vec3b>(y, x);
    
    float inv_sigmac_square = 1.0/(sigmaC*sigmaC);
    
    I.at<float>(0,0) = 1;
    I.at<float>(1,1) = 1;
    I.at<float>(2,2) = 1;
    
//    I.copyTo(work_3x3);
    work_3x3 = I*(alpha*alpha*inv_sigmac_square);
    work_3x3 = work_3x3 + inv_fg_cov;
    
    for(int i=0;i<3;i++)
        for(int j=0;j<3;j++)
            A.at<float>(i, j) = work_3x3.at<float>(i, j);
            
//    I.copyTo(work_3x3);
    work_3x3 = I*(alpha*(1-alpha)*inv_sigmac_square);
    for(int i=0;i<3;i++)
        for(int j=0;j<3;j++)
            A.at<float>(i, 3+j) = work_3x3.at<float>(i, j);
            
    
            
    work_3x3 = I*((1-alpha)*(1-alpha)*inv_sigmac_square);
    work_3x3 = work_3x3 + inv_bg_cov;
    for(int i=0;i<3;i++)
        for(int j=0;j<3;j++)
            A.at<float>(3+i, 3+j) = work_3x3.at<float>(i, j);
    
    work_3x1 = inv_fg_cov*fg_mean;
    for(int i=0;i<3;i++)
        b.at<float>(i, 0) = work_3x1.at<float>(i, 0) + c_color[i]*alpha*inv_sigmac_square;
        
        
        
    work_3x1 = inv_bg_cov*bg_mean;
    for(int i=0;i<3;i++)
        b.at<float>(3+i, 0) = work_3x1.at<float>(i, 0) + c_color[i]*(1-alpha)*inv_sigmac_square;
    
     
    cv::solve(A, b, X);
    
    
    
    fg_color[0] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(0, 0)));
    fg_color[1] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(1, 0)));
    fg_color[2] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(2, 0)));
    bg_color[0] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(3, 0)));
    bg_color[1] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(4, 0)));
    bg_color[2] = (uchar)std::max(0.0, std::min(255.0, (double)X.at<float>(5, 0)));
    
    fgImg.at<cv::Vec3b>(y, x) = fg_color;
    bgImg.at<cv::Vec3b>(y, x) = bg_color;
	//printf("set the fg pixel (%d,%d) - \n", x, y);
            
}

void BayesianMatting::CollectSampleSet(int x, int y, vector<pair<cv::Point, float> > &fg_set, vector<pair<cv::Point, float> > &bg_set)
{
    // Erase any existing set
    fg_set.clear();
    bg_set.clear();
    
    #define UNSURE_DIST 1
    pair<cv::Point, float> sample;
    float dist_weight;
    float inv_2sigma_square = 1.0/(2.0*this->sigma*this->sigma);
    int dist=1;
    
   // printf("Trying to figure around point (%d,%d)\n", x, y);
    
    while(fg_set.size()<nearest)
    {
        if(y-dist>=0)
        {
            for(int z=max(0, x-dist);z<=min(this->img.cols-1, x+dist);z++)
            {
                dist_weight = expf(-(dist*dist+(z-x)*(z-x)) * inv_2sigma_square);
                
                // We know this pixel belongs to the foreground
                if(maskFg.at<uchar>(y-dist, z)!=0)
                {
                    sample.first.x = z;
                    sample.first.y = y-dist;
                    sample.second = dist_weight;
                    //printf("dist_weight = %f\n", dist_weight);
                    
                    fg_set.push_back(sample);
                   // printf("Pushed A");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(y-dist, z)!=0 && maskUnsolved.at<uchar>(y-dist, z)==0)
                {
                    sample.first.x = z;
                    sample.first.y = y-dist;
                    
                    float alpha = alphamap.at<float>(y-dist, z);
                    sample.second = dist_weight*alpha*alpha;     
                    //printf("dist_weight = %f\n", dist_weight);           
                    
                    fg_set.push_back(sample);
                    //printf("Pushed B");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
            }
        }
        
        if(y+dist<=img.rows-1)
        {
            for(int z=max(0, x-dist);z<=min(img.cols-1, x+dist);z++)
            {
                dist_weight = expf(-(dist*dist+(z-x)*(z-x)) * inv_2sigma_square);
                
                if(maskFg.at<uchar>(y+dist, z)!=0)
                {
                    sample.first.y = y+dist;
                    sample.first.x = z;
                    sample.second = dist_weight;
                   // printf("dist_weight = %f\n", dist_weight);
                    
                    fg_set.push_back(sample);
                    //printf("Pushed C");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(y+dist, x)!=0 && maskUnsolved.at<uchar>(y+dist, z)==0)
                {
                    sample.first.x = z;
                    sample.first.y = y+dist;
                    
                    float alpha = alphamap.at<float>(y+dist, z);
                    sample.second = dist_weight*alpha*alpha;
                    
                    fg_set.push_back(sample);
                    //printf("Pushed D");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
            }
        }
        
        if(x-dist>=0)
        {
            for(int z=max(0, y-dist+1);z<=min(img.rows-1, y+dist-1); z++)
            {
                dist_weight = expf(-(dist*dist+(z-y)*(z-y)) * inv_2sigma_square);
                //printf("dist = %d\n", dist*dist+(z-y)*(z-y));

                
                if(maskFg.at<uchar>(z, x-dist)!=0)
                {
                    sample.first.x = x-dist;
                    sample.first.y = z;
                    sample.second = dist_weight;
                    
                    
                    
                    fg_set.push_back(sample);
                    //printf("Pushed E\n");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(z, x-dist)!=0 && maskUnsolved.at<uchar>(z, x-dist)==0)
                {
                    sample.first.x = x-dist;
                    sample.first.y = z;
                    
                    float alpha = alphamap.at<float>(z, x-dist);
                    sample.second = dist_weight*alpha*alpha;
                    
                    fg_set.push_back(sample);
                    //printf("Pushed F");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
            }
        }
        
        if(x+dist<img.cols)
        {
            for(int z=max(0, y-dist+1);z<=min(img.rows-1, y+dist-1); z++)
            {
                dist_weight = expf(-(dist*dist+(y-z)*(y-z)) * inv_2sigma_square);
                
                if(maskFg.at<uchar>(z, x+dist)!=0)
                {
                    sample.first.x = x+dist;
                    sample.first.y = z;
                    sample.second = dist_weight;
                    
                    /*if(dist_weight>0.0)
                        printf("dist_weight = %f\n", dist_weight);*/
                    
                    fg_set.push_back(sample);
                    //printf("Pushed G");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(z, x+dist)!=0 && maskUnsolved.at<uchar>(z, x+dist)==0)
                {
                    sample.first.x = x+dist;
                    sample.first.y = z;
                    
                    float alpha = alphamap.at<float>(z, x+dist);
                    sample.second = dist_weight*alpha*alpha;
                    
                    fg_set.push_back(sample);
                    //printf("Pushed H");
                    if(fg_set.size()==nearest)
                        goto BG;
                }
            }
        }
        
        ++dist;
    }
    
BG:
    int bg_unsure=0;
    dist=1;

    while(bg_set.size()<nearest)
    {
        if(y-dist>=0)
        {
            for(int z=max(0, x-dist);z<=min(x+dist, img.cols-1);z++)
            {
                dist_weight = expf(-(dist*dist+(z-x)*(z-x))*inv_2sigma_square);
                if(maskBg.at<uchar>(y-dist, z)!=0)
                {
                    sample.first.x = z;
                    sample.first.y = y-dist;
                    sample.second = dist_weight;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(y-dist, z)!=0 && maskUnsolved.at<uchar>(y-dist, z)==0)
                {
                    sample.first.x = z;
                    sample.first.y = y-dist;
                    
                    float alpha = alphamap.at<float>(y-dist, z);
                    sample.second = dist_weight*alpha*alpha;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
            }
        }
        
        if(y+dist<img.rows)
        {
            for(int z=max(0, x-dist);z<=min(x+dist, img.cols-1);z++)
            {
                dist_weight = expf(-(dist*dist+(z-x)*(z-x))*inv_2sigma_square);
                if(maskBg.at<uchar>(y+dist, z)!=0)
                {
                    sample.first.x = z;
                    sample.first.y = y+dist;
                    sample.second = dist_weight;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(y+dist, z)!=0 && maskUnsolved.at<uchar>(y-dist, z)==0)
                {
                    sample.first.x = z;
                    sample.first.y = y+dist;
                    
                    float alpha = alphamap.at<float>(y+dist, z);
                    sample.second = dist_weight*alpha*alpha;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
            }
        }
        
        if(x-dist>=0)
        {
            for(int z=max(0, y-dist+1);z<=min(y+dist-1, img.rows-1);z++)
            {
                dist_weight = expf(-(dist*dist+(y-z)*(y-z))*inv_2sigma_square);
                if(maskBg.at<uchar>(z, x-dist)!=0)
                {
                    sample.first.x = x-dist;
                    sample.first.y = z;
                    sample.second = dist_weight;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(z, x-dist)!=0 && maskUnsolved.at<uchar>(z, x-dist)==0)
                {
                    sample.first.x = x-dist;
                    sample.first.y = z;
                    
                    float alpha = alphamap.at<float>(z, x-dist);
                    sample.second = dist_weight*alpha*alpha;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
            }
        }
        
        if(x+dist<img.cols)
        {
            for(int z=max(0, y-dist+1);z<=min(y+dist-1, img.rows-1);z++)
            {
                dist_weight = expf(-(dist*dist+(y-z)*(y-z))*inv_2sigma_square);
                
                if(maskBg.at<uchar>(z, x+dist)!=0)
                {
                    sample.first.x = x+dist;
                    sample.first.y = z;
                    sample.second = dist_weight;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
                else if(dist<UNSURE_DIST && maskUnknown.at<uchar>(z, x+dist)!=0 && maskUnsolved.at<uchar>(z, x+dist)==0)
                {
                    sample.first.x = x+dist;
                    sample.first.y = z;
                    
                    float alpha = alphamap.at<float>(z, x+dist);
                    sample.second = dist_weight*alpha*alpha;
                    
                    bg_set.push_back(sample);
                    if(bg_set.size()==nearest)
                        goto DONE;
                }
            }
        }
        
        dist++;
    }
    
DONE:
    /*Mat temp = this->img.clone();
    cv::line(temp, cv::Point(x, y), cv::Point(x, y), CV_RGB(255,255,255), 1);
    for(int i=0;i<bg_set.size();i++)
    {
        cv::line(temp, bg_set[i].first, bg_set[i].first, CV_RGB(255*bg_set[i].second,255*bg_set[i].second,255*bg_set[i].second), 1);
        //printf("(%d, %d) - %f\n", bg_set[i].first.x, bg_set[i].first.y, bg_set[i].second);
    }
     
    cv::imshow("Temp", temp)   ;
    cv::waitKey(0);*/

    return;
}

float BayesianMatting::computeLikelihood(int x, int y, Mat fg_mean, Mat inv_fg_cov, Mat bg_mean, Mat inv_bg_cov)
{
	float fgL, bgL, cL;
	int i;
	
	float alpha = alphamap.at<float>(y, x);



	Mat work3x1 = Mat(3, 1, CV_32FC1);
	Mat work1x3 = Mat(1, 3, CV_32FC1);
	Mat work1x1 = Mat(1, 1, CV_32FC1);
	Mat fg_color = Mat(3, 1, CV_32FC1);
	Mat bg_color = Mat(3, 1, CV_32FC1);
	Mat c_color = Mat(3, 1, CV_32FC1);
	
	
	
	Vec3b t = fgImg.at<cv::Vec3b>(y, x);
	fg_color.at<float>(0, 0) = t[0];
	fg_color.at<float>(1, 0) = t[1];
	fg_color.at<float>(2, 0) = t[2];
	
	t = bgImg.at<cv::Vec3b>(y, x);
	bg_color.at<float>(0, 0) = t[0];
	bg_color.at<float>(1, 0) = t[1];
	bg_color.at<float>(2, 0) = t[2];
	
	t = img.at<cv::Vec3b>(y, x);
	c_color.at<float>(0, 0) = t[0];
	c_color.at<float>(1, 0) = t[1];
	c_color.at<float>(2, 0) = t[2];
	
	
	
	// fgL
	work3x1 = fg_color - fg_mean;
	work1x3 = work3x1.t();
	work1x3 = work1x3*inv_fg_cov;
	work1x1 = work1x3*work3x1;
	fgL = -1.0f*work1x1.at<float>(0, 0)/2;

	// bgL
	work3x1 = bg_color-bg_mean;
	work1x3 = work3x1.t();
	work1x3 = work1x3*inv_bg_cov;
	work1x1 = work1x3*work3x1;
	bgL = -1.f*work1x1.at<float>(0,0)/2;
    

	// cL
	Mat temp = c_color - (alpha*fg_color)- (1.0f-alpha)*bg_color;
	cL = (temp.dot(temp)) / 2*sigmaC*sigmaC;
	
	//printf("Got till the end with fgL=%f bgL=%f and cL=%f\n", fgL, bgL, cL);

	return cL+fgL+bgL;
}

void BayesianMatting::GetGMMModel(int x, int y, vector<float> &fg_weight, vector<Mat> &fg_mean, vector<Mat> inv_fg_cov, vector<float> &bg_weight, vector<Mat> bg_mean, vector<Mat> inv_bg_cov)
{
    vector<pair<cv::Point, float> > fg_set, bg_set;
    CollectSampleSet(x, y, fg_set, bg_set);
    
    /*for(int i=0;i<fg_set.size();i++)
    {
        float wt = fg_set[i].second;
        if(wt>0.0)
            printf("fg_set[%d] = (%d,%d) - %f\n", fg_set[i].first.x, fg_set[i].first.y, wt);
    }*/
    
    Mat mean = Mat(3, 1, CV_32FC1);
    Mat cov  = Mat(3, 3, CV_32FC1);
    Mat inv_cov = Mat(3, 3, CV_32FC1);
    Mat eigval = Mat(3, 1, CV_32FC1);
    Mat eigvec = Mat(3, 3, CV_32FC1);
    Mat cur_color = Mat(3, 1, CV_32FC1);
    Mat max_eigvec = Mat(3, 1, CV_32FC1);
    Mat target_color = Mat(3, 1, CV_32FC1);
    
    vector<pair<cv::Point, float> > clus_set[BAYESIAN_MAX_CLUS];
    int nClus = 1;
    clus_set[0] = fg_set;
    
    while(nClus < BAYESIAN_MAX_CLUS)
    {
        // Find the largest eigen value
        double max_eigval = 0;
        int max_idx = 0;
        for(int i=0;i<nClus;i++)
        {
            
            CalculateNonNormalizeCov(fgImg, clus_set[i], mean, cov);
            
            // cov = source
            // eigval = result
            // eigvec = left orthogonal matrix
            // cov = eigvec * eigval * V
            
            cv::SVD svd;
            svd(cov);
            svd.w.copyTo(eigval);
            svd.u.copyTo(eigvec);
            //cvSVD(cov, eigval, eigvec);
            
            float temp = eigval.at<float>(0, 0);
            if(temp > max_eigval)
            {
                max_eigvec = eigvec.col(0);
                max_idx = i;
            }
        }
        
        // Split
        vector<pair<cv::Point, float> > new_clus_set[2];
        CalculateMeanCov(fgImg, clus_set[max_idx], mean, cov);
        double boundary = mean.dot(max_eigvec);
        for(size_t i=0;i<clus_set[max_idx].size();i++)
        {
            for(int j=0;j<3;j++)
                cur_color.at<float>(j, 0) = fgImg.at<cv::Vec3b>(clus_set[max_idx][i].first.y, clus_set[max_idx][i].first.x)[j];
                
                if(cur_color.dot(max_eigvec)>boundary)
                    new_clus_set[0].push_back(clus_set[max_idx][i]);
                else
                    new_clus_set[1].push_back(clus_set[max_idx][i]);
        }
        
        clus_set[max_idx] = new_clus_set[0];
        clus_set[nClus] = new_clus_set[1];
        
        nClus+=1;
    }
        
    float weight_sum, inv_weight_sum;
    weight_sum = 0;
    for(int i=0;i<nClus;i++)
    {
        CalculateWeightMeanCov(fgImg, clus_set[i], fg_weight[i], fg_mean[i], cov);
        
        inv_fg_cov[i] = cov.inv();
        weight_sum += fg_weight[i];
    }
    
    //printf("fg_mean[0] = {%f, %f, %f}\n", fg_mean[0].at<float>(0, 0), fg_mean[0].at<float>(1, 0), fg_mean[0].at<float>(2, 0));
    //            printf("fg_mean[1] = {%f, %f, %f}\n", fg_mean[1].at<float>(0, 0), fg_mean[1].at<float>(1, 0), fg_mean[1].at<float>(2, 0));
    //            printf("fg_mean[2] = {%f, %f, %f}\n", fg_mean[2].at<float>(0, 0), fg_mean[2].at<float>(1, 0), fg_mean[2].at<float>(2, 0));
        
    // Normalize weight
    inv_weight_sum = 1.0f/weight_sum;
    for(int i=0;i<nClus;i++)
        fg_weight[i] *= inv_weight_sum;
            
    // Background
    nClus = 1;
    for(int i=0;i<BAYESIAN_MAX_CLUS;i++)
        clus_set[i].clear();
    
    clus_set[0] = bg_set;
    while(nClus<BAYESIAN_MAX_CLUS)
    {
        // Find the largest eigenvalue
        double max_eigval = 0;
        int max_idx = 0;
        
        for(int i=0;i<nClus;i++)
        {
            CalculateNonNormalizeCov(bgImg, clus_set[i], mean, cov);
            
            // Compute the eigval and eigvec
            cv::SVD svd;
            svd(cov);
            eigval = svd.w;
            eigvec = svd.u;
            float temp = eigval.at<float>(0, 0);
            if(temp > max_eigval)
            {
                max_eigvec = eigvec.col(0);
                max_eigval = temp;
                max_idx = i;
            }
        }
        
        // split
        vector<pair<cv::Point, float> > new_clus_set[2];
        CalculateMeanCov(bgImg, clus_set[max_idx], mean, cov);
        double boundary = mean.dot(max_eigvec);
        for(size_t i=0;i<clus_set[max_idx].size();i++)
        {
            for(int j=0;j<3;j++)
                cur_color.at<float>(j, 0) = bgImg.at<cv::Vec3b>(clus_set[max_idx][i].first.y, clus_set[max_idx][i].first.x)[j];
            
            if(cur_color.dot(max_eigvec)>boundary)
                new_clus_set[0].push_back(clus_set[max_idx][i]);
            else
                new_clus_set[1].push_back(clus_set[max_idx][i]);
        }
        
        clus_set[max_idx] = new_clus_set[0];
        clus_set[nClus] = new_clus_set[1];
        
        nClus += 1;
    }
    
    // Return all the mean and cov for the background
    weight_sum = 0;
    for(int i=0;i<nClus;i++)
    {
        CalculateWeightMeanCov(bgImg, clus_set[i], bg_weight[i], bg_mean[i], cov);
        inv_bg_cov[i] = cov.inv();
        weight_sum += bg_weight[i];
    }
    
    // Normalize weights
    inv_weight_sum = 1.0f/weight_sum;
    for(int i=0;i<nClus;i++)
        bg_weight[i] *= inv_weight_sum;
}

void BayesianMatting::CalculateNonNormalizeCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, Mat mean, Mat cov)
{
    int cur_x, cur_y;
    float cur_w, total_w=0;
    
    Mat(Mat::zeros(mean.rows, mean.cols, CV_32FC1)).copyTo(mean);
    Mat(Mat::zeros(cov.rows, cov.cols, CV_32FC1)).copyTo(cov);
    
    for(size_t j=0;j<clus_set.size();j++)
    {
        cur_x = clus_set[j].first.x;
        cur_y = clus_set[j].first.y;
        cur_w = clus_set[j].second;
        
        for(int h=0;h<3;h++)
        {
            cv::Vec3b color = cImg.at<cv::Vec3b>(cur_y, cur_x);
            mean.at<float>(h, 0) = mean.at<float>(h, 0) + cur_w*color[h];
            for(int k=0;k<3;k++)
            {
                color = cImg.at<cv::Vec3b>(cur_y, cur_x);
                cov.at<float>(h, k) = cov.at<float>(h, k) + cur_w*color[h]*color[k];
            }
        }
        
        total_w += cur_w;
    }
    
    float inv_total_w = 1.0f/total_w;
    for(int h=0;h<3;h++)
    {
        for(int k=0;k<3;k++)
            cov.at<float>(h, k) = cov.at<float>(h, k) - inv_total_w*mean.at<float>(h, 0)*mean.at<float>(k, 0);
    }
}

void BayesianMatting::CalculateMeanCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, Mat mean, Mat cov)
{
    int cur_x, cur_y;
    float cur_w, total_w=0;
    Mat(Mat::zeros(mean.rows, mean.cols, CV_32FC1)).copyTo(mean);
    Mat(Mat::zeros(cov.rows, cov.cols, CV_32FC1)).copyTo(cov);    
    for(size_t j=0;j<clus_set.size();j++)
    {
        cur_x = clus_set[j].first.x;
        cur_y = clus_set[j].first.y;
        cur_w = clus_set[j].second;
        
        for(int h=0;h<3;h++)
        {
            cv::Vec3b color = cImg.at<cv::Vec3b>(cur_y, cur_x);
            mean.at<float>(h, 0) = mean.at<float>(h, 0) + cur_w*color[h];
            for(int k=0;k<3;k++)
            {
                color = cImg.at<cv::Vec3b>(cur_y, cur_x);
                cov.at<float>(h, k) = cov.at<float>(h, k) + cur_w*color[h]*color[k];
            }
        }
        
        total_w += cur_w;
    }
    
    float inv_total_w = 1.0f/total_w;
    for(int h=0;h<3;h++)
    {
        mean.at<float>(h, 0) = mean.at<float>(h, 0) * inv_total_w;
        for(int k=0;k<3;k++)
            cov.at<float>(h, k) = cov.at<float>(h, k) * inv_total_w;
    }
    
    for(int h=0;h<3;h++)
    {
        for(int k=0;k<3;k++)
        {
             cov.at<float>(h, k) = cov.at<float>(h, k) - mean.at<float>(h, 0)*mean.at<float>(k, 0);
        }
    }
}

void BayesianMatting::CalculateWeightMeanCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, float &weight, Mat mean, Mat cov)
{
    int cur_x, cur_y;
    float cur_w, total_w=0;
    Mat(Mat::zeros(mean.rows, mean.cols, CV_32FC1)).copyTo(mean);
    Mat(Mat::zeros(cov.rows, cov.cols, CV_32FC1)).copyTo(cov);    
    for(size_t j=0;j<clus_set.size();j++)
    {
        cur_x = clus_set[j].first.x;
        cur_y = clus_set[j].first.y;
        cur_w = clus_set[j].second;
        
        for(int h=0;h<3;h++)
        {
            cv::Vec3b color = cImg.at<cv::Vec3b>(cur_y, cur_x);
            mean.at<float>(h, 0) = mean.at<float>(h, 0) + cur_w*color[h];
            for(int k=0;k<3;k++)
            {
                //color = cImg.at<cv::Vec3b>(cur_y, cur_x);
                cov.at<float>(h, k) = cov.at<float>(h, k) + cur_w*color[h]*color[k];
            }
        }
        
        total_w += cur_w;
    }
    
    float inv_total_w = 1.0f/total_w;
    for(int h=0;h<3;h++)
    {
        mean.at<float>(h, 0) = mean.at<float>(h, 0) * inv_total_w;
        for(int k=0;k<3;k++)
            cov.at<float>(h, k) = cov.at<float>(h, k) * inv_total_w;
    }
    
    for(int h=0;h<3;h++)
    {
        for(int k=0;k<3;k++)
        {
             cov.at<float>(h, k) = cov.at<float>(h, k) - mean.at<float>(h, 0)*mean.at<float>(k, 0);
        }
    }
    
    weight = total_w;
    
    //printf("mean = {%f, %f, %f}\n", mean.at<float>(0, 0), mean.at<float>(1, 0), mean.at<float>(2, 0));
}
