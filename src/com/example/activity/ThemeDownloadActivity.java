package com.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.BeeFramework.example.R;
import com.BeeFramework.theme.ResourcesFactory;
import com.BeeFramework.theme.ThemeManager;
import com.BeeFramework.view.DarkImageView;

public class ThemeDownloadActivity extends Activity {

	private Button          btn;
	private Dialog          mDialog;
	
	private ProgressBar     progressBar;
	private TextView        cancel;
	
	private int             progress;
	private boolean         cancelDownload = false;
    private FrameLayout     nav_bar;
    private DarkImageView   topview_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

        nav_bar = (FrameLayout)findViewById(R.id.nav_bar);

        Drawable drawable = ResourcesFactory.getDrawable(getResources(), R.drawable.nav_background);

        if (null != drawable)
        {
            nav_bar.setBackgroundDrawable(drawable);
        }

        topview_back = (DarkImageView) findViewById(R.id.topview_back);
        topview_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
	}
	
	private void showDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.download_dialog, null);

		mDialog = new Dialog(this, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		progressBar = (ProgressBar) view.findViewById(R.id.download_progress);
		cancel = (TextView) view.findViewById(R.id.download_cancel);
		mDialog.show();
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				cancelDownload = true;
			}
		});
		
		initThread("http://www.bee-framework.com/download/theme.zip");
		
	}
	
	int currentPackageSize = 0;
	public void initThread(final String zip_url) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				
				try {

					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
						String sdpath = Environment.getExternalStorageDirectory()+ "/insthub/ThemeDownload";
                        File downloadFile = new File(sdpath);
                        if (!downloadFile.exists())
                        {
                            downloadFile.mkdirs();
                        }

						URL url = new URL(zip_url);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.connect();
						int length = conn.getContentLength();
						InputStream is = conn.getInputStream();

						File file = new File(sdpath);
						if (!file.exists()) {
							file.mkdir();
						}
						
						File zipFile = new File(sdpath, "theme.zip");
						FileOutputStream fos = new FileOutputStream(zipFile);
						int count = 0;
						byte buf[] = new byte[1024];
						do {
							int numread = is.read(buf);
							count += numread;
							progress = (int) (((float) count / length) * 100);
							handler.sendEmptyMessage(1);
							if (numread <= 0)
                            {
								handler.sendEmptyMessage(2);
								break;
							}

							fos.write(buf, 0, numread);
						} while (!cancelDownload);
						fos.close();
						is.close();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mDialog.dismiss();
			}
		}.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what == 1)
            {
				progressBar.setProgress(progress);
			}
            else if(msg.what == 2)
            {
				try
                {
					unZipFiles(new File(Environment.getExternalStorageDirectory()+ "/insthub/ThemeDownload/theme.zip"),Environment.getExternalStorageDirectory()+ "/insthub/ThemeDownload/");
                    ThemeManager.getInstance().setThemeRootPath(Environment.getExternalStorageDirectory()+ "/insthub/ThemeDownload/");
                    ThemeDownloadActivity.this.ThemeChange();
				}
                catch(Exception e)
                {
                    e.printStackTrace();
                }
			}
		}
	};

    public void ThemeChange()
    {
        Drawable drawable = ResourcesFactory.getDrawable(getResources(), R.drawable.nav_background);

        if (null != drawable)
        {
            nav_bar.setBackgroundDrawable(drawable);
        }
    }
	
	public void unZipFiles(File zipFile, String descDir) throws IOException
    {
		File pathFile = new File(descDir);
		if (!pathFile.exists())
        {
			pathFile.mkdirs();
		}

		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();)
        {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists())
            {
				file.mkdirs();
			}

			if (new File(outPath).isDirectory())
            {
				continue;
			}
			System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}

		Toast.makeText(this, "success", 0).show();
	}
	
	
}
