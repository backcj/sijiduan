package com.rd.callcarDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rd.callcar.Util.ExitApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StepTwo extends Activity {

	 private List<Map<String, Object>> mData;
	 private ListView orderList;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twostep_layout);
		ExitApplication.getInstance().addActivity(this);
		 mData = getData();
		 MyAdapter adapter = new MyAdapter( this );
		 orderList = (ListView) findViewById(R.id.listview);
		 orderList.setAdapter(adapter);
		 orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
			}
		});
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", "常工院");
		map.put("end", "万达");
		map.put("number", "1");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("start", "常工院");
		map.put("end", "新北");
		map.put("number", "2");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("start", "秋白楼");
		map.put("end", "院士楼");
		map.put("number", "4");
		list.add(map);

		return list;
	}

	public final class ViewHolder {
		public TextView start;
		public TextView end;
		public TextView number;
		public Button accept;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.order_list, null);
				holder.start = (TextView) convertView.findViewById(R.id.start);
				holder.end = (TextView) convertView.findViewById(R.id.end);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.accept = (Button) convertView.findViewById(R.id.accept);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.start.setText((String) mData.get(position).get("start"));
			holder.end.setText((String) mData.get(position).get("end"));
			holder.number.setText((String) mData.get(position).get("number"));
			holder.accept.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showInfo();
				}
			});

			return convertView;
		}

	}
	
	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo() {
		Toast.makeText(this, "订单接受成功", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(StepTwo.this, StepThree.class));
	}
	
}
