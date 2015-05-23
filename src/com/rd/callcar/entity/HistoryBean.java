package com.rd.callcar.entity;

public class HistoryBean {
	public static final String ID = "_id";
	public static final String TIME = "time";
	public static final String NAME = "name";

	private String id;
	private String name;
	private int time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
