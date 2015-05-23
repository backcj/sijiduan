package com.rd.callcar.adapter;

import java.io.Serializable;
import java.util.List;

import com.rd.callcarDriver.R;
import com.rd.callcar.entity.CallHistory;
import com.rd.callcarDriver.ComplantActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private List<CallHistory> newslist;
	private Context context;

	public HistoryAdapter(Context context, List<CallHistory> newslist) {
		this.context = context;
		this.newslist = newslist;
	}

	public int getCount() {
		return newslist.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		AreaHolder holder = null;

		CallHistory history = newslist.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.history_item, null);
			holder = new AreaHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.complant = (Button) convertView.findViewById(R.id.complant);

			convertView.setTag(holder);
		} else {
			holder = (AreaHolder) convertView.getTag();
		}
		holder.text.setText(position + 1 + ". " + "历史叫车记录"
				+ history.getAddDate());

		switch (history.getCallType()) {
		case 0:
			holder.complant.setText("等待叫车");
			holder.complant.setEnabled(false);
			break;
		case 1:
			holder.complant.setText("取消叫车");
			holder.complant.setEnabled(false);
			break;
		case 2:
			holder.complant.setText("叫车超时");
			holder.complant.setEnabled(false);
			break;
		case 3:
			break;
		}

		holder.complant.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) newslist);

				context.startActivity(new Intent(context,
						ComplantActivity.class).putExtras(bundle));
			}
		});

		return convertView;
	}

	class AreaHolder {
		TextView text;
		Button complant;
	}

}
