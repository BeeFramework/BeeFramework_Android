#BeeFramework 0.1

##BeeFramework是什么
BeeFramework Android版主要为Android初级开发人员提供一个基于MVC开发模式的APP DEMO,并提供一套APP内调试工具,包括

*	查看网络数据请求历史
*	Crash Log列表
*	WIFI环境下模拟2G\3G网络等功能


##快速开始
###开启调试模式
需要Application继承自BeeFrameworkApp,在MainActivity按返回键时，调用

	BeeFrameworkApp.getInstance().showBug(this);

###网络数据请求

	BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>()
	{
		public void callback(String url, JSONObject jo, AjaxStatus status)
	         {
	         	//TODO
	         }
	};	  
	cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
	com.BeeFramework.model.BeeQuery aq = new BeeQuery(context);
	aq.ajax(cb);
###开启Crash Log

	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConst.LOG_DIR_PATH;
	File storePath = new File(path);
	storePath.mkdirs();
	Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
           path, null));
                
###模拟2G/3G网络

	BeeQuery.setForceThrottleBandwidth(true);
	BeeQuery.setMaxBandwidthPerSecond(1000);//限定1000Bytes/s                

**[下载APK安装包，开始体验](https://play.google.com/store/apps/details?id=com.BeeFramework.example)**

##作者

**Howiezhang**

+ [https://github.com/howiezhang](https://github.com/howiezhang)
+ [zhanghao@geek-zoo.com](zhanghao@geek-zoo.com)

**HouXin**

+ [https://github.com/houxin](https://github.com/houxin)
+ [houxin@geek-zoo.com](houxin@geek-zoo.com)

## Copyright and license
<br/>

Copyright 2013 ~ 2014, [Geek-Zoo Studio, Inc.](http://www.geek-zoo.com) and [INSTHUB Beijing HQ](http://www.insthub.com)


	 ______    ______    ______
	/\  __ \  /\  ___\  /\  ___\
	\ \  __<  \ \  __\_ \ \  __\_
	 \ \_____\ \ \_____\ \ \_____\
	  \/_____/  \/_____/  \/_____/


	Copyright (c) 2013-2014, {Bee} open source community
	http://www.bee-framework.com


	Permission is hereby granted, free of charge, to any person obtaining a
	copy of this software and associated documentation files (the "Software"),
	to deal in the Software without restriction, including without limitation
	the rights to use, copy, modify, merge, publish, distribute, sublicense,
	and/or sell copies of the Software, and to permit persons to whom the
	Software is furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
	IN THE SOFTWARE.




