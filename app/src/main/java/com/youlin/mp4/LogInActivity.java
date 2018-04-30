package com.youlin.mp4;
import android.support.v7.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import com.qmuiteam.qmui.widget.*;
import android.content.*;

public class LogInActivity extends AppCompatActivity
{

	private EditText name;
	private EditText password;
	private Button btn_login;
	private Button btn_signup;
	private QMUITopBar topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		init();
	}

	private void init()
	{
		topbar = (QMUITopBar) findViewById(R.id.loginQMUITopBar);
		name = (EditText) findViewById(R.id.loginactivityEditTextname);
		password = (EditText) findViewById(R.id.loginactivityEditTextpassword);
		btn_login = (Button) findViewById(R.id.loginactivityButtonlogin);
		btn_signup = (Button) findViewById(R.id.loginactivityButtonsignup);
		topbar.addLeftBackImageButton().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
				}

			
		});
		btn_login.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String nameString = name.getText().toString();
					String passwordString = password.getText().toString();
					if (nameString != null && nameString.length() != 0 && passwordString != null && passwordString.length() != 0)
					{
						MyUser user = new MyUser();
						user.setUsername(nameString);
						user.setPassword(passwordString);
						user.login(new SaveListener<MyUser>(){

								@Override
								public void done(MyUser p1, BmobException p2)
								{
									if (p2 == null)
									{
										UserData.init();
										Toast.makeText(LogInActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
										finish();
									}else{
										Toast.makeText(LogInActivity.this,"登录失败，请检查是否输入错误或帐号是否已激活",Toast.LENGTH_SHORT).show();
									}
								}
							});
					}
				}
			});
			
		btn_signup.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
					startActivity(intent);
				}
			});
	}
}
