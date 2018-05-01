package com.youlin.mp4;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import java.text.*;
import java.util.*;
import android.util.*;

public class UserData
{
	public static String useName = null;
	public static String useEmail = null;
	public static Integer useVipTime = 0;
	public static Boolean emailVerified = false;
	public static boolean isVip = false;
	public static String vipText = null;

	public static void init()
	{
		MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
		if (bmobUser != null)
		{
			useName = (String) BmobUser.getObjectByKey("username");
			useEmail = (String) BmobUser.getObjectByKey("email");
			useVipTime = (Integer) BmobUser.getObjectByKey("usedata");
			final Long time = useVipTime.longValue();
			emailVerified = (Boolean) BmobUser.getObjectByKey("emailVerified");
			
			Bmob.getServerTime(new QueryListener<Long>(){

					@Override
					public void done(Long p1, BmobException p2)
					{
						if (p2 == null)
						{
							
							if (time > p1)
							{
								isVip = true;
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String times = simpleDateFormat.format(new Date(time * 1000L));
								vipText = times + "到期";
							}
							else if (time < p1)
							{
								vipText = "非会员";
							}
						}else{
							System.out.println("出错了");
						}
					}
				});
		}
	}

	public static int timejs(int s)
	{
		int i =86400 * s;
		return i;
	}
}
