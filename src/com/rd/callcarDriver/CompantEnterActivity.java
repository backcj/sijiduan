package com.rd.callcarDriver;

import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ExitApplication;
import com.rd.callcar.entity.FeedMsg;
import com.rd.callcar.json.getJson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CompantEnterActivity extends Activity {

	private EditText feedmessage, contact;
	private Button back, clear, submit_feed;

	private ProgressDialog pro = null;

	private final int feedkbackSuccess = 0;
	final int fedkbackFail = 1;
	App app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_layout);
		ExitApplication.getInstance().addActivity(this);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		app = (App) getApplication();

		// 初始化加载对话框
		pro = new ProgressDialog(this);
		pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pro.setTitle("提示");
		pro.setIcon(android.R.drawable.ic_dialog_info);
		pro.setMessage("正在提交反馈，请稍候……");
		pro.setIndeterminate(false);
		pro.setCancelable(true);

		feedmessage = (EditText) findViewById(R.id.feedmessage);
		contact = (EditText) findViewById(R.id.contact);

		back = (Button) findViewById(R.id.back);
		clear = (Button) findViewById(R.id.clear);
		submit_feed = (Button) findViewById(R.id.submit_feed);

		back.setOnClickListener(new OnClk());
		clear.setOnClickListener(new OnClk());
		submit_feed.setOnClickListener(new OnClk());
	}

	class OnClk implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.clear:
				if (feedmessage.isFocused())
					feedmessage.setText("");
				if (contact.isFocused())
					contact.setText("");
				break;
			case R.id.submit_feed:
				SubMit();
				break;
			case R.id.back:
				CompantEnterActivity.this.finish();
				break;
			}
		}
	}

	private void Clear() {
		feedmessage.setText("");
		contact.setText("");
	}

	private void SubMit() {
		String msg = feedmessage.getText().toString().trim();
		String add = contact.getText().toString().trim();

		if (msg.equals("")) {
			ShowToast("请输入反馈内容！");
			return;
		}

		if (msg.length() > 500) {
			ShowToast("输入内容过多，请删减部分内容！");
			return;
		}

		if (add.length() > 200) {
			ShowToast("联系方式输入过长，请修改长度！");
			return;
		}

		final FeedMsg fedmsg = new FeedMsg();
		fedmsg.setUserid(app.getUSerid());
		fedmsg.setContent(msg);
		fedmsg.setAddress(add);

		pro.show();
		new Thread(new Runnable() {

			public void run() {
				try {
					boolean isSub = getJson.SubFeedBack(fedmsg);
					if (isSub) {
						Message message = new Message();
						message.what = feedkbackSuccess;
						handler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = fedkbackFail;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					Message message = new Message();
					message.what = fedkbackFail;
					handler.sendMessage(message);
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pro.dismiss();
			switch (msg.what) {
			case feedkbackSuccess:
				ShowToast("反馈成功，感谢您的反馈！");
				Clear();
				break;
			case fedkbackFail:
				ShowToast("反馈失败，请检测网络或者重新提交，感谢您的使用！");
				break;
			}
		}
	};

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
