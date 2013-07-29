package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.protocol.STATUSES;
import com.BeeFramework.example.R;
import com.BeeFramework.adapter.BeeBaseAdapter;
import com.BeeFramework.view.WebImageView;

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
public class TimelineAdapter extends BeeBaseAdapter{
    public TimelineAdapter(Context c, ArrayList dataList) {
        super(c, dataList);
    }
    public class TopicSearchHolder extends BeeCellHolder
    {
        WebImageView    userAvatar;
        TextView        userName;
        TextView        feedContent;
        WebImageView    feedPhoto;
    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView)
    {
        TopicSearchHolder holder = new TopicSearchHolder();
        holder.userAvatar = (WebImageView)cellView.findViewById(R.id.user_avatar);
        holder.userName   = (TextView)cellView.findViewById(R.id.user_name);
        holder.feedContent  = (TextView)cellView.findViewById(R.id.feed_content);
        holder.feedPhoto    = (WebImageView)cellView.findViewById(R.id.feed_photo);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h)
    {
        STATUSES feed = (STATUSES)dataList.get(position);
        TopicSearchHolder holder = (TopicSearchHolder)h;
        holder.userName.setText(feed.user.screen_name);
        holder.userAvatar.setImageWithURL(mContext,feed.user.avatar_large,R.drawable.default_user_icon);

        if (null != feed.text)
        {
            holder.feedContent.setText(feed.text);
        }

        if (null != feed.original_pic)
        {
            holder.feedPhoto.setVisibility(View.VISIBLE);
            holder.feedPhoto.setImageWithURL(mContext,feed.original_pic,R.drawable.default_image);
        }
        else
        {
            holder.feedPhoto.setVisibility(View.GONE);
        }

        return cellView;
    }

    @Override
    public View createCellView() {
        return mInflater.inflate(R.layout.feed_item,null);
    }
}
