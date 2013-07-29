package com.example.model;

import android.content.Context;
import com.example.protocol.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;
import com.BeeFramework.model.BaseModel;
import com.BeeFramework.model.BeeCallback;
import com.BeeFramework.view.MyProgressDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
public class TrendModel extends BaseModel
{
    public ArrayList <String> dataList = new ArrayList<String>();
    public TrendModel(Context context)
    {
        super(context);
    }
    public void fetchPreTopic() {
        String url = ApiInterface.TRENDS_DAILY;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)  {

                TrendModel.this.callback(url, jo, status);
                JSONObject trendsJSONObject = jo.optJSONObject("trends");
                Iterator it = trendsJSONObject.keys();
                if (it.hasNext())
                {
                    String trend_key = (String)it.next();
                    JSONArray trend_array = trendsJSONObject.optJSONArray(trend_key);
                    dataList.clear();
                    try
                    {
                        for (int i = 0; i < trend_array.length(); i++)
                        {
                            JSONObject trend_item  = trend_array.getJSONObject(i);
                            String trend_name = trend_item.optString("query");
                            dataList.add(trend_name);
                        }
                        TrendModel.this.OnMessageResponse(url, jo, status);
                    }
                    catch (JSONException e)
                    {

                    }


                }


            }

        };


        HashMap<String,String> params = new HashMap<String, String>();


        url +="?source=3880614442&baseapp=0&access_token=2.00h5EowBWFfcOE0f877310cdrp1zXD";

        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);

        MyProgressDialog mPro = new MyProgressDialog(mContext, "请稍后...");

        aq.progress(mPro.mDialog).ajax(cb);
    }
}
