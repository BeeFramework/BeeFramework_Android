package com.BeeFramework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class DarkButton extends Button {  
    
    /**保存当前容器原来设置的图片*/  
    private Drawable drawableSave;  
    /**高亮图片*/  
    private Drawable drawableGrap;  
      
    public DarkButton(Context context) {  
        super(context);  
        setListener();  
    }  
      
    public DarkButton(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        setListener();  
    }  
  
    public DarkButton(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        setListener();  
    }  
    
    @Override  
    public void setBackground(Drawable drawable) {  
        this.drawableSave = drawable;  
        setSuperImageDrawable(drawable);  
    }  
  
    @Override  
    public void setBackgroundDrawable(Drawable drawable) {  
  
        this.drawableSave = drawable;  
        setSuperImageDrawable(drawable);  
    }  
      
    @Override  
    public void setBackgroundResource(int resId) {  
  
        super.setBackgroundResource(resId);  
        this.drawableSave = this.getBackground();  
    }  
    
    /**调用父类设置图片方法*/  
    private void setSuperImageDrawable(Drawable drawable) {  
       // super.setBackground(drawable);  
        super.setBackgroundDrawable(drawable);
    }  
      
    /**设置正常状态*/  
    public void setNormalState() {  
        setSuperImageDrawable(drawableSave);  
    }  
    /**设置点击状态*/  
    public void setClickedState() {  
        setDrawableGrap();//生成点击后图片  
        setSuperImageDrawable(drawableGrap);  
    }  
      
    /** 
     * 绑定事件 
     */  
    private void setListener() {  
        //鼠标事件  
        this.setOnTouchListener(new OnTouchListener() {  
              
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
  
                switch (event.getAction()) {  
                    case MotionEvent.ACTION_DOWN:   //鼠标按下  
                        setClickedState();  
                        break;  
                    case MotionEvent.ACTION_UP:     //鼠标松开  
                        setNormalState();  
                        break;  
                    case MotionEvent.ACTION_CANCEL: //取消  
                        setNormalState();  
                        break;  
                    default :  
                        break;  
                }  
                  
                return false;  
            }  
        });  
    }  
      
    private void setDrawableGrap() {  
       
		int w = drawableSave.getIntrinsicWidth(); // 取 drawable 的长宽
		int h = drawableSave.getIntrinsicWidth();

		Bitmap.Config config = drawableSave.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);// 建立对应 bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawableSave.setBounds(0, 0, w, h);
		drawableSave.draw(canvas); // 把 drawable 内容画到画布中

		int array[] = new int[w * h];
		int n = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int color = bitmap.getPixel(j, i);
				int a = Color.alpha(color);
				color = Color.argb(a, 0, 0, 0);
				array[n] = color;
				n++;
			}
		}
		Bitmap newBitmap = Bitmap.createBitmap(array, w, h,
				Bitmap.Config.ARGB_8888);

		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();

		if (newBitmap != null) {
			paint.setAlpha(25); // 设置透明度
			cv.drawBitmap(newBitmap, 0, 0, paint);
		}

		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		drawableGrap = new BitmapDrawable(getResources(), newb);

	}

}  