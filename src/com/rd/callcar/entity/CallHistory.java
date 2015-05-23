package com.rd.callcar.entity;

import java.io.Serializable;

public class CallHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String seqid;

	private String addDate;

	private boolean isSelected = false;

	private int callType;

	public String getSeqid() {
		return seqid;
	}

	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getCallType() {
		return callType;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}
}
