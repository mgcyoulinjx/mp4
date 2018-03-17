package com.youlin.mp4;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MyAdapter extends BaseAdapter
{
	private List<News> list;
	private ListView listview;
	private LruCache<String, BitmapDrawable> mImageCache;

	public MyAdapter(List<News> list)
	{
		super();
		this.list = list;

		int maxCache = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxCache / 8;
		mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
			@Override
			protected int sizeOf(String key, BitmapDrawable value)
			{
				return value.getBitmap().getByteCount();
			}
		};

	}

	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return list.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (listview == null)
		{
			listview = (ListView) parent;
		}
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.movie_item, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		News news = list.get(position);
		holder.title.setText(news.getTitle());
		holder.iv.setTag(news.getImageUrl());
		// 如果本地已有缓存，就从本地读取，否则从网络请求数据
		if (mImageCache.get(news.getImageUrl()) != null)
		{
			holder.iv.setImageDrawable(mImageCache.get(news.getImageUrl()));
		}
		else
		{
			ImageTask it = new ImageTask();
			it.execute(news.getImageUrl());
		}
		return convertView;
	}
	class ViewHolder
	{
		ImageView iv;
		TextView title;
	}
	class ImageTask extends AsyncTask<String, Void, BitmapDrawable>
	{

		private String imageUrl;

		@Override
		protected BitmapDrawable doInBackground(String... params)
		{
			imageUrl = params[0];
			Bitmap bitmap = downloadImage();
			BitmapDrawable db = new BitmapDrawable(listview.getResources(),
												   bitmap);
			// 如果本地还没缓存该图片，就缓存
			if (mImageCache.get(imageUrl) == null)
			{
				mImageCache.put(imageUrl, db);
			}
			return db;
		}

		@Override
		protected void onPostExecute(BitmapDrawable result)
		{
			// 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
			ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
			if (iv != null && result != null)
			{
				iv.setImageDrawable(result);
			}
		}

		/**
		 * 根据url从网络上下载图片
		 * 
		 * @return
		 */
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
}
