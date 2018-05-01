package com.youlin.mp4;

import android.os.*;
import android.support.v7.app.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.widget.AdapterView.*;
import android.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import com.qmuiteam.qmui.widget.dialog.*;
import com.qmuiteam.qmui.widget.*;
import com.tencent.bugly.crashreport.*;

public class MainActivity extends AppCompatActivity implements OnItemClickListener
{
	private QMUITopBar mTopBar;
	private ListView lv;
	private boolean a;
	private QMUITipDialog tipDialog;
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
		mTopBar = (QMUITopBar) findViewById(R.id.QMUITopBar);
		mTopBar.setTitle(R.string.app_name);
		mTopBar.addRightImageButton(R.drawable.image_4, R.id.topbar_right_about_button)
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					initlogin();
				}
			});
		CrashReport.initCrashReport(getApplicationContext(), "76ea692997",false);
		Bmob.initialize(this, "e6b30c43cc144f8b27e0efcdc7e9a8a7");
		cxads();
		lv = (ListView) findViewById(R.id.mainListView);
		lv.setOnItemClickListener(this);
    }

	private void initlogin()
	{
		MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
		if (bmobUser != null)
		{
			Intent intent = new Intent(MainActivity.this, VipActivity.class);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(MainActivity.this, LogInActivity.class);
			startActivity(intent);
		}
	}

	public void cxads()
	{
		tipDialog = new QMUITipDialog.Builder(MainActivity.this)
			.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
			.setTipWord("正在加载数据")
			.create();
		tipDialog.show();
		BmobQuery<MyDataBmob> query = new BmobQuery<MyDataBmob>();
		query.getObject("38c46e8580", new QueryListener<MyDataBmob>(){

				@Override
				public void done(MyDataBmob p1, BmobException p2)
				{
					if (p2 == null)
					{
						MyData.ads = p1.getAds();
						tipDialog.dismiss();
						fetchUserInfo();
						lv.setAdapter(new MyAdapter());
					}
					else
					{
						Toast.makeText(MainActivity.this, "错误" + p2, Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void fetchUserInfo()
	{
		MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
		if (bmobUser != null)
		{
			MyUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
					@Override
					public void done(String s, BmobException e)
					{
						if (e == null)
						{
							a = true;
							UserData.init();
							Toast.makeText(MainActivity.this, "数据更新完成", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(MainActivity.this, "更新数据错误" + e.toString(), Toast.LENGTH_SHORT).show();
						}
					}
				});
		}

    }

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		if (a)
		{
			if (UserData.emailVerified)
			{
				if (UserData.isVip)
				{
					String url = MyData.ads + ads[p3];
					Intent intent = new Intent(MainActivity.this, MovieActivity.class);
					intent.putExtra("ads", url);
					intent.putExtra("title", name[p3]);
					startActivity(intent);
				}
				else
				{
					new QMUIDialog.MessageDialogBuilder(MainActivity.this)
						.setTitle("提示")
						.setMessage("你的会员已到期")
						.addAction("确定", new QMUIDialogAction.ActionListener(){

							@Override
							public void onClick(QMUIDialog p1, int p2)
							{
								p1.dismiss();
							}


						}).create().show();
				}
			}
			else
			{
				new QMUIDialog.MessageDialogBuilder(MainActivity.this)
					.setTitle("提示")
					.setMessage("你的邮箱未激活，请去邮箱激活")
					.addAction("确定", new QMUIDialogAction.ActionListener(){

						@Override
						public void onClick(QMUIDialog p1, int p2)
						{
							p1.dismiss();
						}


					}).create().show();
			}
		}
		else
		{
			new QMUIDialog.MessageDialogBuilder(MainActivity.this)
				.setTitle("提示")
				.setMessage("请稍候，用户信息还未加载完成")
				.addAction("确定", new QMUIDialogAction.ActionListener(){

					@Override
					public void onClick(QMUIDialog p1, int p2)
					{
						p1.dismiss();
					}
				}).create().show();
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
