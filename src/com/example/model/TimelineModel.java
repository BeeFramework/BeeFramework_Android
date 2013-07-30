package com.example.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.protocol.ApiInterface;
import com.example.protocol.STATUSES;
import com.example.protocol.statusespublic_timelineResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;
import com.BeeFramework.model.BaseModel;
import com.BeeFramework.model.BeeCallback;
import com.BeeFramework.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.BeeFramework.model.BeeQuery;


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

public class TimelineModel extends BaseModel {
    public ArrayList<STATUSES> searchResult = new ArrayList<STATUSES>();
    
    public TimelineModel(Context context) {
        super(context);
    }

    public void fetchPreFeed(String token) {
        String url = ApiInterface.STATUSES_PUBLIC_TIMELINE;

        
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){
        	
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)
            {

            	TimelineModel.this.callback(url, jo, status);
                try
                {
                    statusespublic_timelineResponse response = new statusespublic_timelineResponse();
                    response.fromJson(jo);
                    searchResult.clear();
                    if(response.statuses.size() > 0)
                    {
                        searchResult.addAll(response.statuses);
                        TimelineModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
        
        HashMap<String,String> params = new HashMap<String, String>();

        url +="?source=804874423&count=10&access_token="+token;
        
        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
        MyProgressDialog mPro = new MyProgressDialog(mContext, "请稍后...");
        
        aq.progress(mPro.mDialog).ajax(cb);
        
    }

    public void getAccessToken(String code) {
        String url = "https://api.weibo.com/oauth2/access_token";


        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)
            {

                TimelineModel.this.callback(url, jo, status);

                if (jo.has("access_token"))
                {
                    String access_token = jo.optString("access_token");

                    SharedPreferences shared;
                    SharedPreferences.Editor editor;
                    shared = mContext.getSharedPreferences("user_info", 0);
                    editor = shared.edit();
                    editor.putString("access_token",access_token);
                    editor.commit();

                    try
                    {
                        TimelineModel.this.OnMessageResponse(url, jo, status);
                    }
                    catch (JSONException e)
                    {

                    }

                }
            }

        };

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("client_id","804874423");
        params.put("client_secret","a4151118fc6ba19992fd892155ddd95e");
        params.put("grant_type","authorization_code");
        params.put("code",code);
        params.put("redirect_uri","http://open.weibo.com/apps/804874423/info/advanced");

        cb.url(url).type(JSONObject.class).method(Constants.METHOD_POST);
        cb.params(params);
        MyProgressDialog mPro = new MyProgressDialog(mContext, "请稍后...");

        aq.progress(mPro.mDialog);
        aq.ajaxAbsolute(cb);

    }
}
