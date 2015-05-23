package com.rd.callcarDriver;

import java.util.List;

import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ExitApplication;
import com.rd.callcar.adapter.ComplantAdapter;
import com.rd.callcar.entity.CallHistory;
import com.rd.callcar.json.getJson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ComplantActivity extends Activity {

	private List<CallHistory> list;
	private Button back, button_say, button_complant;
	private ListView complantList;

	private ProgressDialog pro = null;

	private final int feedkbackSuccess = 0;
	final int fedkbackFail = 1;
	App app = null;
	ComplantAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complant_layout);
		ExitApplication.getInstance().addActivity(this);

		list = (List<CallHistory>) getIntent().getSerializableExtra("data");

		app = (App) getApplication();

		// 初始化加载对话框
		pro = new ProgressDialog(this);
		pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pro.setTitle("提示");
		pro.setIcon(android.R.drawable.ic_dialog_info);
		pro.setMessage("正在提交投诉，请稍候……");
		pro.setIndeterminate(false);
		pro.setCancelable(true);

		complantList = (ListView) findViewById(R.id.complantList);
		adapter = new ComplantAdapter(this, list);
		complantList.setAdapter(adapter);
		complantList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				adapter.CheckOnlyOne(arg2);
			}
		});

		back = (Button) findViewById(R.id.back);
		button_say = (Button) findViewById(R.id.button_say);
		button_complant = (Button) findViewById(R.id.button_complant);

		back.setOnClickListener(new OnClk());
		button_say.setOnClickListener(new OnClk());
		button_complant.setOnClickListener(new OnClk());
	}

	class OnClk implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				ComplantActivity.this.finish();
				break;
			case R.id.button_say:
				startActivity(new Intent(ComplantActivity.this,
						CompantEnterActivity.class));
				break;
			case R.id.button_complant:
				ShowDD();
				break;
			}
		}
	}

	private void ShowDD() {
		if (adapter.getSelect() == null) {
			app.ShowToast("请选择一个原因再进行投诉！");
			return;
		}

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ComplantActivity.this)
				.setTitle(R.string.Hint)
				.setMessage("您确定要进行投诉吗？若发现用户进行虚假投诉将作违规处理，请慎用此功能！")
				.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Complant();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		alertDialog.show();
	}

	private void Complant() {
		pro.show();
		new Thread(new Runnable() {

			public void run() {
				try {
					boolean isSub = getJson.isComplantSelect(app.getUSerid(),
							adapter.getSelect().getSeqid());
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
				app.ShowToast("投诉成功，我们将尽快处理您的投诉！");
				break;
			case fedkbackFail:
				app.ShowToast("投诉失败，请检测网络或者重新提交，感谢您的使用！");
				break;
			}
		}
	};
}
