package com.qy.beans;

import java.util.Date;

/**
 * 
 * 
 */
public class UserInfor extends BaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uid;// '用户唯一标记',
	private String lng;// '用户经度',
	private String lat;// '用户纬度',
	private String ua;
	private int count;// '用户打印次数',
	private String code;// '用户注册时行政区编码',
	private String province;// '用户注册时省',
	private String city;// '用户注册时市',
	private String district;// '用户注册时区',
	private String addr;// '用户注册时详细地址',
	private String ip;// '用户注册时上网地址',
	private Date ctime;// '创建时间',
	private Date utime;// '更新时间',

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

}
