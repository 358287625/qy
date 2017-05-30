/**
 * 
 */
package com.qy.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kathy
 * 
 */
public class BaseController {
	public String getResHeader(HttpServletRequest req,String headerName){
		return req.getHeader(headerName);
	}
	/**
	 * 获取request请求参数
	 * 
	 * @param req
	 * @param paramName
	 * @return
	 */
	public String getParams(HttpServletRequest req, String paramName) {
		Object obj = req.getParameter(paramName);
		return obj == null ? "" : (String) obj;
	}
}
