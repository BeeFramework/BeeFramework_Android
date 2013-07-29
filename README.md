#BeeFramework 0.1

##BeeFramework是什么
BeeFramework android版主要为android开发人员提供一个android app的MVC开发解决方案，包括数据存储，网络拉取，app内调试等,以新浪微薄的两个api为demo，展示app协议调试时的协议请求历史查看，crash log列表，app系统性能，2G\3G网络模拟等功能


###app内调试
####协议拉取历史查看
通过扩展Androidquery类，添加调试模式下记录协议的发送和拉取，协议的发送继承BeeQuery后，即可以在小虫子中查看协议的拉取历史


![Mou icon](http://wal8.com/51506684)

###Activity周期
查看App内的Activity生存周期

###3G网络模拟
提供类似android模拟器的3G网络模拟功能，并且可以自由设定maxBandwidthPerSecond来限定app协议层的网络传输速率，达到在wifi环境下模拟3G网络的目的，便于测试和发现问题

![Mou icon](http://wal8.com/51560960)


###Crash log
通过截取线程的exception，记录在文本文件中，并通过小虫子及时查看本app的crashlog。

![Mou icon](http://wal8.com/51561235)

##Quick Start
###开启调试模式
继承BeeFrameworkApp,在MainActivity按返回键时，调用

	BeeFrameworkApp.getInstance().showBug(this);

###网络请求部分

	  BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>()
	  {
	  	public void callback(String url, JSONObject jo, AjaxStatus status)
            {
            	//TODO
            }
	  };	  
	  HashMap<String,String> params = new HashMap<String, String>();
	  cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
	  com.BeeFramework.model.BeeQuery aq = new BeeQuery(context);
	  aq.ajax(cb);
###开启Crash log

	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConst.LOG_DIR_PATH;
        File storePath = new File(path);
        storePath.mkdirs();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                path, null));
                
###限定网络请求速率

	BeeQuery.setForceThrottleBandwidth(true);
	BeeQuery.setMaxBandwidthPerSecond(1000);//限定1000Bytes/s                



