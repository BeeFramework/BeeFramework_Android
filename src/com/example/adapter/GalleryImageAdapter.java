package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BeeFramework.example.R;
import com.external.imagezoom.ImageViewTouch;
import com.external.imagezoom.ImageViewTouchBase;
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

public class GalleryImageAdapter extends PagerAdapter {

	private LayoutInflater mInflater;
    private Context context;
    public List<String> list;

    public GalleryImageAdapter(Context context, List<String> list) {
        mInflater =  LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
    	
    	final ViewHolder holder;
		
		holder = new ViewHolder();
		View imageLayout = mInflater.inflate(R.layout.gallery_image_item, null);
		
		holder.image = (ImageViewTouch) imageLayout.findViewById(R.id.gallery_image_item_image);
		holder.image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
		
		holder.image.setImageWithURL(context, list.get(position).toString());
		
    	((ViewPager) view).addView(imageLayout, 0);
    	
        return imageLayout;
    }
    
    class ViewHolder {
		private ImageViewTouch image;
	}

}
