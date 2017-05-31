/**
 * 
 */
package com.qy.common;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.qy.beans.Doc;
import com.qy.beans.UserInfor;

/**
 * @author Kathy
 *
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String res = "{\"ret\":\"ok\",\"data\":[\"本机地址\",\"本机地址\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]}";
		String[] infor = new String[7];// 省、市、区、详细地址、经度、纬度、地区编号
		JSONObject jsonObject = JSONObject.fromObject(res);
		String ret = (String) jsonObject.get("ret");
		if ("ok".equals(ret)) {
			net.sf.json.JSONArray data = (net.sf.json.JSONArray) jsonObject
					.get("data");
			String city = (String) data.get(2);
			String lat = (String) data.get(5);
			String lng = (String) data.get(6);
			String code = (String) data.get(9);
//			infor = gaoDeParse(lng, lat);
			if (StringUtils.isEmpty(infor[1]))
				infor[0] = city;

			if (StringUtils.isEmpty(infor[6]))
				infor[6] = code;

			infor[4] = lng;
			infor[5] = lat;
		}
	}

}
