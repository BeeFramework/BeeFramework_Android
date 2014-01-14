package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.example.R;
import com.example.activity.CommentListActivity;
import com.example.activity.PhotoDetailActivity;
import com.example.activity.ProfileActivity;
import com.example.activity.ShotActivity;
import com.example.protocol.SHOT;
import com.nostra13.universalimageloader.core.ImageLoader;
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
public class ShotAdapter extends BaseAdapter {

	private Context context;
	private List<SHOT> list;
	private LayoutInflater inflater;
	public ShotAdapter(Context context, List<SHOT> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shot_list_item, null);
			holder.head = (ImageView) convertView.findViewById(R.id.shot_item_head);
			holder.name = (TextView) convertView.findViewById(R.id.shot_item_name);
			holder.time = (TextView) convertView.findViewById(R.id.shot_item_time);
			holder.image = (ImageView) convertView.findViewById(R.id.shot_item_image);
			holder.comments_count = (TextView) convertView.findViewById(R.id.shot_item_comments_count);
			holder.likes_count = (TextView) convertView.findViewById(R.id.shot_item_likes_count);
			holder.views_count = (TextView) convertView.findViewById(R.id.shot_item_views_count);
			holder.ll_comment= (LinearLayout) convertView.findViewById(R.id.ll_comment);

			ViewTreeObserver vto = holder.image.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					holder.image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int width = holder.image.getWidth();
					
					LayoutParams params = holder.image.getLayoutParams();
					params.height = width / 4 * 3;
					holder.image.setLayoutParams(params);
					
					LayoutParams params2 = holder.head.getLayoutParams();
					params2.width = width / 6;
					params2.height = width / 6;
					holder.head.setLayoutParams(params2);
				}
			});
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).player.avatar_url, holder.head, BeeFrameworkApp.options_head);
		holder.name.setText(list.get(position).player.name);
		holder.time.setText(TimeUtil.timeAgo(list.get(position).created_at));
		ImageLoader.getInstance().displayImage(list.get(position).image_teaser_url, holder.image, BeeFrameworkApp.options);
		holder.comments_count.setText(list.get(position).comments_count+"");
		holder.likes_count.setText(list.get(position).likes_count+"");
		holder.views_count.setText(list.get(position).views_count+"");
		
		holder.head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, list.get(position).player.id);
				context.startActivity(intent);
			}
		});
		 holder.image.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, PhotoDetailActivity.class);
                 intent.putExtra(PhotoDetailActivity.IMAGE_URL, list.get(position).image_url);
                 context.startActivity(intent);
             }
         });
        holder.ll_comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentListActivity.class);
                intent.putExtra(CommentListActivity.SHOT_ID,list.get(position).id);
                context.startActivity(intent);
            }
        });
		return convertView;
	}
	
	class ViewHolder{
		ImageView head;
		TextView name;
		TextView time;
		ImageView image;
		TextView comments_count;
		TextView likes_count;
		TextView views_count;
        LinearLayout ll_comment;
	}

}
