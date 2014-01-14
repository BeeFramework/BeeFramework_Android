package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeQuery;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.BeeInjectId;
import com.BeeFramework.view.DarkImageView;
import com.BeeFramework.view.HighlightImageView;
import com.example.adapter.CommentAdapter;
import com.example.adapter.ShotAdapter;
import com.example.model.CommentModel;
import com.example.model.ShotDetailModel;
import com.example.model.ShotModel;
import com.example.protocol.ApiInterface;
import com.example.protocol.SHOT;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;
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

public class CommentListActivity extends BaseActivity implements BusinessResponse, IXListViewListener {
    @BeeInjectId(id = R.id.shot_detail_comment_listview)
    private XListView shot_detail_comment_listview;
    public static  final String  SHOT_ID="shot_id";
    private Intent intent;
    private int shot_id;
    private TextView title;
    private CommentModel commentModel;
    private CommentAdapter commentAdapter;
    private DarkImageView topview_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        title = (TextView) findViewById(R.id.topview_title);
        title.setText("Comment列表");
        intent= getIntent();
        shot_id=intent.getIntExtra(SHOT_ID, 0);
        topview_back= (DarkImageView) findViewById(R.id.topview_back);
        topview_back.setVisibility(View.VISIBLE);
        topview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shot_detail_comment_listview.setXListViewListener(this, 0);
        shot_detail_comment_listview.setPullLoadEnable(true);
        shot_detail_comment_listview.setRefreshTime();
        commentModel=new CommentModel(this);
        commentModel.addResponseListener(this);
        commentModel.getComments(shot_id);

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        shot_detail_comment_listview.stopRefresh();
        shot_detail_comment_listview.stopLoadMore();
        if(url.startsWith(BeeQuery.getAbsoluteUrl(ApiInterface.SHOT_LIST + "/" + shot_id + "/comments"))){
            int size = jo.optInt("total");
            if(commentModel.dataList.size() >= size) {
                shot_detail_comment_listview.setPullLoadEnable(false);
            } else {
                shot_detail_comment_listview.setPullLoadEnable(true);
            }
            if(commentAdapter == null) {
                commentAdapter = new CommentAdapter(this, commentModel.dataList);
                shot_detail_comment_listview.setAdapter(commentAdapter);
            } else {
                commentAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onRefresh(int id) {
        commentModel.getComments(shot_id);
        shot_detail_comment_listview.setRefreshTime();

    }

    @Override
    public void onLoadMore(int id) {
        commentModel.getCommentsNext(shot_id);
    }
}
