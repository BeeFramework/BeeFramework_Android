package com.example.model;

import android.content.Context;
import com.BeeFramework.model.BaseModel;
import com.BeeFramework.model.BeeCallback;
import com.BeeFramework.view.MyProgressDialog;
import com.example.protocol.ApiInterface;
import com.example.protocol.SHOT;
import com.example.protocol.statusespublic_timelineResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class LikeShotModel extends BaseModel
{
    public ArrayList<SHOT> shotList = new ArrayList<SHOT>();

    public static final int PER_PAGE = 10;
    int total;

    public LikeShotModel(Context context) {
        super(context);
    }
    
	public void getLikeShotList(int id) {
        
        String url = ApiInterface.PLAYERS;
        int page = 1;
        int per_page = PER_PAGE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)
            {
            	LikeShotModel.this.callback(url, jo, status);
            	
                if (null != jo)
                {
                    shotList.clear();
                    try
                    {
                        JSONArray shotArray = jo.optJSONArray("shots");
                        for (int i = 0; i < shotArray.length(); i++)
                        {
                            JSONObject jsonItem = shotArray.getJSONObject(i);
                            SHOT shotItem = new SHOT();
                            shotItem.fromJson(jsonItem);
                            shotList.add(shotItem);
                        }
                        LikeShotModel.this.OnMessageResponse(url, jo, status);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        url += id;
        url += "/shots/likes?" + "page="+page+"&per_page="+per_page;
        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
        MyProgressDialog mPro = new MyProgressDialog(mContext, "请稍后...");
        aq.progress(mPro.mDialog).ajax(cb);
    }
	
	public void getLikeShotListMore(int id) {
        
        String url = ApiInterface.PLAYERS;
        int page = shotList.size() / PER_PAGE + 1;
        int per_page = PER_PAGE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)
            {
            	LikeShotModel.this.callback(url, jo, status);
            	
                if (null != jo)
                {
                    try
                    {
                        JSONArray shotArray = jo.optJSONArray("shots");
                        for (int i = 0; i < shotArray.length(); i++)
                        {
                            JSONObject jsonItem = shotArray.getJSONObject(i);
                            SHOT shotItem = new SHOT();
                            shotItem.fromJson(jsonItem);
                            shotList.add(shotItem);
                        }
                        LikeShotModel.this.OnMessageResponse(url, jo, status);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        url += id;
        url += "/shots/likes?" + "page="+page+"&per_page="+per_page;
        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
        aq.ajax(cb);
    }

}
