package com.example.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.activity.WebViewActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeQuery;
import com.BeeFramework.model.BusinessResponse;
import com.example.adapter.ProfileAdapter;
import com.example.model.PlayerModel;
import com.example.protocol.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
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
public class ProfileActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	private ImageView back;
	private TextView title;
	private ImageView head;
	private TextView name;
	private TextView location;
	private TextView net;
	private TextView shots_count;
	private TextView likes_count;
	private TextView following_count;
	private TextView followers_count;
	private LinearLayout shots_view;
	private LinearLayout likes_view;
	private LinearLayout following_view;
	private LinearLayout followers_view;
	
	private LinearLayout more;
	
	private ListView listView;
	
	private View headView;
	private View bottomView;
	
	private PlayerModel playerModel;
	private int playerId;
	private ProfileAdapter profileAdapter;
	
	public static final String PLAYER_ID = "player_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_list);
		
		title = (TextView) findViewById(R.id.topview_title);
		back = (ImageView) findViewById(R.id.topview_back);
		listView = (ListView) findViewById(R.id.profile_shots_list);
		
		Intent intent = getIntent();
		playerId = intent.getIntExtra(PLAYER_ID, 1);
		
		title.setText("个人主页");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		
		headView = LayoutInflater.from(this).inflate(R.layout.profile, null);
		bottomView = LayoutInflater.from(this).inflate(R.layout.profile_bottom, null);
		
		head = (ImageView) headView.findViewById(R.id.profile_head);
		name = (TextView) headView.findViewById(R.id.profile_name);
		location = (TextView) headView.findViewById(R.id.profile_location);
		net = (TextView) headView.findViewById(R.id.profile_net);
		shots_count = (TextView) headView.findViewById(R.id.profile_shots_count);
		likes_count = (TextView) headView.findViewById(R.id.profile_likes_count);
		following_count = (TextView) headView.findViewById(R.id.profile_following_count);
		followers_count = (TextView) headView.findViewById(R.id.profile_followers_count);
		shots_view = (LinearLayout) headView.findViewById(R.id.profile_shots_view);
		likes_view = (LinearLayout) headView.findViewById(R.id.profile_likes_view);
		following_view = (LinearLayout) headView.findViewById(R.id.profile_following_view);
		followers_view = (LinearLayout) headView.findViewById(R.id.profile_followers_view);
		
		more = (LinearLayout) bottomView.findViewById(R.id.profile_bottom_more);
		
		LayoutParams params = head.getLayoutParams();
		params.width = getWindowManager().getDefaultDisplay().getWidth()/4;
		params.height = params.width;
		head.setLayoutParams(params);
				
		
		listView.addHeaderView(headView);
		listView.addFooterView(bottomView);
		
		head.setOnClickListener(this);
		shots_view.setOnClickListener(this);
		likes_view.setOnClickListener(this);
		following_view.setOnClickListener(this);
		followers_view.setOnClickListener(this);
		
		more.setOnClickListener(this);
		
		playerModel = new PlayerModel(this);
		playerModel.addResponseListener(this);
		playerModel.getPlayer(playerId);
		
		playerModel.getProfileShotList(playerId);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId()) {
		case R.id.topview_back:
			finish();
			break;
		case R.id.profile_head:
			if(playerModel.player != null) {
				intent = new Intent(this, PhotoDetailActivity.class);
                intent.putExtra(PhotoDetailActivity.IMAGE_URL, playerModel.player.avatar_url);
                startActivity(intent);
			}
			break;
		case R.id.profile_shots_view:
			if(playerModel.player != null) {
				intent = new Intent(this, ShotActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, playerId);
				intent.putExtra(ShotActivity.PLAYER_NAME, playerModel.player.name);
				startActivity(intent);
			}
			break;
		case R.id.profile_likes_view:
			if(playerModel.player != null) {
				intent = new Intent(this, LikeShotListActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, playerId);
				intent.putExtra(ShotActivity.PLAYER_NAME, playerModel.player.name);
				startActivity(intent);
			}
			break;
		case R.id.profile_following_view:
			if(playerModel.player != null) {
				intent = new Intent(this, FollowingActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, playerId);
				intent.putExtra(ShotActivity.PLAYER_NAME, playerModel.player.name);
				startActivity(intent);
			}
			break;
		case R.id.profile_followers_view:
			if(playerModel.player != null) {
				intent = new Intent(this, FollowersActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, playerId);
				intent.putExtra(ShotActivity.PLAYER_NAME, playerModel.player.name);
				startActivity(intent);
			}
			break;
		case R.id.profile_bottom_more:
			if(playerModel.player != null) {
				intent = new Intent(this, ShotActivity.class);
				intent.putExtra(ShotActivity.PLAYER_ID, playerId);
				intent.putExtra(ShotActivity.PLAYER_NAME, playerModel.player.name);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		if(url.endsWith(ApiInterface.PLAYERS+playerId)) {
			setPlayer();
		}
		
		if(url.startsWith(BeeQuery.getAbsoluteUrl(ApiInterface.PLAYERS+playerId+"/shots"))) {
			if(playerModel.dataList.size() < 3) {
				listView.removeFooterView(bottomView);
			}
			if(profileAdapter == null) {
				profileAdapter = new ProfileAdapter(this, playerModel.dataList);
				listView.setAdapter(profileAdapter);
			} else {
				profileAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void setPlayer() {
		ImageLoader.getInstance().displayImage(playerModel.player.avatar_url, head, BeeFrameworkApp.options_head);
		name.setText(playerModel.player.name);
		location.setText(playerModel.player.location);
		
		shots_count.setText(playerModel.player.shots_count+"");
		likes_count.setText(playerModel.player.likes_count+"");
		following_count.setText(playerModel.player.following_count+"");
		followers_count.setText(playerModel.player.followers_count+"");
		
		net.setText(playerModel.player.website_url);
		
		CharSequence text = net.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) net.getText();
			URLSpan[] spans = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan span : spans) {
				JayceSpan mySpan = new JayceSpan(span.getURL());
				style.setSpan(mySpan, sp.getSpanStart(span), sp.getSpanEnd(span), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			net.setText(style);
		}
	}
	
	private class JayceSpan extends ClickableSpan {
		private String mSpan;
		JayceSpan(String span) {
			mSpan = span;
		}
		@Override
		public void onClick(View widget) {
			Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
			intent.putExtra(WebViewActivity.WEBURL, mSpan);
			intent.putExtra(WebViewActivity.WEBTITLE, playerModel.player.name);
			startActivity(intent);
		}
	}
	
}
