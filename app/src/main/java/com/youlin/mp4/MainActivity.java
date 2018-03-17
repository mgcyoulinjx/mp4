package com.youlin.mp4;

import android.os.*;
import android.support.v7.app.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.widget.AdapterView.*;
import android.*;
import com.google.android.gms.ads.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;

public class MainActivity extends AppCompatActivity implements OnItemClickListener
{
	private ListView lv;
	private ProgressBar pro;
	private InterstitialAd mInterstitialAd;
	private String[] ads = {
		"/htm/mp4list7/",
		"/htm/mp4list1/",
		"/htm/mp4list2/",
		"/htm/mp4list3/",
		"/htm/mp4list5/",
		"/htm/mp4list6/",
		"/htm/vodlist3/"
	};

	private String[] name = {
		"自拍偷拍",
		"中文无码",
		"中文有码",
		"欧美无码",
		"日本无码",
		"日本有码",
		"欧美视频"
	};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Bmob.initialize(this, "e6b30c43cc144f8b27e0efcdc7e9a8a7");
		cxads();
		lv = (ListView) findViewById(R.id.mainListView);
		pro = (ProgressBar) findViewById(R.id.mainProgressBar);
		lv.setOnItemClickListener(this);
		MobileAds.initialize(this, "ca-app-pub-2688831136654780~5865337257");
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-2688831136654780/6601728060");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

	public void cxads()
	{
		BmobQuery<MyDataBmob> query = new BmobQuery<MyDataBmob>();
		query.getObject("38c46e8580", new QueryListener<MyDataBmob>(){

				@Override
				public void done(MyDataBmob p1, BmobException p2)
				{
					if (p2 == null)
					{
						MyData.ads = p1.getAds();
						pro.setVisibility(View.GONE);
						lv.setAdapter(new MyAdapter());
					}else{
						Toast.makeText(MainActivity.this,"错误" + p2,Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		String url = MyData.ads + ads[p3];
		Intent intent = new Intent(MainActivity.this, MovieActivity.class);
		intent.putExtra("ads", url);
		intent.putExtra("title", name[p3]);
		startActivity(intent);
		if (mInterstitialAd.isLoaded())
		{
            mInterstitialAd.show();
        }
	}


	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return name.length;
		}

		@Override
		public Object getItem(int p1)
		{
			// TODO: Implement this method
			return p1;
		}

		@Override
		public long getItemId(int p1)
		{
			// TODO: Implement this method
			return p1;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3)
		{
			View view = null;
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			view = inflater.inflate(R.layout.main_item, null); 
			TextView tv = (TextView) view.findViewById(R.id.main_itemTextView);
			tv.setText(name[p1]);
			return view;
		}


	}


	private boolean mIsExit;
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

        if (keyCode == KeyEvent.KEYCODE_BACK)
		{
            if (mIsExit)
			{
                this.finish();

            }
			else
			{
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
						@Override
						public void run()
						{
							mIsExit = false;
						}
					}, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
