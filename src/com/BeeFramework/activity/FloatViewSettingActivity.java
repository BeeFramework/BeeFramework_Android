package com.BeeFramework.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.BeeFramework.example.R;
import com.BeeFramework.model.BeeCallback;

public class FloatViewSettingActivity extends BaseActivity {

	private Button on;
	private Button off;
	private TextView title;
    private Button third_genaration;
    private Button second_genaration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floatview_setting);
		
		title = (TextView) findViewById(R.id.topview_title);
		title.setText("浮窗设置");
		
		on = (Button) findViewById(R.id.float_view_setting_on);
		off = (Button) findViewById(R.id.float_view_setting_off);
        third_genaration = (Button)findViewById(R.id.third_genaration_network);
        second_genaration = (Button)findViewById(R.id.second_genaration_network);
        third_genaration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (third_genaration.isSelected())
                {
                    third_genaration.setSelected(false);
                    third_genaration.setText("模拟3G网络");
                    BeeCallback.setForceThrottleBandwidth(false);
                }
                else
                {
                    third_genaration.setSelected(true);
                    BeeCallback.setForceThrottleBandwidth(true);
                    BeeCallback.setMaxBandwidthPerSecond(14800);
                    third_genaration.setText("取消模拟3G网络");
                    if(!isServiceRunning()) {
                        Intent intent = new Intent();
                        intent.setAction("com.BeeFramework.MemoryService");
                        startService(intent);
                    }
                }
            }
        });

        second_genaration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second_genaration.isSelected())
                {
                    second_genaration.setSelected(false);
                    second_genaration.setText("模拟2G网络");
                    BeeCallback.setForceThrottleBandwidth(false);
                }
                else
                {
                    second_genaration.setSelected(true);
                    BeeCallback.setForceThrottleBandwidth(true);
                    BeeCallback.setMaxBandwidthPerSecond(5000);
                    second_genaration.setText("取消模拟2G网络");
                    if(!isServiceRunning()) {
                        Intent intent = new Intent();
                        intent.setAction("com.BeeFramework.MemoryService");
                        startService(intent);
                    }
                }
            }
        });
		
		on.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isServiceRunning()) {
					Intent intent = new Intent();
					intent.setAction("com.BeeFramework.MemoryService");
					startService(intent);
				}
				
			}
		});
		
		off.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isServiceRunning()) {
					Intent intent = new Intent();
					intent.setAction("com.BeeFramework.MemoryService");
					stopService(intent);
				}
			}
		});
	}
	
	private boolean isServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if ("com.BeeFramework.service.MemoryService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
