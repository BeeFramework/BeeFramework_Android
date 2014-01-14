package com.example.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeQuery;
import com.BeeFramework.model.BusinessResponse;
import com.example.adapter.ProfileAdapter;
import com.example.model.ShotModel;
import com.example.protocol.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
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
public class ShotActivity extends BaseActivity implements IXListViewListener, BusinessResponse {

	private ImageView       back;
	private TextView        title;
	private XListView       xlistView;
	private ProfileAdapter  profileAdapter;
	private ShotModel       shotModel;
	
	private int             playerId;
	private String          playerName;
	public static final String PLAYER_ID = "player_id";
	public static final String PLAYER_NAME = "player_name";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shot);
		
		Intent intent = getIntent();
		playerId = intent.getIntExtra(PLAYER_ID, 1);
		playerName = intent.getStringExtra(PLAYER_NAME);
		
		back = (ImageView) findViewById(R.id.topview_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


		title = (TextView) findViewById(R.id.topview_title);
		title.setText(playerName+"的Shot列表");
		xlistView = (XListView) findViewById(R.id.shot_listview);
		xlistView.setXListViewListener(this, 0);
		xlistView.setPullLoadEnable(true);
		xlistView.setRefreshTime();
		
		shotModel = new ShotModel(this);
		shotModel.addResponseListener(this);
		shotModel.getProfileShotList(playerId);
		
	}
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		shotModel.getProfileShotList(playerId);
		xlistView.setRefreshTime();
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		shotModel.getProfileShotListMore(playerId);
	}
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		xlistView.stopRefresh();
		xlistView.stopLoadMore();
		if(url.startsWith(BeeQuery.getAbsoluteUrl(ApiInterface.PLAYERS+playerId+"/shots"))) {
			int size = jo.optInt("total");
			if(shotModel.shotList.size() >= size) {
				xlistView.setPullLoadEnable(false);
			} else {
				xlistView.setPullLoadEnable(true);
			}
			if(profileAdapter == null) {
				profileAdapter = new ProfileAdapter(this, shotModel.shotList);
				xlistView.setAdapter(profileAdapter);
			} else {
				profileAdapter.notifyDataSetChanged();
			}
		}
	}
	
}
