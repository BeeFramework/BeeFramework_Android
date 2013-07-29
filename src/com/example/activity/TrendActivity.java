package com.example.activity;

import android.os.Bundle;
import com.example.adapter.TrendAdapter;
import com.example.model.TrendModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.BeeFramework.example.R;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
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
public class TrendActivity extends BaseActivity implements BusinessResponse
{
    XListView listView;
    TrendAdapter listAdapter;
    TrendModel  trendModel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trend_activity);
        listView = (XListView)findViewById(R.id.trend_list);
        trendModel = new TrendModel(this);
        listAdapter = new TrendAdapter(this,trendModel.dataList);
        listView.setAdapter(listAdapter);
        trendModel.fetchPreTopic();
        trendModel.addResponseListener(this);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        trendModel.removeResponseListener(this);
    }
}
