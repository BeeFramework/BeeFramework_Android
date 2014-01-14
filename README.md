#BeeFramework 0.1

##BeeFramework是什么
BeeFramework Android版主要为Android初级开发人员提供一个基于MVC开发模式的APP DEMO,并提供一套APP内调试工具,包括

*	查看网络数据请求历史
*	Crash Log列表
*	真机WIFI环境下模拟2G\3G网络
*	查看APP性能(内存占用，CPU占用等)


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

**切磋请移步QQ群:330271021**

  <a target="_blank" href="http://wp.qq.com/wpa/qunwpa?idkey=37d8346a7dfc346783bb20355f0b03742d7a99ac6d09332739d4d47a6d3128b4"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="BeeFramework android群" title="BeeFramework android群"></a>

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
	


##Change log	
###0.2
	1.替换Example代码，将新浪微薄接口，替换成Dribbble接口

	2.添加切换主题DEMO
	从服务器上下载theme.zip,解压缩到指定目录，修改Android图片资源加载方式，从相同的R文件，根据主题设置的不同，加载不同资源。
	
	3.通过注解的方式，关联UI控件与变量
	
	4.优化图片加载方式

	5.优化协议Log列表的展现方式，自动Format成JSON缩进格式










