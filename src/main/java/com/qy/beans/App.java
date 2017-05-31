package com.qy.beans;

import java.util.Date;

public class App extends BaseData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8528328240309386057L;
	private String aid;// '设备mac地址',
	private String mac;// '设备mac地址',
	private String ver;// 打印设备pc版本
	private String os;// 打印设备pc操作系统
	private String lng;// '设备经度',
	private String lat;// '设备纬度',
	private String type;// '设备打印机类型',
	private int st;// '在线状态，0在线，1表示离线',
	private String code;// '行政区编码',
	private String province;// '省',
	private String city;// '市',
	private String district;// '区',
	private String addr;// '打印机详细地址',
	private String ip;// '打印机上网地址',
	private Date ctime;// '创建时间',
	private Date utime;// '更新时间',

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSt() {
		return st;
	}

	public void setSt(int st) {
		this.st = st;
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

}
