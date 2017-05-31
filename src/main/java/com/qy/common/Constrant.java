package com.qy.common;

import java.util.HashMap;
import java.util.Map;


public class Constrant {
	public static String LOCALHOST_IP="127.0.0.1";
	public static String REQUEST_CMD="c";
	public static String DEVICE_ON_LINE="0";
	public static String DEVICE_OFF_LINE="1";
	public static int REQUEST_SUCCESS=1;
	public static int REQUEST_FAIL=0;
	public static String PDF_SUB_PATH ="/sub/";
	public static String PDF_PATH ="/pdf/";
	public static String REPORT_DEVICE="rpt";//设备上报
	public static String REPORT_APP_STATUS="ras";//上报app的状态
	public static String QRCODE_SCAN="scan";//扫描打印机二位嘛
	public static String GET_TASK="get";//获取打印任务
	public static String INSTALL_APP="init";//app装机
	public static String REPORT_TASK_STATUS="sta";//打印任务状态上报
	public static String LIST_DEVICE="lst";//设备列表
	public static String GET_DEVICE_STATUS="dst";//单台设备状态
	public static String REPORT_USER="rpu";//用户上报
	public static String UA="User-Agent";
	public static String IPIP_NET_REQ_PATH="http://ipapi.ipip.net/find?addr=";//ip地址定位第三方参数
	public static String IPIP_NET_TOKEN="778a17dff25ac37c836659ff28749d371f0108aa"; 
	public static Map IPIP_MAP=new HashMap<String, String>();
	public static final String GAO_DE_WEB_KEY ="3e9646d4cb1ce2ecb06677d74c3a691b";//web api 肖舒怀负责
	public static final String REVERSE_LOCATION_URL = "http://restapi.amap.com/v3/geocode/regeo?key=%s&location=%s";//逆地址解析
	public static int NUMBER_INT_ONE =1;
	static{
		IPIP_MAP.put("Token", IPIP_NET_TOKEN);
	}
}
