package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.example.R;
import com.example.activity.ProfileActivity;
import com.example.activity.ShotActivity;
import com.example.protocol.COMMENT;
import com.example.protocol.SHOT;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
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
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<COMMENT> list;
    private LayoutInflater inflater;
    public CommentAdapter(Context context, List<COMMENT> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return position;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_comment_item, null);
            holder.head = (ImageView) convertView.findViewById(R.id.comment_item_avarta);
            holder.name = (TextView) convertView.findViewById(R.id.comment_item_name);
            holder.time = (TextView) convertView.findViewById(R.id.comment_item_time);
            holder.body=(TextView)convertView.findViewById(R.id.comment_item_cont);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).player.avatar_url, holder.head, BeeFrameworkApp.options_head);
        holder.name.setText(list.get(position).player.name);
        holder.time.setText(TimeUtil.timeAgo(list.get(position).created_at));
        holder.body.setText(list.get(position).body);



        return convertView;
    }
    class ViewHolder{
        ImageView head;
        TextView name;
        TextView time;
        TextView body;
    }
}
