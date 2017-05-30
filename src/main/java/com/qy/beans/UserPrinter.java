/**
 * 
 */
package com.qy.beans;

import java.util.Date;

/**
 * @author Kathy
 * 
 */
public class UserPrinter extends BaseData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;// '用户唯一标记',
	private String did;// '设备唯一标记',
	private Date ctime;// '创建时间',
	private Date utime;// '更新时间',

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

}
