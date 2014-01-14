package com.example.model;

import android.content.Context;
import com.BeeFramework.model.BaseModel;
import com.BeeFramework.model.BeeCallback;
import com.BeeFramework.view.MyProgressDialog;
import com.example.protocol.ApiInterface;
import com.example.protocol.SHOT;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
public class ShotDetailModel extends BaseModel {
    public SHOT shot;
    public ShotDetailModel(Context context) {
        super(context);
    }
     public void getShotDetail(int id){
         String url = ApiInterface.SHOT_LIST;
         BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

             @Override
             public void callback(String url, JSONObject jo, AjaxStatus status)
             {

                 ShotDetailModel.this.callback(url, jo, status);
                 try {
                     if(jo!=null){
                         shot= new SHOT();
                         shot.fromJson(jo);
                         ShotDetailModel.this.OnMessageResponse(url,jo,status);
                     }
                 } catch (JSONException e) {
                      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                 }

             }
         };

         url +="/"+id;
         cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
         aq.ajax(cb);
     }
}
