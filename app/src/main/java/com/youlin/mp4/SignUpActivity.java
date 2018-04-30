package com.youlin.mp4;
import android.support.v7.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import com.qmuiteam.qmui.widget.dialog.*;
import com.qmuiteam.qmui.widget.*;
import android.util.*;

public class SignUpActivity extends AppCompatActivity
{
	private EditText ed_name;
	private EditText ed_password;
	private EditText ed_email;
	private Button btn_signup;
	private QMUITopBar topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		init();
	}

	private void init()
	{
		ed_name = (EditText) findViewById(R.id.signupEditTextname);
		ed_password = (EditText) findViewById(R.id.signupEditTextpassword);
		ed_email = (EditText) findViewById(R.id.signupEditTextemail);
		btn_signup = (Button) findViewById(R.id.signupButtonsignup);
		topbar = (QMUITopBar) findViewById(R.id.signupQMUITopBar);
		topbar.addLeftBackImageButton().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
				}
			});
		btn_signup.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Bmob.getServerTime(new QueryListener<Long>(){

							@Override
							public void done(Long p1, BmobException p2)
							{
								if (p2 == null)
								{
									Log.d("userdata",p1.toString());
									int time = p1.intValue();
									String name = ed_name.getText().toString();
									String password = ed_password.getText().toString();
									String email = ed_email.getText().toString();
									if (name != null && name.length() != 0 && password != null && password.length() != 0)
									{
										if (email != null && email.length() != 0 && email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"))
										{
											MyUser user = new MyUser();
											user.setUsername(name);
											user.setPassword(password);
											user.setEmail(email);
											user.setUsedata(time + UserData.timejs(1));
											user.signUp(new SaveListener<MyUser>(){

													@Override
													public void done(MyUser p1, BmobException p2)
													{
														if (p2 == null)
														{
															new QMUIDialog.MessageDialogBuilder(SignUpActivity.this)
																.setTitle("提示")
																.setMessage("我们已发送一封验证邮件到您的邮箱，请前往您的邮箱查看并点击邮箱里的链接进行帐号激活。")
																.addAction("确定", new QMUIDialogAction.ActionListener(){

																	@Override
																	public void onClick(QMUIDialog p1, int p2)
																	{
																		p1.dismiss();
																		finish();
																	}


																}).create().show();
														}
														else
														{
															Toast.makeText(SignUpActivity.this, "注册失败，请换一个帐号和邮箱尝试", Toast.LENGTH_SHORT).show();
														}
													}


												});
										}
										else
										{
											Toast.makeText(SignUpActivity.this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
										}
									}
									else
									{
										Toast.makeText(SignUpActivity.this, "请输入帐号和密码", Toast.LENGTH_SHORT).show();
									}
								}
							}


						});


				}


			});

	}

}
