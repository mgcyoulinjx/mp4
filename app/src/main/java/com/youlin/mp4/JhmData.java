package com.youlin.mp4;
import cn.bmob.v3.*;

public class JhmData extends BmobObject
{
	private String jhm;
	private Integer date;


	public void setJhm(String jhm)
	{
		this.jhm = jhm;
	}

	public String getJhm()
	{
		return jhm;
	}

	public void setDate(Integer date)
	{
		this.date = date;
	}

	public Integer getDate()
	{
		return date;
	}}
