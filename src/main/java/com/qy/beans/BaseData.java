package com.qy.beans;

/**
 * 
 */

import java.io.Serializable;

/**
 * @author timy
 * 
 */
public class BaseData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5895279553153339743L;
	/** 每页显示多少条记录 */
	private int pageSize;
	/** 开始记录数 */
	private int start;
	private String startTime;
	private String endTime;
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return start*pageSize;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
