package com.rd.callcar.data;

import java.util.ArrayList;
import java.util.List;

public class staticData {
	private static String[] chooseText = { "不拼车", "拼车，1人", "拼车，2人", "拼车，3人" };

	public static String numByChoose(String ss) {
		for (int i = 0; i < chooseText.length; i++) {
			if (ss.equals(chooseText[0])) {
				return "最多4人";
			} else if (chooseText[i].equals(ss)) {
				return String.valueOf(i) + "人";
			}
		}

		return String.valueOf("0");
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < chooseText.length; i++) {
			list.add(chooseText[i]);
		}
		return list;
	}

	private static String[] Setting = { "登陆", "注册", "切换账户", "退出" };

	public static List<String> getSettingList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < Setting.length; i++) {
			list.add(Setting[i]);
		}
		return list;
	}
}
