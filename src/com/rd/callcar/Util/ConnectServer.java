package com.rd.callcar.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;

public class ConnectServer {
	private static String urlString;
	
	static {
		//加载配置文件
		Properties properties = new Properties();
		InputStream inputStream = ConnectServer.class.getResourceAsStream("config.properties");
		try {
			properties.load(inputStream);
			urlString = properties.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param actionName 路由名   例如:/test
	 * @param params 参数  一般为String
	 * @return String 返回值   json字符串
	 */
	public static <T> String connect(String actionName, T... params){
		try {
			StringBuilder sb = new StringBuilder();
			boolean isfirst = true;
			for(T t : params){
				//拼接参数  /v0-v1-v2
				if(isfirst){
					sb.append(actionName + "/" + t);
					sb.append("-");
					isfirst = false;
				}else{
					sb.append(t + "-");
				}
			}
			//去除最后一个"-"
			sb.delete(sb.length()-1, sb.length());
			
			URL url = new URL(urlString+sb.toString());
			System.out.println("url>>>" + urlString + sb.toString());
			try {
				//获得连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String msg = "";
				while((line = in.readLine())!= null){
					msg += line;
				}
				in.close();
				conn.disconnect();
				return msg;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 将JSON字符串变为json数组
	 * @param jsonStr
	 * @return
	 */
	public static JSONArray toJSON(String jsonStr){
		System.out.println(jsonStr);
		
		JSONArray jsonArray = null;
		try {
			if(jsonStr.startsWith("[")){
				jsonArray = new JSONArray(jsonStr);
			}else{
				jsonArray = new JSONArray("["+jsonStr+"]");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
}
