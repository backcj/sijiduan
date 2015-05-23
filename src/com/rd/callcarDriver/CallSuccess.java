package com.rd.callcarDriver;

import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ExitApplication;
import com.rd.callcar.json.getJson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CallSuccess extends Activity {

	private Button coutinue, cancel, back;

	private ProgressDialog mpDialog;

	final int LOGINSUCCESS_MSG = 0;
	final int LOGINFAIL_MSG = 1;

	App app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success_layout);
		ExitApplication.getInstance().addActivity(this);

		app = (App) this.getApplication();

		// 初始化加载对话框
		mpDialog = new ProgressDialog(this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mpDialog.setTitle(R.string.loading_data);
		mpDialog.setIcon(android.R.drawable.ic_dialog_info);
		mpDialog.setMessage(getString(R.string.canceling));
		mpDialog.setIndeterminate(false);
		mpDialog.setCancelable(true);

		coutinue = (Button) findViewById(R.id.coutinue);
		back = (Button) findViewById(R.id.back);

		cancel = (Button) findViewById(R.id.cancel);

		coutinue.setOnClickListener(new OnClk());
		back.setOnClickListener(new OnClk());
		cancel.setOnClickListener(new OnClk());
	}

	class OnClk implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.coutinue:
			case R.id.back:
				CallSuccess.this.finish();
				break;
			case R.id.cancel:
				Cancel();
				break;
			}
		}
	}

	private void Cancel() {
		mpDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					boolean isCancel = getJson.cancelCall(app.getUSerid(),
							getSave("backId"));
					if (isCancel) {
						Message message = new Message();
						message.what = LOGINSUCCESS_MSG;
						mhandler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = LOGINFAIL_MSG;
						mhandler.sendMessage(message);
					}
				} catch (Exception e) {
					Message message = new Message();
					message.what = LOGINFAIL_MSG;
					mhandler.sendMessage(message);
				}
			}
		}).start();
	}

	// 线程处理
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mpDialog.dismiss();
			switch (msg.what) {
			case LOGINSUCCESS_MSG:
				ShowToast("取消打车成功！");
				startActivity(new Intent(CallSuccess.this, StepOne.class));
				break;
			case LOGINFAIL_MSG:
				ShowToast("取消打车失败！");
				break;
			}
		}
	};

	private String getSave(String key) {
		return PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString(key, "");
	}

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
