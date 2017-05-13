package com.qy.common;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
//import net.sf.json.JSONObject;
import net.sf.json.JSONObject;

public class Tools {
	public static SimpleDateFormat nyrsfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat nyr = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat nyrsf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static ExecutorService executorService = Executors.newCachedThreadPool();
	public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.CHINA);
	public static JSONObject formatStr2JSON(String str) {
		
		return JSONObject.fromObject(str);
	}
	 
	
	public static void writeCookie( HttpServletResponse response,String key,String value,int maxAge){
		Cookie cookie = new Cookie(key,value);
		//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);//关闭浏览器就清除
		response.addCookie(cookie);
	}
	
	public static String readCookie(HttpServletRequest request,String key){
		if(StringUtils.isEmpty(key))
			return null;
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		if(null != cookies){
			for(Cookie cookie : cookies){
				if(key.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static String getMd5(String code) {
		String encode = DigestUtils.md5Hex(code);
		return encode;
	}
	
	public static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");  
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
        }  
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (StringUtils.isEmpty(ip)  || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (StringUtils.isEmpty(ip)  || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (StringUtils.isEmpty(ip)  || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
      //经过nginx转发后，x-forwarded-for 获取的ip可能有多个，他们是逗号分隔的。例如183.16.90.3, 219.133.40.15
		if(StringUtils.isNotEmpty(ip) && ip.indexOf(",")>=0){
			String[] p = ip.split(",");
			if(p.length>=0)
				ip=p[0];
		}
		return  "0:0:0:0:0:0:0:1".equals(ip)|| StringUtils.isEmpty(ip)  ? Constrant.LOCALHOST_IP : ip; 
	}
	
}
