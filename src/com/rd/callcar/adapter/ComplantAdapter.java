package com.rd.callcar.adapter;

import java.util.List;

import com.rd.callcarDriver.R;
import com.rd.callcar.entity.CallHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class ComplantAdapter extends BaseAdapter {
	private List<CallHistory> newslist;
	private Context context;
	private CallHistory select;

	public ComplantAdapter(Context context, List<CallHistory> newslist) {
		this.context = context;
		this.newslist = newslist;

	}

	public int getCount() {
		return newslist.size();
	}

	public Object getItem(int arg0) {
		return newslist.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final AreaHolder holder = new AreaHolder();

		final CallHistory history = newslist.get(position);
		// if (convertView == null) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.complaint_item, null);
		// holder = new AreaHolder();
		holder.text = (TextView) convertView.findViewById(R.id.text);
		holder.radio = (RadioButton) convertView.findViewById(R.id.radio);

		convertView.setTag(holder);
		// } else {
		// holder = (AreaHolder) convertView.getTag();
		// }
		holder.text.setText(position + 1 + ". " + "历史叫车记录"
				+ history.getAddDate());

		holder.radio.setChecked(history.isSelected());

		holder.radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				CheckOnlyOne(position);
			}
		});

		return convertView;
	}

	public void CheckOnlyOne(int index) {
		for (int i = 0; i < newslist.size(); i++) {
			if (index != i) {
				newslist.get(i).setSelected(false);
			}
		}
		newslist.get(index).setSelected(true);
		select = newslist.get(index);
		this.notifyDataSetChanged();
	}

	public CallHistory getSelect() {
		return select;
	}

	class AreaHolder {
		TextView text;
		RadioButton radio;
	}

}
