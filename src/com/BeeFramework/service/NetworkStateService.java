package com.BeeFramework.service;

import com.BeeFramework.view.ToastView;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.view.Gravity;

public class NetworkStateService extends Service {

	private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                //System.out.println("网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    if(name.equals("WIFI")) { // Wifi
                    	editor.putString("netType", "wifi");
            			editor.commit();
            			ToastView toast = new ToastView(context, "当前网络WIFI");
            			toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else { // 2G/3G
                    	editor.putString("netType", "3g");
            			editor.commit();
            			ToastView toast = new ToastView(context, "当前网络2G/3G");
            			toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else { // 无网络
                	editor.putString("netType", "null");
        			editor.commit();
        			ToastView toast = new ToastView(context, "无网络");
        			toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		shared = getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
}
