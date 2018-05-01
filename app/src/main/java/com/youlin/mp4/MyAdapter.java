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
import android.content.*;
import com.bumptech.glide.*;

public class MyAdapter extends BaseAdapter
{
	private List<News> list;
	private Context context;
	private LayoutInflater inflater;

	public MyAdapter(Context context, List<News> list)
	{
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
		if (null == convertView)
		{
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
        }

		ImageView img = (ImageView)convertView.findViewById(R.id.iv);
		TextView tv = (TextView) convertView.findViewById(R.id.title);
		tv.setText(list.get(position).getTitle());
		Glide
			.with(context)
			.load(list.get(position).getImageUrl())
			.into(img);

		return convertView;
	}
}
