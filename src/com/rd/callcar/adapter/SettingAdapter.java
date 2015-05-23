package com.rd.callcar.adapter;

import java.util.List;

import com.rd.callcarDriver.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter {
	private List<String> newslist;
	private Context context;

	public SettingAdapter(Context context) {
		this.context = context;
	}

	public SettingAdapter(Context context, List<String> newslist) {
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.setting_item, null);
			holder = new AreaHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);

			convertView.setTag(holder);
		} else {
			holder = (AreaHolder) convertView.getTag();
		}
		holder.text.setText(newslist.get(position));

		return convertView;
	}

	class AreaHolder {
		TextView text;
	}

}
