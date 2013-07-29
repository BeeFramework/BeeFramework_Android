package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.BeeFramework.example.R;
import com.BeeFramework.adapter.BeeBaseAdapter;

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
public class TrendAdapter extends BeeBaseAdapter{
    public TrendAdapter(Context c, ArrayList dataList)
    {
        super(c, dataList);
    }

    public class TrendHolder extends BeeCellHolder
    {
        public TextView trendTimeTextView;
        public TextView trendContentTextView;
    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView)
    {
        TrendHolder holder = new TrendHolder();
        holder.trendTimeTextView = (TextView)cellView.findViewById(R.id.topic_time);
        holder.trendContentTextView = (TextView)cellView.findViewById(R.id.topic_content);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h)
    {
        String trend_name = (String)dataList.get(position);
        TrendHolder holder = (TrendHolder)h;
        holder.trendContentTextView.setText(trend_name);
        return cellView;
    }

    @Override
    public View createCellView()
    {
        return mInflater.inflate(R.layout.trend_topic,null);
    }
}
