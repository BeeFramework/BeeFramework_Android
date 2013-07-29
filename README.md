#BeeFramework 0.1

##BeeFramework是什么
BeeFramework Android版主要为Android初级开发人员提供一个Android APP的MVC开发DEMO,并提供一套APP内调试工具,包括

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

