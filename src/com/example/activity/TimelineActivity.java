package com.example.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.example.adapter.TimelineAdapter;
import com.example.model.TimelineModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.BeeFramework.AppConst;
import com.BeeFramework.example.R;
import com.BeeFramework.activity.MainActivity;
import com.BeeFramework.model.BusinessResponse;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.weibo.sdk.android.*;
import com.weibo.sdk.android.sso.SsoHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

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
public class TimelineActivity extends MainActivity  implements BusinessResponse, IXListViewListener
{
    TimelineModel       dataModel;
    TimelineAdapter     listAdapter;
    XListView           feedListView;
    Weibo               mWeibo;
    SharedPreferences   shared;
    private SharedPreferences.Editor editor;


    public static final String TAG = "sinasdk";
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * SsoHandler 仅当sdk支持sso时有效，
     */
    private SsoHandler mSsoHandler;

    public static final String SCOPE = "email,direct_messages_read,direct_messages_write," +
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
            "follow_app_official_microblog";
    
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        shared = this.getSharedPreferences("user_info", 0);
        editor = shared.edit();

        mWeibo =  Weibo.getInstance(AppConst.SINA_KEY,"http://open.weibo.com/apps/"+AppConst.SINA_KEY+"/info/advanced",SCOPE);
        IWeiboAPI weiboAPI = WeiboSDK.createWeiboAPI(this,AppConst.SINA_KEY);

        mSsoHandler = new SsoHandler(this, mWeibo);
        String pkName = this.getPackageName();
        mSsoHandler.authorize(new AuthDialogListener(),pkName);

        dataModel = new TimelineModel(this);
        dataModel.addResponseListener(this);
        
        feedListView = (XListView)findViewById(R.id.feed_list);
        feedListView.setPullLoadEnable(true);
        feedListView.setRefreshTime();
        feedListView.setXListViewListener(this, 1);

        listAdapter = new TimelineAdapter(this,dataModel.searchResult);
        feedListView.setAdapter(listAdapter);
        
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        if (url.startsWith("https://api.weibo.com/oauth2/access_token"))
        {
            onRefresh(0);
        }
        else
        {
            feedListView.setRefreshTime();
            feedListView.stopLoadMore();
            feedListView.stopRefresh();
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataModel.removeResponseListener(this);
    }

	@Override
	public void onRefresh(int id)
    {
        String access_token = shared.getString(ACCESS_TOKEN,"");
        if (null != access_token && access_token.length() > 0)
        {
            dataModel.fetchPreFeed(access_token);
        }

	}

	@Override
	public void onLoadMore(int id) {
		
	}

    public void getAccessToken()
    {
        onRefresh(0);
    }

    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            String code = values.getString("code");
            if(code != null){
//                editor.putString(ACCESS_TOKEN,code);
//                editor.commit();
                dataModel.getAccessToken(code);
                return;
            }
            String token = values.getString("access_token");

            editor.putString(ACCESS_TOKEN,token);
            editor.commit();

            Toast.makeText(TimelineActivity.this, "认证成功", Toast.LENGTH_SHORT)
                        .show();
            TimelineActivity.this.getAccessToken();

        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // sso 授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
