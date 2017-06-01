package com.qy.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tools {
	private static Logger log = LoggerFactory.getLogger(Tools.class);

	public static SimpleDateFormat nyrsfm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat nyr = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat nyrsf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static ExecutorService cachedThreadPool = Executors
			.newCachedThreadPool();
	public static final NumberFormat CURRENCY_FORMAT = NumberFormat
			.getCurrencyInstance(Locale.CHINA);

	public static JSONObject formatStr2JSON(String infor) {
		return JSONObject.parseObject(infor);
	}

	public static void writeCookie(HttpServletResponse response, String key,
			String value, int maxAge) {
		Cookie cookie = new Cookie(key, value);
		// 设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);// 关闭浏览器就清除
		response.addCookie(cookie);
	}

	public static String readCookie(HttpServletRequest request, String key) {
		if (StringUtils.isEmpty(key))
			return null;
		Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
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
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 经过nginx转发后，x-forwarded-for 获取的ip可能有多个，他们是逗号分隔的。例如183.16.90.3,
		// 219.133.40.15
		if (StringUtils.isNotEmpty(ip) && ip.indexOf(",") >= 0) {
			String[] p = ip.split(",");
			if (p.length >= 0)
				ip = p[0];
		}
		return "0:0:0:0:0:0:0:1".equals(ip) || StringUtils.isEmpty(ip) ? Constrant.LOCALHOST_IP
				: ip;
	}

	public static String[] getLocationByIPIP(String ip) {

		String req_path = Constrant.IPIP_NET_REQ_PATH + ip;
		String res = HttpUtils.getInstance().doGetWithHeader(req_path,
				Constrant.IPIP_MAP, null);
		log.info("ipip_req_path=" + req_path + "     res=" + res);

		if (StringUtils.isEmpty(res))
			return null;
		String[] infor = new String[7];// 省、市、区、详细地址、经度、纬度、地区编号
		JSONObject jsonObject = JSONObject.parseObject(res);
		String ret = (String) jsonObject.get("ret");
		if ("ok".equals(ret)) {
			JSONArray data =  jsonObject.getJSONArray("data");
			String city = (String) data.get(2);
			String lat = (String) data.get(5);
			String lng = (String) data.get(6);
			String code = (String) data.get(9);
			infor = gaoDeParse(lng, lat);
			if (StringUtils.isEmpty(infor[1]))
				infor[0] = city;

			if (StringUtils.isEmpty(infor[6]))
				infor[6] = code;

			infor[4] = lng;
			infor[5] = lat;
		}
		return infor;
	}

	/**
	 * 高德解析经纬度
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public static String[] gaoDeParse(String lng, String lat) {
		String[] retinfor = new String[7];// 省、市、区、详细地址、经度、纬度、地区编号
		if ("4.9E-324".equals(lng) || "4.9E-324".equals(lat)) {
			return retinfor;
		}
		retinfor[4] = lng;
		retinfor[5] = lat;
		String url = String.format(Constrant.REVERSE_LOCATION_URL,
				Constrant.GAO_DE_WEB_KEY, lng + "," + lat);

		String infor = HttpUtils.getInstance().callHtmlRequestGet(url);
		JSONObject jsonObject = JSONObject.parseObject(infor);
		int res =jsonObject.getInteger("status");
		if (Constrant.NUMBER_INT_ONE != res)
			return retinfor;
		Object infocode = jsonObject.get("infocode");
		if (infocode instanceof String)
			retinfor[6] = (String) infocode;

		JSONObject regeocode = (JSONObject) jsonObject.get("regeocode");
		if (null == regeocode)
			return retinfor;
		Object address = regeocode.get("formatted_address");
		if (address instanceof String)
			retinfor[3] = (String) address;

		JSONObject addressComponent = (JSONObject) regeocode
				.get("addressComponent");

		if (null == addressComponent)
			return retinfor;

		String province = (String) addressComponent.get("province");
		Object city = addressComponent.get("city");
		Object district = addressComponent.get("district");

		retinfor[0] = province;
		retinfor[1] = (city instanceof String) ? (String) city : province;
		retinfor[2] = (district instanceof String) ? "" : (String) district;
		return retinfor;

	}
	/**
	 * 执行shell 脚本
	 * @param shellPath
	 * @param params
	 * @return
	 */
	public static int excShell(String shellPath, String... params) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; (null != params) && i < params.length; i++) {
			buffer.append(" " + params[i]);
		}
		int success = 0;
		BufferedReader bufferedReader = null;
		try {
			Process pid = null;
			String[] cmd = { "/bin/sh", "-c", shellPath + buffer.toString() }; // 给shell传递参数
			pid = Runtime.getRuntime().exec(cmd); // 执行Shell命令
			if (pid != null) {
				log.info("进程号：" + pid.toString());
				bufferedReader = new BufferedReader(new InputStreamReader(
						pid.getInputStream()), 1024);
				pid.waitFor();
			} else {
				log.info("没有pid\r\n");
			}
			String line = null;
			while (bufferedReader != null
					&& (line = bufferedReader.readLine()) != null) {
				buffer.append(line).append("\r\n");
			}
			log.info("stringBuffer:" + buffer.toString());
		} catch (Exception ioe) {
			log.error(ioe.getMessage(), ioe);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
					// 将Shell的执行情况输出到日志文件中
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			success = 1;
		}
		return success;

	}
}
