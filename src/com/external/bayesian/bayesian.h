#include <vector>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;

#define BAYESIAN_NUMBER_NEAREST   50
#define BAYESIAN_SIGMA            8.f
#define BAYESIAN_SIGMA_C          5.f
#define BAYESIAN_MAX_CLUS           3
#define WIN_SIZE 1
class BayesianMatting
{
public:
    BayesianMatting(Mat img, Mat trimap);
    ~BayesianMatting();
    
    void setParameters(int n=BAYESIAN_NUMBER_NEAREST, double sigma=BAYESIAN_SIGMA, double sigmaC=BAYESIAN_SIGMA_C);
	    void Composite(Mat src, Mat alpha,Mat &dst);
    double solve();
    vector<Point2i> getContour(Mat trimap);	
   
	Mat img, fgImg, bgImg,unKnownImg;
    Mat maskFg, maskBg, maskUnknown, maskUnsolved;
    Mat trimap, alphamap;
	
private:
    void initialize();

    void CollectSampleSet(int x, int y, vector<pair<cv::Point, float> > &fg_set, vector<pair<cv::Point, float> > &bg_set);
    void GetGMMModel(int x, int y, vector<float> &fg_weight, vector<Mat> &fg_mean, vector<Mat> inv_fg_cov, vector<float> &bg_weight, vector<Mat> bg_mean, vector<Mat> inv_bg_cov);
    void CalculateNonNormalizeCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, Mat mean, Mat cov);
    void CalculateMeanCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, Mat mean, Mat cov);
    void CalculateWeightMeanCov(Mat cImg, vector<pair<cv::Point, float> > &clus_set, float &weight, Mat mean, Mat cov);
    void InitializeAlpha(int x, int y, Mat unsolvedMask);
    void SolveAlpha(int x, int y);
    void SolveBF(int x, int y, Mat fg_mean, Mat inv_fg_cov, Mat bg_mean, Mat inv_bg_cov);
    float computeLikelihood(int x, int y, Mat fg_mean, Mat inv_fg_cov, Mat bg_mean, Mat inv_bg_cov);
    int nearest;
    double sigma, sigmaC;
};
