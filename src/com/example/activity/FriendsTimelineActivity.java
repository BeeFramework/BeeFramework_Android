package com.example.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.BeeFramework.activity.MainActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BusinessResponse;
import com.example.adapter.TimelineAdapter;
import com.example.model.FriendsTimelineModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import org.json.JSONException;
import org.json.JSONObject;
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
public class FriendsTimelineActivity extends MainActivity implements BusinessResponse, XListView.IXListViewListener
{
    FriendsTimelineModel datamodel;
    TimelineAdapter     TopicSearchAdapter;
    XListView           feedlistView;
    SharedPreferences   shared;

    private SharedPreferences .Editor editor;

    public static final String tag="sinasdk";
    public static final String ACCESS_TOKEN="access_token";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        shared=this.getSharedPreferences("user_info",0);
        editor=shared.edit();
        datamodel=new FriendsTimelineModel(this);
        datamodel.addResponseListener(this);
        String access_token = shared.getString(ACCESS_TOKEN,"");
        datamodel.fetchPreFeed(access_token);

        feedlistView= (XListView) findViewById(R.id.feed_list);
        feedlistView.setPullLoadEnable(true);
        feedlistView.setRefreshTime() ;
        feedlistView.setXListViewListener(this,1);

        TopicSearchAdapter=new TimelineAdapter(this,datamodel.searchResult);
        feedlistView.setAdapter(TopicSearchAdapter);


    }
    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        feedlistView.setRefreshTime();
        feedlistView.stopLoadMore() ;
        feedlistView.setRefreshTime();
        TopicSearchAdapter.notifyDataSetChanged();
    }

    public void onDestory(){
        super.onDestroy ();
        datamodel.removeResponseListener(this);
    }

    @Override
    public void onRefresh(int id) {
        String access_token=shared.getString(ACCESS_TOKEN,"");
        if(null!=access_token&&access_token.length()>0){
            datamodel.fetchPreFeed(access_token);
        }
    }

    @Override
    public void onLoadMore(int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
