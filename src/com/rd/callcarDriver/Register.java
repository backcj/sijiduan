package com.rd.callcarDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ConnectServer;
import com.rd.callcar.Util.ExitApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	private Button back, ok, cancel;

	private EditText input_userid, input_pwd, input_name, input_repwd, input_tel, input_lp;

	private ProgressDialog mpDialog;

	final int LOGINSUCCESS_MSG = 0;
	final int LOGINFAIL_MSG = 1;

	App app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		ExitApplication.getInstance().addActivity(this);

		app = (App) getApplication();

		// 初始化加载对话框
		mpDialog = new ProgressDialog(this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mpDialog.setTitle(R.string.loading_data);
		mpDialog.setIcon(android.R.drawable.ic_dialog_info);
		mpDialog.setMessage(getString(R.string.registing));
		mpDialog.setIndeterminate(false);
		mpDialog.setCancelable(true);

		back = (Button) findViewById(R.id.back);
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);

		input_userid = (EditText) findViewById(R.id.input_userid);
		input_pwd = (EditText) findViewById(R.id.input_pwd);
		input_repwd = (EditText) findViewById(R.id.input_repwd);
		input_name = (EditText) findViewById(R.id.input_name);
		input_tel = (EditText) findViewById(R.id.input_tel);
		input_lp = (EditText) findViewById(R.id.input_lp);

		back.setOnClickListener(new OnClick());
		ok.setOnClickListener(new OnClick());
		cancel.setOnClickListener(new OnClick());
	}

	class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
			case R.id.cancel:
				Register.this.finish();
				break;
			case R.id.ok:
				try {
					RegisterMeth();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private void RegisterMeth() throws Exception {
		checkValidate();
	}

	private void checkValidate() throws Exception {
		final String userid = input_userid.getText().toString().trim();
		final String pwd = input_pwd.getText().toString().trim();
		final String repwd = input_repwd.getText().toString().trim();
		final String name = input_name.getText().toString().trim();
		final String tel = input_tel.getText().toString().trim();
		final String lp = input_lp.getText().toString().trim();
		
		if (userid == "") {
			ShowToast("请输入用户名！");
			return;
		}
		if (pwd == "") {
			ShowToast("请输入密码！");
			return;
		}

		if (repwd == "") {
			ShowToast("请输入确认密码！");
			return;
		}

		if (!pwd.equals(repwd)) {
			ShowToast("密码与确认密码不一致！");
			return;
		}
		
		if (tel == "") {
			ShowToast("请输入电话！");
			return;
		}
		
		if (lp == "") {
			ShowToast("请输入车牌号！");
			return;
		}

		if (pwd.length() < 6) {
			app.ShowToast(R.string.noEnough);
			return;
		}

		if (pwd.length() > 18) {
			app.ShowToast(R.string.noMore);
			return;
		}
		
		String jsonstr = ConnectServer.connect("/regist/regist", URLEncoder.encode(userid, "gbk"), pwd, URLEncoder.encode(name, "gbk"), tel, lp);
		JSONObject jsonObject = new JSONObject(jsonstr);
		String result = jsonObject.getString("result");
		if(result != null && result.equals("注册成功！")){
			ShowToast("注册成功！");
			Intent inten = new Intent();
			inten.putExtra("userid", input_userid.getText().toString()
					.trim());
			setResult(1, inten);
			Register.this.finish();
		}else {
			ShowToast(result);
		} 
		
	}

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void ShowToast(int msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
