/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aibasis.parent.ui.entrance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aibasis.parent.ui.base.BaseActivity;
import com.aibasis.parent.network.api.account.AccountAPI;
import com.aibasis.parent.network.api.entity.account.RegisterResult;
import com.aibasis.parent.network.exception.APIException;
import com.aibasis.parent.network.http.RequestListener;
import com.aibasis.parent.utils.SharePreferenceUtil;
import com.aibasis.parent.DemoApplication;
import com.aibasis.parent.R;
import org.json.JSONException;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;

	//http api
	AccountAPI accountAPI;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);

		accountAPI = new AccountAPI(this);
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setMessage(getResources().getString(R.string.Is_the_registered));
			pd.show();

			new Thread(new Runnable() {
				public void run() {
//					try {
//						// 调用sdk注册方法
//						EMChatManager.getInstance().createAccountOnServer(username, pwd);
//						runOnUiThread(new Runnable() {
//							public void run() {
//								if (!RegisterActivity.this.isFinishing())
//									pd.dismiss();
//								// 保存用户名
//								DemoApplication.getInstance().setUserName(username);
//								Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), 0).show();
//								finish();
//							}
//						});
//					} catch (final EaseMobException e) {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								if (!RegisterActivity.this.isFinishing())
//									pd.dismiss();
//								int errorCode=e.getErrorCode();
//								if(errorCode==EMError.NONETWORK_ERROR){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.USER_ALREADY_EXISTS){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.UNAUTHORIZED){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.ILLEGAL_USER_NAME){
//								    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
//								}else{
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
//								}
//							}
//						});
//					}
					accountAPI.register(username, pwd, new RequestListener() {
						@Override
						public void onComplete(String result) {
							if(!RegisterActivity.this.isFinishing())
								pd.dismiss();
							try {
								RegisterResult registerResult = RegisterResult.parse(result);
								if(RegisterResult.SUCCESS.equals(registerResult.getResult())) {
									DemoApplication.getInstance().setParentId(registerResult.getParentId());
									DemoApplication.getInstance().setEaseId(registerResult.getEaseId());
									DemoApplication.getInstance().setEasePassword(registerResult.getEasePassword());

									SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(RegisterActivity.this);
									sharePreferenceUtil.setParentId(registerResult.getParentId());
								} else if(RegisterResult.USERNAME_EXISTS.equals(registerResult.getResult())) {
									Toast.makeText(RegisterActivity.this, getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
							finish();
						}

						@Override
						public void onAPIException(APIException exception) {
							Log.e("jijun", exception.toString());
						}
					});

				}
			}).start();
		}
	}

	public void back(View view) {
		finish();
	}

}
