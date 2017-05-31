package com.qy.beans;



/**
 * 
 * @author Administrator
 * 
 */

public class Device extends BaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String did;//'设备唯一标记',
	private String pid;//打印机id
	private String aid;//app,
	private String appSta;//app在线状态'在线状态，0在线，1表示离线',,appSta
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getAppSta() {
		return appSta;
	}
	public void setAppSta(String appSta) {
		this.appSta = appSta;
	}
	
	
}
