package com.qy.vo;


import java.io.Serializable;

import com.qy.common.Constrant;

public class JsonBody implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5743483561932951256L;
	private int code=Constrant.REQUEST_SUCCESS;
	private String msg="success";
	private Object obj;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
