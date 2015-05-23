package com.rd.callcar.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.preference.PreferenceManager;

import com.rd.callcar.entity.CallHistory;
import com.rd.callcar.entity.FeedMsg;
import com.rd.callcar.entity.PointInfo;
import com.rd.callcar.entity.UpdataInfo;

public class getJson {

	public static String getAddressByLocal(double longitude, double latitude) {
		String back = "";
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		try {
			String content = getUrlContent(
					"http://api.map.baidu.com/geocoder?location="
							+ df.format(latitude)
							+ ","
							+ df.format(longitude)
							+ "&output=json&key=E3041FEDFA4A24627A4B76539E07658B0FE44A5D",
					5000);

			try {
				System.out.println(content);
				JSONObject obj = new JSONObject(content);

				if (obj != null) {
					String state = obj.getString("status");
					if (state.equals("OK")) {
						String address = new JSONObject(obj.getString("result"))
								.getString("addressComponent");
						JSONObject add = new JSONObject(address);
						String stress = add.getString("street");
						String stressNo = add.getString("street_number");
						back = stress + stressNo;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		}

		return back;
	}

	public static String getAddress(double lang, double lant, Activity act) {
		String back = "";
		try {
			String content = getUrlContent(
					String.format(
							"http://api.map.baidu.com/geocoder?location=%1$s,%2$s&output=json&key=E3041FEDFA4A24627A4B76539E07658B0FE44A5D",
							lang, lant), 5000);
			try {
				JSONObject obj = new JSONObject(content);

				if (obj.getString("status").equals("OK")) {
					JSONObject resu = new JSONObject(obj.getString("result"));
					JSONObject add = new JSONObject(
							resu.getString("addressComponent"));

					back = add.getString("street")
							+ add.getString("street_number");

					String city = add.getString("city");
					System.out.println("city:"+city);

					SaveBegin(act, lang, lant);
					SaveCity(act, city);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		}
		return back;
	}

	public static boolean Login(String userid, String pwd) {
		boolean back = false;
		try {
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?login=11&userid=%1$s&pwd=%2$s",
							userid, pwd), 5000);

			back = Boolean.parseBoolean(content);
		} catch (Exception e) {

		}
		return back;
	}

	// 注册
	public static int RegisterMeth(String userid, String pwd, String name) {
		int back = 0;
		try {
			@SuppressWarnings("deprecation")
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?register=11&userid=%1$s&pwd=%2$s&name=%3$s",
							userid, pwd, URLEncoder.encode(name)), 5000);

			back = Integer.parseInt(content);
		} catch (Exception e) {

		}
		return back;
	}

	public static int SendInfo(PointInfo info) {
		int back = 0;
		try {
			@SuppressWarnings({ "deprecation" })
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?send=11&userid=%1$s&city=%2$s&lang=%3$s&lant=%4$s&type=%5$s&destnation=%6$s",
							info.getUserid(),
							URLEncoder.encode(info.getCity()), info.getLang(),
							info.getLant(), URLEncoder.encode(info.getType()),
							URLEncoder.encode(info.getDestnation())), 5000);

			back = Integer.parseInt(content);

		} catch (Exception e) {

		}
		return back;
	}

	// 取消打车
	public static boolean cancelCall(String uSerid, String save) {
		boolean back = false;
		try {
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?cancel=11&userid=%1$s&seqid=%2$s",
							uSerid, save), 5000);

			back = Boolean.parseBoolean(content);
		} catch (Exception e) {

		}
		return back;
	}

	private static void SaveBegin(Activity act, double lang, double lant) {
		PreferenceManager
				.getDefaultSharedPreferences(act.getApplicationContext())
				.edit().putString("lang", String.valueOf(lang))
				.putString("lant", String.valueOf(lant)).commit();
	}

	private static void SaveCity(Activity act, String city) {
		PreferenceManager
				.getDefaultSharedPreferences(act.getApplicationContext())
				.edit().putString("city", city).commit();
	}

	private static String getUrlContent(String url, int timeOut) {
		String str = "";
		try {

			URL aURL = null;
			try {
				aURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			URLConnection conn = aURL.openConnection();
			conn.connect();
			StringBuilder b = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));

			String line;
			while ((line = reader.readLine()) != null) {
				b.append(line);
			}
			reader.close();
			str = b.toString();
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return str;
	}

	// 取应用版本信息
	public static UpdataInfo getNewsVersion() {
		UpdataInfo appInfo = null;
		try {
			String content = getUrlContent(
					"http://125.211.221.147/Default.aspx?version=newver", 5000);

			content = content.replace("[", "").replace("]", "");
			JSONObject obj = new JSONObject(content.trim());

			if (obj != null && obj.length() > 0) {
				appInfo = new UpdataInfo();
				appInfo.setVersion(obj.getInt("versionId"));
				appInfo.setDescription(obj.getString("content"));
				appInfo.setUrl(obj.getString("url"));
			}
		} catch (Exception e) {
		}
		return appInfo;
	}

	// 修改密码
	public static String ChangePwd(String userid, String oldPwd, String newPwd) {
		String back = null;
		try {
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?changePwd=11&userid=%1$s&oldPwd=%2$s&newPwd=%3$s",
							userid, oldPwd, newPwd), 5000);

			content = content.trim();

			JSONObject obj = new JSONObject(content);

			if (obj != null && obj.length() > 0) {
				back = obj.getString("result");
			}

		} catch (Exception e) {
		}

		return back;
	}

	// 取历史叫车记录
	public static List<CallHistory> getRecordList(String uSerid, int page) {
		List<CallHistory> list = null;
		try {
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?history=11&userid=%1$s&page=%2$d",
							uSerid, page), 5000);

			JSONArray array = new JSONArray(content);
			if (array != null && array.length() > 0) {
				list = new ArrayList<CallHistory>();
				for (int i = 0; i < array.length(); i++) {
					CallHistory history = new CallHistory();
					history.setSeqid(array.getJSONObject(i).getString("seqid"));

					history.setAddDate(array.getJSONObject(i).getString(
							"addDate"));
					history.setCallType(array.getJSONObject(i).getInt(
							"callType"));

					list.add(history);
				}
			}
		} catch (Exception e) {
		}

		return list;
	}

	// 提交输入投诉内容
	public static boolean SubFeedBack(FeedMsg fedMsg) {
		boolean back = false;
		try {
			@SuppressWarnings("deprecation")
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?compliant=11&userid=%1$s&contact=%2$s&complantContent=%3$s",
							fedMsg.getUserid(),
							URLEncoder.encode(fedMsg.getAddress()),
							URLEncoder.encode(fedMsg.getContent())), 5000);
			content = content.trim();

			back = Boolean.parseBoolean(content);

		} catch (Exception e) {

		}
		return back;
	}

	// 提交投诉
	public static boolean isComplantSelect(String uSerid, String seqid) {
		boolean back = false;
		try {
			@SuppressWarnings("deprecation")
			String content = getUrlContent(
					String.format(
							"http://125.211.221.147/Default.aspx?compliantselect=11&userid=%1$s&seqid=%2$s",
							uSerid, URLEncoder.encode(seqid)), 5000);
			content = content.trim();

			back = Boolean.parseBoolean(content);

		} catch (Exception e) {

		}
		return back;
	}

}
