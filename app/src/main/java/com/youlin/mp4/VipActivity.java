package com.youlin.mp4;
import android.support.v7.app.*;
import android.os.*;
import com.qmuiteam.qmui.widget.*;
import android.view.View.*;
import android.view.*;
import com.qmuiteam.qmui.widget.grouplist.*;
import android.content.*;
import cn.bmob.v3.*;
import com.qmuiteam.qmui.widget.dialog.*;
import android.text.*;
import android.widget.*;
import android.net.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import java.util.*;

public class VipActivity extends AppCompatActivity
{

	private QMUITopBar mTopBar;
	private QMUIGroupListView mGroupListView;
	private String emailtext;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip);
		initTopBar();
		init();
	}

	private void init()
	{
		mGroupListView = (QMUIGroupListView) findViewById(R.id.groupListView);
		initEmailText();
		initGroupListView();
	}



	private void initEmailText()
	{
		if (UserData.emailVerified.booleanValue())
		{
			emailtext = "(已激活)";
		}
		else
		{
			emailtext = "(未激活)";
		}
	}

	private void initTopBar()
	{
		mTopBar = (QMUITopBar) findViewById(R.id.vipQMUITopBar);
		mTopBar.addLeftBackImageButton().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
				}
			});
	}

	/****************
	 *
	 * 发起添加群流程。群号：蓝影影视(777971012) 的 key 为： wHDorEkZlTUa5Ko_R5BnMlOQ_-aNbmKW
	 * 调用 joinQQGroup(wHDorEkZlTUa5Ko_R5BnMlOQ_-aNbmKW) 即可发起手Q客户端申请加群 蓝影影视(777971012)
	 *
	 * @param key 由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup(String key)
	{
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try
		{
			startActivity(intent);
			return true;
		}
		catch (Exception e)
		{
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}


	private void initGroupListView()
	{
		QMUICommonListItemView itemName = mGroupListView.createItemView("用户名");
		itemName.setDetailText(UserData.useName);
		QMUICommonListItemView itemEmail = mGroupListView.createItemView("邮箱");
		itemEmail.setDetailText(UserData.useEmail + emailtext);
		final QMUICommonListItemView itemVIP = mGroupListView.createItemView("会员");
		itemVIP.setDetailText(UserData.vipText);




		QMUICommonListItemView jh = mGroupListView.createItemView("输入会员码");
		jh.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
		QMUICommonListItemView ads = mGroupListView.createItemView("购买卡密");
		ads.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


		QMUICommonListItemView kf = mGroupListView.createItemView("联系客服QQ");
		kf.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
		QMUICommonListItemView qqqun = mGroupListView.createItemView("加入售后群");
		qqqun.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

		QMUIGroupListView.newSection(this)
			.setTitle("帐号信息")
			.setDescription("(注:邮箱如果未激活将无法继续使用，即使你已有vip！请前往邮箱点击链接地址激活即可)")
			.addItemView(itemName, null)
			.addItemView(itemEmail, null)
			.addItemView(itemVIP, null)
			.addTo(mGroupListView);


		QMUIGroupListView.newSection(this)
			.setTitle("帐号操作")
			.addItemView(jh, new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(VipActivity.this);
					builder.setTitle("请输入激活码")
						.setPlaceholder("在此输入您的激活码")
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.addAction("取消", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(QMUIDialog dialog, int index)
							{
								dialog.dismiss();
							}
						})
						.addAction("确定", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(final QMUIDialog dialog, int index)
							{
								CharSequence text = builder.getEditText().getText();
								if (text != null && text.length() > 0)
								{
									//查询激活码
									BmobQuery<JhmData> query = new BmobQuery<JhmData>();
									query.addWhereEqualTo("jhm", text);
									query.setLimit(1);
									query.findObjects(new FindListener<JhmData>(){

											@Override
											public void done(List<JhmData> jhmsj, BmobException jhme)
											{
												if (jhme == null && jhmsj != null)
												{
													final int time = jhmsj.get(0).getDate().intValue();
													final String jhmid = jhmsj.get(0).getObjectId();
													//获取网络时间
													Bmob.getServerTime(new QueryListener<Long>(){

															@Override
															public void done(Long p1, BmobException timee)
															{
																if (timee == null)
																{
																	//更新用户使用时间
																	MyUser myUser = new MyUser();
																	if(UserData.useVipTime < p1.intValue()){
																		myUser.setUsedata(p1.intValue() + UserData.timejs(time));
																	}else{
																		myUser.setUsedata(UserData.useVipTime + UserData.timejs(time));
																	}
																	MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
																	myUser.update(bmobUser.getObjectId(), new UpdateListener(){

																			@Override
																			public void done(BmobException e)
																			{
																				if (e == null)
																				{
																					//删除激活码
																					JhmData jhmData = new JhmData();
																					jhmData.setObjectId(jhmid);
																					jhmData.delete(new UpdateListener(){

																							@Override
																							public void done(BmobException de)
																							{
																								if (de == null)
																								{
																									dialog.dismiss();
																									MyUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
																											@Override
																											public void done(String s, BmobException e)
																											{
																												if (e == null)
																												{
																													UserData.init();
																													Toast.makeText(VipActivity.this, "数据更新完成", Toast.LENGTH_SHORT).show();
																													itemVIP.setDetailText(UserData.vipText);
																												}
																												else
																												{
																													Toast.makeText(VipActivity.this, "更新数据错误" + e.toString(), Toast.LENGTH_SHORT).show();
																												}
																											}
																										});
																								}
																							}


																						});
																				}
																			}


																		});
																}
															}


														});

												}
											}


										});

								}
								else
								{
									Toast.makeText(VipActivity.this, "请输入激活码", Toast.LENGTH_SHORT).show();
								}
							}
						}).create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
				}


			})
			.addItemView(ads, new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.2faka.com/merchant/E78754B579D7")));
				}

				
			})
			.addTo(mGroupListView);


		QMUIGroupListView.newSection(VipActivity.this)
			.setTitle("联系方式")
			.addItemView(kf, new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try
					{
						Toast.makeText(VipActivity.this, "跳转添加qq", Toast.LENGTH_SHORT).show();
						//第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
						String url = "mqqwpa://im/chat?chat_type=wpa&uin=1253270652";//uin是发送过去的qq号码
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					}
					catch (Exception e)
					{
						e.printStackTrace();
						Toast.makeText(VipActivity.this, "请检查是否安装最新版QQ", Toast.LENGTH_SHORT).show();
					}
				}


			})
			.addItemView(qqqun, new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					boolean i = joinQQGroup("wHDorEkZlTUa5Ko_R5BnMlOQ_-aNbmKW");
					if (i)
					{
						Toast.makeText(VipActivity.this, "正在调起QQ", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(VipActivity.this, "请检查是否安装最新版QQ", Toast.LENGTH_SHORT).show();
					}
				}


			})
			.addTo(mGroupListView);



	}


}
