package com.example.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.example.R;
import com.example.activity.ProfileActivity;
import com.example.protocol.PLAYER;
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
public class FollowersAdapter extends BaseAdapter {

	private Context context;
	private List<PLAYER> list;
	private LayoutInflater inflater;
	public FollowersAdapter(Context context, List<PLAYER> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list.size()%3>0) {
			return list.size()/3+1;
		} else {
			return list.size()/3;
		}
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
			convertView = inflater.inflate(R.layout.followers_item, null);
			holder.view1 = (LinearLayout) convertView.findViewById(R.id.followers_item_view1);
			holder.head1 = (ImageView) convertView.findViewById(R.id.followers_item_head1);
			holder.name1 = (TextView) convertView.findViewById(R.id.followers_item_name1);
			
			holder.view2 = (LinearLayout) convertView.findViewById(R.id.followers_item_view2);
			holder.head2 = (ImageView) convertView.findViewById(R.id.followers_item_head2);
			holder.name2 = (TextView) convertView.findViewById(R.id.followers_item_name2);
			
			holder.view3 = (LinearLayout) convertView.findViewById(R.id.followers_item_view3);
			holder.head3 = (ImageView) convertView.findViewById(R.id.followers_item_head3);
			holder.name3 = (TextView) convertView.findViewById(R.id.followers_item_name3);
			
			LayoutParams params1 = holder.head1.getLayoutParams();
			params1.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth()/5;
			params1.height = params1.width;
			holder.head1.setLayoutParams(params1);
			
			LayoutParams params2 = holder.head2.getLayoutParams();
			params2.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth()/5;
			params2.height = params2.width;
			holder.head2.setLayoutParams(params2);
			
			LayoutParams params3 = holder.head3.getLayoutParams();
			params3.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth()/5;
			params3.height = params3.width;
			holder.head3.setLayoutParams(params3);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.view2.setVisibility(View.VISIBLE);
		holder.view3.setVisibility(View.VISIBLE);
		
		final PLAYER player1 = list.get(position*3);
		final PLAYER player2 = list.get(position*3+1);
		final PLAYER player3 = list.get(position*3+2);
		
		ImageLoader.getInstance().displayImage(player1.avatar_url, holder.head1, BeeFrameworkApp.options_head);
		holder.name1.setText(player1.name);
		holder.head1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra(ProfileActivity.PLAYER_ID, player1.id);
				context.startActivity(intent);
			}
		});
		
		if(player2.avatar_url == null) {
			holder.view2.setVisibility(View.INVISIBLE);
		} else {
			ImageLoader.getInstance().displayImage(player2.avatar_url, holder.head2, BeeFrameworkApp.options_head);
			holder.name2.setText(player2.name);
		}
		holder.head2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra(ProfileActivity.PLAYER_ID, player2.id);
				context.startActivity(intent);
			}
		});
		
		if(player3.avatar_url == null) {
			holder.view3.setVisibility(View.INVISIBLE);
		} else {
			ImageLoader.getInstance().displayImage(player3.avatar_url, holder.head3, BeeFrameworkApp.options_head);
			holder.name3.setText(player3.name);
		}
		holder.head3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra(ProfileActivity.PLAYER_ID, player3.id);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		LinearLayout view1;
		LinearLayout view2;
		LinearLayout view3;
		ImageView head1;
		ImageView head2;
		ImageView head3;
		TextView name1;
		TextView name2;
		TextView name3;
	}

}
