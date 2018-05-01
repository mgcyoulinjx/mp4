package com.youlin.mp4;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import okhttp3.*;

import android.support.v7.widget.Toolbar;
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;
import android.util.*;
import android.view.View.*;
import android.webkit.*;
import com.tencent.smtt.sdk.*;
import android.app.*;
import android.net.*;

public class PlayerActivity extends AppCompatActivity
{
	private CollapsingToolbarLayoutState state;
	private ButtonBarLayout playButton;
	private Toolbar toolbar;
	private AppBarLayout app_bar;
	private FloatingActionButton fab;
	private ImageView img;
	private ImageView img1;
	private CollapsingToolbarLayout toolbarlayout;
	private com.tencent.smtt.sdk.WebView wv;
	private Button btn;

	private String url;
	private String jieshaotxt;
	private String playurl;
	private String title;
	private String jieshaoimgads;
	private String titleimgads;


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					if (title != null && title.length() != 0)
					{
						toolbarlayout.setTitle(title);
					}
					MyAsyncTask myAsyncTask = new MyAsyncTask(fab, img);
					MyAsyncTask myAsyncTask1 = new MyAsyncTask(fab, img1);
					myAsyncTask.execute(titleimgads);
					myAsyncTask1.execute(jieshaoimgads);
					if (jieshaotxt != null && jieshaotxt.length() != 0)
					{
						wv.loadData(jieshaotxt, "text/html; charset=UTF-8", null);//webview显示

					}
					break;
			}
		}
	};

	private enum CollapsingToolbarLayoutState
	{
		EXPANDED,
		COLLAPSED,
		INTERNEDIATE
		}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		getads();
		init();
		initadata();
	}

	private void initadata()
	{
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		call.enqueue(new Callback(){

				@Override
				public void onFailure(Call p1, IOException p2)
				{
// TODO: Implement this method
				}

				@Override
				public void onResponse(Call p1, Response response)
				{
					try
					{
						String html = response.body().string();
						Document doc = Jsoup.parse(html);
						Elements titlediv = doc.select("div.container");
						title = titlediv.select("h3").text();
						
						Elements titleimg = titlediv.select("div.col-md-9");
						for (Element element : titleimg)
						{
							String titletext = element.html();
							String text = titletext.replace(" ", "");
							
							titleimgads = text.substring(text.indexOf("\"") + 1, text.indexOf("title") - 1);
						}

						Elements jieshao = doc.select("div.info");
						jieshaotxt = jieshao.first().html();
						
						jieshaoimgads = doc.select("div.bigpic").select("img").attr("src");
						
						Elements script= doc.select("SCRIPT");
						for (Element element : script)
						{
							String scripttext = element.html();
							String text = scripttext.replace(" ", "");
							if (text.contains("varGvodUrls="))
							{
								playurl = text.substring(text.indexOf("\"") + 1, text.lastIndexOf("\""));
								
							}
						}
					}
					catch (IOException e)
					{}
					mHandler.obtainMessage(0).sendToTarget();
				}
			});
	}

	private void init()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		playButton = (ButtonBarLayout) findViewById(R.id.playButton);
		app_bar = (AppBarLayout)findViewById(R.id.app_bar);
		fab = (FloatingActionButton) findViewById(R.id.fab);
		img = (ImageView) findViewById(R.id.imageview);
		img1 = (ImageView) findViewById(R.id.content_scrollingImageView);
		toolbarlayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		wv = (com.tencent.smtt.sdk.WebView) findViewById(R.id.content_scrollingWebView);
		btn = (Button) findViewById(R.id.conbtn);

		setSupportActionBar(toolbar);
		app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
				@Override
				public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
				{

					if (verticalOffset == 0)
					{
						if (state != CollapsingToolbarLayoutState.EXPANDED)
						{
							state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//设置title为EXPANDED
						}
					}
					else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange())
					{
						if (state != CollapsingToolbarLayoutState.COLLAPSED)
						{
//设置title不显示
							playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
							state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
						}
					}
					else
					{
						if (state != CollapsingToolbarLayoutState.INTERNEDIATE)
						{
							if (state == CollapsingToolbarLayoutState.COLLAPSED)
							{
								playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
							}
//设置title为INTERNEDIATE
							state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
						}
					}
				}
			});
		fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					if (TbsVideo.canUseTbsPlayer(PlayerActivity.this))
					{
						TbsVideo.openVideo(PlayerActivity.this, playurl);
					}
				}
			});

		playButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (TbsVideo.canUseTbsPlayer(PlayerActivity.this))
					{
						TbsVideo.openVideo(PlayerActivity.this, playurl);
					}
				}
			});
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (playurl != null && playurl.length() != 0)
					{
						DownloadManager.Request request = new DownloadManager.Request(Uri.parse(playurl));
						request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
						request.setTitle(title);
						request.setDescription("蓝影影视");
						File saveFile = new File(Environment.getExternalStorageDirectory(),"蓝影影视下载目录/" + title + ".mp4");
						request.setDestinationUri(Uri.fromFile(saveFile));
						DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
						manager.enqueue(request);
						Snackbar.make(toolbarlayout, "已加入下载列表", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					}
					else
					{
						Snackbar.make(toolbarlayout, "稍等，正在加载视频地址...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
					}
				}
			});
	}

	private void getads()
	{
		Intent intent = getIntent();
		url = MyData.ads + intent.getStringExtra("ads");
	}


	class MyAsyncTask extends AsyncTask<String,Void,Bitmap>
	{

		private View view;
		private ImageView imageView;
		private String imageUrl;

		//构造
		public MyAsyncTask(View view, ImageView imageView)
		{
			super();
			this.view = view;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String...params)
		{
			imageUrl = params[0];
			Bitmap bitmap = downloadImage();
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			// TODO: Implement this method
			super.onPostExecute(bitmap);
			if (bitmap != null)
			{
				imageView.setImageBitmap(bitmap);
				
			}
			else
			{
				Snackbar.make(view, "图片加载失败", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			}
		}
		//下载图片
		private Bitmap downloadImage()
		{
			HttpURLConnection con = null;
			Bitmap bitmap = null;
			try
			{
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(10 * 1000);
				bitmap = BitmapFactory.decodeStream(con.getInputStream());
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (con != null)
				{
					con.disconnect();
				}
			}
			return bitmap;
		}


	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			PlayerActivity.this.finish();    	  
			return true;  
		}

		return super.onKeyDown(keyCode, event);  
	}
}
