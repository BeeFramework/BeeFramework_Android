package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.example.adapter.GalleryImageAdapter;
import com.external.viewpagerindicator.PageIndicator;

public class GalleryImageActivity extends BaseActivity {

	private ViewPager imagePager;
	private PageIndicator mIndicator;
	private GalleryImageAdapter galleryImageAdapter;

	private List<String> list = new ArrayList<String>();

	private String[] imagePath = new String[] {
			"http://g.hiphotos.baidu.com/album/w%3D2048/sign=10e27d76adaf2eddd4f14ee9b92800e9/bd315c6034a85edfeb5afd5348540923dc5475ef.jpg",
			"http://d.hiphotos.baidu.com/album/w%3D2048/sign=e5974229adaf2eddd4f14ee9b92800e9/bd315c6034a85edf1e2fc20c48540923dd547579.jpg",
			"http://a.hiphotos.baidu.com/album/w%3D2048/sign=87622c1934fae6cd0cb4ac613b8b0e24/728da9773912b31ba01800078718367adab4e1ac.jpg",
			"http://c.hiphotos.baidu.com/album/w%3D2048/sign=bb64b3117aec54e741ec1d1e8d009a50/574e9258d109b3de06d9da47cdbf6c81800a4cbe.jpg",
			"http://a.hiphotos.baidu.com/album/w%3D2048/sign=abdce0dbb64543a9f51bfdcc2a2f8b82/0b7b02087bf40ad17a1cf9cc562c11dfa9ecce20.jpg",
			"http://b.hiphotos.baidu.com/album/w%3D2048/sign=ecb30bc8902397ddd6799f046dbab3b7/9c16fdfaaf51f3de3d048b4b95eef01f3a29796e.jpg",
			"http://d.hiphotos.baidu.com/album/w%3D2048/sign=0176fc51738b4710ce2ffaccf7f6c2fd/c995d143ad4bd113c827b3425bafa40f4afb05d3.jpg",
			"http://c.hiphotos.baidu.com/album/w%3D2048/sign=1a929103ac4bd11304cdb0326e97a40f/2f738bd4b31c8701738cc796267f9e2f0708ff8c.jpg",
			"http://g.hiphotos.baidu.com/album/w%3D2048/sign=c933feb571cf3bc7e800caece538bba1/e7cd7b899e510fb3a11e752fd833c895d1430c06.jpg",
			"http://a.hiphotos.baidu.com/album/w%3D2048/sign=67b261168882b9013dadc43347b5a977/f3d3572c11dfa9ec225d1c2660d0f703918fc194.jpg" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_image);

		for (int i = 0; i < imagePath.length; i++) {
			list.add(imagePath[i].toString());
		}

		imagePager = (ViewPager) findViewById(R.id.image_pager);

		galleryImageAdapter = new GalleryImageAdapter(this, list);
		imagePager.setAdapter(galleryImageAdapter);
		
		mIndicator = (PageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(imagePager);

	}

}
