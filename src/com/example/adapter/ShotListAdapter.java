package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.adapter.BeeBaseAdapter;
import com.BeeFramework.example.R;
import com.BeeFramework.view.WebImageView;
import com.example.protocol.SHOT;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

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
public class ShotListAdapter extends BeeBaseAdapter
{
    public class ShotHolder extends BeeCellHolder
    {
        TextView        shotTitle;
        ImageView       shotImage;

        TextView        likeCount;
        TextView        commentCount;
    }

    public ShotListAdapter(Context c, ArrayList dataList)
    {
        super(c, dataList);
    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView)
    {
        ShotHolder holder       = new ShotHolder();
        holder.shotTitle        = (TextView)cellView.findViewById(R.id.shot_title);
        holder.shotImage        = (ImageView)cellView.findViewById(R.id.shot_image);
        holder.likeCount        = (TextView)cellView.findViewById(R.id.like_count);
        holder.commentCount     = (TextView)cellView.findViewById(R.id.comment_count);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h)
    {
        SHOT shot = (SHOT)dataList.get(position);
        ShotHolder holder = (ShotHolder)h;

        holder.shotTitle.setText(shot.title);
        ImageLoader.getInstance().displayImage(shot.image_url, holder.shotImage, BeeFrameworkApp.options);

        holder.likeCount.setText(shot.likes_count +"");
        holder.commentCount.setText(shot.comments_count + "");
        return null;
    }

    @Override
    public View createCellView()
    {
        return mInflater.inflate(R.layout.shot_item,null);
    }
}
