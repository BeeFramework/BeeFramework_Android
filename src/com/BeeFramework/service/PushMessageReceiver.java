package com.BeeFramework.service;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.baidu.android.pushservice.PushConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class PushMessageReceiver extends BroadcastReceiver
{
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    /**
     *
     *
     * @param context
     *            Context
     * @param intent
     *            接收的intent
     */
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE))
        {
            //获取消息内容
//            String message = intent.getExtras().getString(
//                    PushConstants.EXTRA_PUSH_MESSAGE_STRING);
//
//            //用户自定义内容读取方式，CUSTOM_KEY值和管理界面发送时填写的key对应
//            String customContentString = intent.getExtras().getString("content");
//
//            //用户在此自定义处理消息,以下代码为demo界面展示用
//            Intent responseIntent = null;
//            responseIntent = new Intent(SquaredActivity.ACTION_MESSAGE);
//            responseIntent.putExtra(SquaredActivity.EXTRA_MESSAGE, message);
//            responseIntent.setClass(context, SquaredActivity.class);
//            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(responseIntent);

            //处理绑定等方法的返回数据
            //注:PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到
        }
        else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE))
        {
            //获取方法
            final String method = intent
                    .getStringExtra(PushConstants.EXTRA_METHOD);
            //方法返回错误码,您需要恰当处理。比如，方法为bind时，若失败，需要重新bind,即重新调用startWork
            final int errorCode = intent
                    .getIntExtra(PushConstants.EXTRA_ERROR_CODE,
                            PushConstants.ERROR_SUCCESS);
            //返回内容
            final String content = new String(
                    intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));

            //用户在此自定义处理消息,以下代码为demo界面展示用


//            Intent responseIntent = null;
//            responseIntent = new Intent(SquaredActivity.ACTION_RESPONSE);
//            responseIntent.putExtra(SquaredActivity.RESPONSE_METHOD, method);
//            responseIntent.putExtra(SquaredActivity.RESPONSE_ERRCODE,
//                    errorCode);
//            responseIntent.putExtra(SquaredActivity.RESPONSE_CONTENT, content);
//            responseIntent.setClass(context, SquaredActivity.class);
//            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(responseIntent);

            if (errorCode == 0)
            {
                String appid = "";
                String channelid = "";
                String userid = "";
                try
                {
                    JSONObject jsonContent = new JSONObject(content);
                    JSONObject params = jsonContent
                            .getJSONObject("response_params");
                    appid = params.getString("appid");
                    channelid = params.getString("channel_id");
                    userid = params.getString("user_id");
                    editor.putString("UUID",userid);
                    editor.commit();
                }
                catch (JSONException e)
                {

                }


            }


            //可选。通知用户点击事件处理
        }
        else if (intent.getAction().equals(
                PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK))
        {

//            String content = intent
//                    .getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
//
//            Intent responseIntent = null;
//            responseIntent = new Intent(SquaredActivity.ACTION_PUSHCLICK);
//
//
//            responseIntent.setClass(context, SquaredActivity.class);
//            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            //用户自定义内容读取方式，CUSTOM_KEY值和管理界面发送时填写的key对应
//            String customContentString = intent.getExtras().getString("content");
//            responseIntent.putExtra(SquaredActivity.CUSTOM_CONTENT, customContentString);
//
//            context.startActivity(responseIntent);
        }
    }
}
