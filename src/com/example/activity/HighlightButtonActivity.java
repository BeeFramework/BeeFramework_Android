package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.example.R;
import com.BeeFramework.view.BeeInjectId;
import com.BeeFramework.view.DarkButton;
import com.BeeFramework.view.DarkFrameLayout;
import com.BeeFramework.view.DarkImageView;
import com.BeeFramework.view.DarkLinearLayout;
import com.BeeFramework.view.DarkRelativeLayout;
import com.BeeFramework.view.HighlightButton;
import com.BeeFramework.view.HighlightImageView;

public class HighlightButtonActivity extends BaseActivity implements OnClickListener {

	@BeeInjectId(id = R.id.high_image)
	private HighlightImageView high_image;
	@BeeInjectId(id = R.id.high_button)
	private HighlightButton high_button;
	private DarkImageView dark_image;
	private DarkButton dark_button;
	private DarkLinearLayout linear_a;
	private DarkLinearLayout linear_b;
	private DarkFrameLayout frame_a;
	private DarkFrameLayout frame_b;
	private DarkRelativeLayout relative_a;
	private DarkRelativeLayout relative_b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//high_image = (HighlightImageView) findViewById(R.id.high_image);
		//high_button = (HighlightButton) findViewById(R.id.high_button);
		dark_image = (DarkImageView) findViewById(R.id.dark_image);
		dark_button = (DarkButton) findViewById(R.id.dark_button);
		linear_a = (DarkLinearLayout) findViewById(R.id.linear_a);
		linear_b = (DarkLinearLayout) findViewById(R.id.linear_b);
		frame_a = (DarkFrameLayout) findViewById(R.id.frame_a);
		frame_b = (DarkFrameLayout) findViewById(R.id.frame_b);
		relative_a = (DarkRelativeLayout) findViewById(R.id.relative_a);
		relative_b = (DarkRelativeLayout) findViewById(R.id.relative_b);
		
		high_image.setOnClickListener(this);
		high_button.setOnClickListener(this);
		dark_image.setOnClickListener(this);
		dark_button.setOnClickListener(this);
		
		linear_a.setOnClickListener(this);
		linear_b.setOnClickListener(this);
		frame_a.setOnClickListener(this);
		frame_b.setOnClickListener(this);
		relative_a.setOnClickListener(this);
		relative_b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.high_image:
			Toast.makeText(this, "高亮图片", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.high_button:
			Toast.makeText(this, "高亮按钮", Toast.LENGTH_SHORT).show();
			break;
		case R.id.dark_image:
			Toast.makeText(this, "深色图片", Toast.LENGTH_SHORT).show();
			break;
		case R.id.dark_button:
			Toast.makeText(this, "深色按钮", Toast.LENGTH_SHORT).show();
			break;
		case R.id.linear_a:
			Toast.makeText(this, "深色LinearLayou：单色背景", Toast.LENGTH_SHORT).show();
			break;
		case R.id.linear_b:
			Toast.makeText(this, "深色LinearLayou：图片背景", Toast.LENGTH_SHORT).show();
			break;
		case R.id.frame_a:
			Toast.makeText(this, "深色LinearLayou：单色背景", Toast.LENGTH_SHORT).show();
			break;
		case R.id.frame_b:
			Toast.makeText(this, "深色LinearLayou：图片背景", Toast.LENGTH_SHORT).show();
			break;
		case R.id.relative_a:
			Toast.makeText(this, "深色RelativeLayout：单色背景", Toast.LENGTH_SHORT).show();
			break;
		case R.id.relative_b:
			Toast.makeText(this, "深色RelativeLayout：图片背景", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

}
