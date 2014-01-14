package com.example.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.BeeFramework.activity.MainActivity;
import com.BeeFramework.theme.ResourcesFactory;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeQuery;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.example.adapter.ShotAdapter;
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
public class ShotListActivity extends MainActivity implements BusinessResponse, IXListViewListener
{
    private TextView        title;
    private XListView       xlistView;
    private ShotAdapter     shopAdapter;
    private ShotModel       shotModel;
    private ImageView       rightButton;
    private FrameLayout     nav_bar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot);

        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT) {
            BeeFrameworkApp.getInstance().showBug(this);
        }

        title = (TextView) findViewById(R.id.topview_title);
        title.setText("首页");

        nav_bar = (FrameLayout)findViewById(R.id.nav_bar);

        Drawable drawable = ResourcesFactory.getDrawable(getResources(), R.drawable.nav_background);

        if (null != drawable)
        {
            nav_bar.setBackgroundDrawable(drawable);
        }




        rightButton = (ImageView)findViewById(R.id.rightButton);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShotListActivity.this, ThemeDownloadActivity.class);
                startActivity(it);
            }
        });

        xlistView = (XListView) findViewById(R.id.shot_listview);
        xlistView.setXListViewListener(this, 0);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();

        shotModel = new ShotModel(this);
        shotModel.addResponseListener(this);
        shotModel.getShotListPre("popular");

    }

    @Override
    protected void onResume()
    {
        Drawable drawable = ResourcesFactory.getDrawable(getResources(), R.drawable.nav_background);

        if (null != drawable && null != nav_bar)
        {
            nav_bar.setBackgroundDrawable(drawable);
        }
        super.onResume();
    }

    @Override
    public void onRefresh(int id) {
        // TODO Auto-generated method stub
        shotModel.getShotListPre("popular");
        xlistView.setRefreshTime();
    }
    @Override
    public void onLoadMore(int id) {
        // TODO Auto-generated method stub
        shotModel.getShotListNext("popular");
    }
    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        // TODO Auto-generated method stub
    	xlistView.stopRefresh();
    	xlistView.stopLoadMore();
        
        if(url.startsWith(BeeQuery.getAbsoluteUrl(ApiInterface.SHOT_LIST))) {
            int size = jo.optInt("total");
            if(shotModel.dataList.size() >= size) {
                xlistView.setPullLoadEnable(false);
            } else {
                xlistView.setPullLoadEnable(true);
            }
            if(shopAdapter == null) {
                shopAdapter = new ShotAdapter(this, shotModel.dataList);
                xlistView.setAdapter(shopAdapter);
            } else {
                shopAdapter.notifyDataSetChanged();
            }
        }
    }
    
	// 退出操作
	private boolean isExit = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				ToastView toast = new ToastView(getApplicationContext(), "再按一次退出BeeExample");
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Handler mHandler = new Handler(){
		            @Override
		            public void handleMessage(Message msg) {
		                super.handleMessage(msg);
		                isExit = false;
		            }
		        };
		        mHandler.sendEmptyMessageDelayed(0, 3000);
				return true;
			} else {
				finish();
				return false;
			}
		}
		return true;
	}

}
