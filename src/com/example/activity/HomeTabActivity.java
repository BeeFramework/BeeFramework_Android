package com.example.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.FrameLayout.LayoutParams;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeQuery;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */

public class HomeTabActivity extends TabActivity {

    private TabHost tabHost;
    
    private ImageView image;
	private TranslateAnimation mTranslateAnimation;
	private int width;
	int start;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tab);
        
        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT) {
            BeeFrameworkApp.getInstance().showBug(this);
        }
        
        width = getWindowManager().getDefaultDisplay().getWidth()/5;
        
		image = (ImageView) findViewById(R.id.home_tab_image);
		LayoutParams params = (LayoutParams) image.getLayoutParams();
		params.width = width;
		image.setLayoutParams(params);	
        
        tabHost = getTabHost();
        TabHost.TabSpec spec_tab1 = tabHost.newTabSpec("spec_tab1").setIndicator("spec_tab1")
                .setContent(new Intent(HomeTabActivity.this,HighlightButtonActivity.class));
        tabHost.addTab(spec_tab1);

        TabHost.TabSpec spec_tab2 = tabHost.newTabSpec("spec_tab2").setIndicator("spec_tab2")
                .setContent(new Intent(HomeTabActivity.this,TrendActivity.class));
        tabHost.addTab(spec_tab2);

        TabHost.TabSpec spec_tab3 = tabHost.newTabSpec("spec_tab3").setIndicator("spec_tab3")
                .setContent(new Intent(HomeTabActivity.this,TimelineActivity.class));
        tabHost.addTab(spec_tab3);

        TabHost.TabSpec spec_tab4 = tabHost.newTabSpec("spec_tab4").setIndicator("spec_tab4")
                .setContent(new Intent(HomeTabActivity.this,GalleryImageActivity.class));
        tabHost.addTab(spec_tab4);
        
        TabHost.TabSpec spec_tab5 = tabHost.newTabSpec("spec_tab5").setIndicator("spec_tab5")
                .setContent(new Intent(HomeTabActivity.this,TeamIntroduction.class));
        tabHost.addTab(spec_tab5);

        RadioGroup group = (RadioGroup) this.findViewById(R.id.tab_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.tab_one:
                        tabHost.setCurrentTabByTag("spec_tab1");
                        mTranslateAnimation = new TranslateAnimation(start, 0,0, 0);
    					mTranslateAnimation.setDuration(200);
    					mTranslateAnimation.setFillEnabled(true);
    					mTranslateAnimation.setFillAfter(true);
    					image.startAnimation(mTranslateAnimation);
    					start = 0;
                        break;
                    case R.id.tab_two:
                        tabHost.setCurrentTabByTag("spec_tab2");
                        mTranslateAnimation = new TranslateAnimation(start, width,0, 0);
    					mTranslateAnimation.setDuration(200);
    					mTranslateAnimation.setFillEnabled(true);
    					mTranslateAnimation.setFillAfter(true);
    					image.startAnimation(mTranslateAnimation);
    					start = width;
                        break;
                    case R.id.tab_three:
                        tabHost.setCurrentTabByTag("spec_tab3");
                        mTranslateAnimation = new TranslateAnimation(start, width*2,0, 0);
    					mTranslateAnimation.setDuration(200);
    					mTranslateAnimation.setFillEnabled(true);
    					mTranslateAnimation.setFillAfter(true);
    					image.startAnimation(mTranslateAnimation);
    					start = width*2;
                        break;
                    case R.id.tab_four:
                        tabHost.setCurrentTabByTag("spec_tab4");
                        mTranslateAnimation = new TranslateAnimation(start, width*3,0, 0);
    					mTranslateAnimation.setDuration(100);
    					mTranslateAnimation.setFillEnabled(true);
    					mTranslateAnimation.setFillAfter(true);
    					image.startAnimation(mTranslateAnimation);
    					start = width*3;
                        break;
                    case R.id.tab_five:
                        tabHost.setCurrentTabByTag("spec_tab5");
                        mTranslateAnimation = new TranslateAnimation(start, width*4,0, 0);
    					mTranslateAnimation.setDuration(100);
    					mTranslateAnimation.setFillEnabled(true);
    					mTranslateAnimation.setFillAfter(true);
    					image.startAnimation(mTranslateAnimation);
    					start = width*4;
                        break;
                }
            }
        });
    }
}
