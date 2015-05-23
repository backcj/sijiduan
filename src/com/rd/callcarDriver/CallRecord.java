package com.rd.callcarDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rd.callcarDriver.R;
import com.rd.callcar.Util.ExitApplication;
import com.rd.callcar.adapter.HistoryAdapter;
import com.rd.callcar.entity.CallHistory;
import com.rd.callcar.json.getJson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class CallRecord extends Activity implements OnScrollListener {

	final int LOGINSUCCESS_MSG = 0;
	final int LOGINFAIL_MSG = 1;
	public final int getMoreSuccess = 2;
	public final int getMoreFail = 3;
	App app = null;

	private Button back;
	private ListView historyList;

	private List<CallHistory> list = new ArrayList<CallHistory>();
	int page = 1;
	private int visibleLastIndex = 0;
	private boolean isLoad = true;
	HistoryAdapter adapter;
	private View footer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_layout);
		ExitApplication.getInstance().addActivity(this);

		app = (App) getApplication();

		back = (Button) findViewById(R.id.back);
		historyList = (ListView) findViewById(R.id.historyList);
		footer = getLayoutInflater().inflate(R.layout.listfooter, null);
		historyList.addFooterView(footer);
		adapter = new HistoryAdapter(CallRecord.this, list);
		historyList.setAdapter(adapter);
		historyList.setOnScrollListener(this);
		historyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) list);

				startActivity(new Intent(CallRecord.this,
						ComplantActivity.class).putExtras(bundle));
			}
		});

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CallRecord.this.finish();
			}
		});

		LoadHistory();
	}

	private void LoadHistory() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<CallHistory> list = getJson.getRecordList(
							app.getUSerid(), page);
					if (list != null && list.size() > 0) {
						Message message = new Message();
						message.what = LOGINSUCCESS_MSG;
						message.obj = list;
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
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGINSUCCESS_MSG:
				list.addAll((List<CallHistory>) msg.obj);
				if (historyList.getFooterViewsCount() > 0)
					historyList.removeFooterView(footer);
				adapter.notifyDataSetChanged();
				break;
			case LOGINFAIL_MSG:
				TextView tt = (TextView) footer
						.findViewById(R.id.footer_loading);
				tt.setText(R.string.historyFail);
				break;
			case getMoreFail:
				isLoad = true;
				TextView tt1 = (TextView) footer
						.findViewById(R.id.footer_loading);
				tt1.setText(R.string.noMorehistory);
				break;
			case getMoreSuccess:
				isLoad = true;
				list.addAll((List<CallHistory>) msg.obj);
				if (historyList.getFooterViewsCount() > 0)
					historyList.removeFooterView(footer);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex - 1 && isLoad) {
			isLoad = false;
			LoadMore();
		}
	}

	private void LoadMore() {
		page++;
		TextView tt = (TextView) footer.findViewById(R.id.footer_loading);
		tt.setText(R.string.waiting);
		historyList.addFooterView(footer);
		new Thread(new Runnable() {

			public void run() {
				try {
					List<CallHistory> listData = getJson.getRecordList(
							app.getUSerid(), page);
					if (listData != null && listData.size() > 0) {
						Message msg = new Message();
						msg.what = getMoreSuccess;
						msg.obj = listData;
						mhandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = getMoreFail;
						mhandler.sendMessage(msg);
					}
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = getMoreFail;
					mhandler.sendMessage(msg);
				}
			}
		}).start();
	}
}
