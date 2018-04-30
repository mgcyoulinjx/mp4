package com.youlin.mp4;
import cn.bmob.v3.*;

public class MyUser extends BmobUser
{
	private Integer usedata;
	

	public void setUsedata(Integer usedata)
	{
		this.usedata = usedata;
	}

	public Integer getUsedata()
	{
		return usedata;
	}
}
