package com.youlin.mp4;
import android.os.*;
import android.support.v7.app.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import okhttp3.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import android.content.*;
import java.util.regex.*;
import android.util.*;
import android.widget.AdapterView.*;
import android.view.*;


public class MovieActivity extends AppCompatActivity implements OnItemClickListener
{

	private ListView lv;
	private View footView;
	private Button btn;
	private String title;
	private List<News> list = new ArrayList<News>();;
	private int yeshu;
	private int nowyeshu = 1;
	private MyAdapter adapter;
	private String HTTPURL = "https://www.287ff.com/htm/mp4list7/";
	private String httpurl;
	private LinearLayout linearLayout;
	private LinearLayout linearLayout1;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					linearLayout.setVisibility(View.GONE);//ProgressBar提示隐藏
					adapter = new MyAdapter(list);
					lv.setAdapter(adapter);
					break;

				case 1:
					adapter.notifyDataSetChanged();
					linearLayout1.setVisibility(View.GONE);//ProgressBar隐藏
					btn.setVisibility(View.VISIBLE);//显示按钮
					break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movieactivity);
		getads();
		init();
		initData(0, HTTPURL);
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		Intent intent = new Intent(MovieActivity.this, PlayerActivity.class);
		intent.putExtra("ads", list.get(p3).getSummary());
		intent.putExtra("imgads",list.get(p3).getImageUrl());
		startActivity(intent);
	}

	private void init()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(title);
		lv = (ListView) findViewById(R.id.movieactivityListView);
		footView = getLayoutInflater().inflate(R.layout.footview, null);
		linearLayout = (LinearLayout) findViewById(R.id.movieactivityLinearLayout);
		linearLayout1 = (LinearLayout) footView.findViewById(R.id.footviewLinearLayout);
		btn = (Button) footView.findViewById(R.id.footviewButton);
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (yeshu >= nowyeshu)
					{
						btn.setVisibility(View.GONE);//隐藏按钮
						linearLayout1.setVisibility(View.VISIBLE);//ProgressBar显示
						nowyeshu ++;
						httpurl = HTTPURL + nowyeshu + ".htm";
						initData(1, httpurl);
					}else{
						Toast.makeText(MovieActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
						btn.setText("没有更多了");
					}
				}
			});
		lv.addFooterView(footView);
		lv.setOnItemClickListener(this);
	}

	private int getint(String string)
	{
		String regEx="[^0-9]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(string);  
		String number =m.replaceAll("").trim();
		return Integer.parseInt(number);
	}

	private void getads()
	{
		Intent intent = getIntent();
		HTTPURL = intent.getStringExtra("ads");
		title = intent.getStringExtra("title");
	}

	private void initData(final int i, String url)
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
						News news = null;
						String html = response.body().string();
						Document doc = Jsoup.parse(html);
						Elements div = doc.select("div.thumbs");
						Elements a = div.select("a.thumb-link");
						Elements intdiv = doc.select("div.pagination");
						String wei = intdiv.select("a:contains(尾页)").first().attr("href");
						yeshu = getint(wei);
						Log.d("int", yeshu + "");

						for (Element element : a)
						{
							String text = element.select("span.thumb-text").text();
							String imgads = element.select("img").attr("src");
							String ads = element.attr("href");
							news = new News(imgads, text, ads);
							list.add(news);
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (i == 0)
					{
						mHandler.obtainMessage(0).sendToTarget();
					}
					else if (i == 1)
					{
						mHandler.obtainMessage(1).sendToTarget();
					}
				}
			});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
      if (keyCode == KeyEvent.KEYCODE_BACK){
    	  MovieActivity.this.finish();    	  
    	  return true;  
      }
		
      return super.onKeyDown(keyCode, event);  
	}

}

