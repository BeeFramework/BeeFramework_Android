package com.example.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.view.DarkImageView;
import com.BeeFramework.view.ToastView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
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
public class PhotoDetailActivity extends BaseActivity {
    public static final String IMAGE_URL="image_url";
    private TextView title;
    private DarkImageView topview_back;
    private Intent intent;
    private ImageView photo_detail_bigImage;
    private ImageView photo_loading;
    private AnimationDrawable animationDrawable = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        title = (TextView) findViewById(R.id.topview_title);
        title.setText("预览");
        topview_back= (DarkImageView) findViewById(R.id.topview_back);
        topview_back.setVisibility(View.VISIBLE);
        topview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photo_detail_bigImage= (ImageView) findViewById(R.id.photo_detail_bigImage);
        photo_loading = (ImageView) findViewById(R.id.photo_detail_loading);
        intent=getIntent();
        String image_url=intent.getStringExtra(IMAGE_URL);
        
        photo_loading.setImageResource(R.anim.loading_animation);
		animationDrawable = (AnimationDrawable)photo_loading.getDrawable();
		animationDrawable.setOneShot(false);
		animationDrawable.start();
		
		// 加载图片带有加载提示
        ImageLoader.getInstance().displayImage(image_url,photo_detail_bigImage, BeeFrameworkApp.options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				photo_loading.setVisibility(View.VISIBLE);
				photo_detail_bigImage.setVisibility(View.GONE);
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				animationDrawable.stop();	
				photo_loading.setVisibility(View.GONE);// 不显示圆形进度条
				photo_detail_bigImage.setVisibility(View.VISIBLE);
			}
		});
    }
}
