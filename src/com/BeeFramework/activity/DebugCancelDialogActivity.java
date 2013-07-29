package com.BeeFramework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.BeeFramework.example.R;

public class DebugCancelDialogActivity extends Activity {

	
	private TextView title;
	private TextView message;
	private TextView positive;
	private TextView negative;
	
	public static Handler parentHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		
		title = (TextView) findViewById(R.id.dialog_title);
		message = (TextView) findViewById(R.id.dialog_message);
		title.setVisibility(View.GONE);
		message.setText("确定退出debug模式？");
		
		positive = (TextView) findViewById(R.id.yes);
		negative = (TextView) findViewById(R.id.no);
		
		positive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
                msg.what = 1;
                parentHandler.handleMessage(msg);
                finish();
			}
		});
		
		negative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
}
