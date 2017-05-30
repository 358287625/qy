package com.qy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.qy.common.Constrant;
import com.qy.vo.JsonBody;

@Controller
@RequestMapping(value = "/usr")
public class UserController extends BaseController {
	private static  Logger log = LoggerFactory.getLogger(DeviceController.class);  
	@RequestMapping(value="pro")
	@ResponseBody
	public JsonBody process(HttpServletRequest req,HttpServletResponse res){
		String cmd=getParams(req, Constrant.REQUEST_CMD);
		log.info(req.getQueryString()+" -- "+cmd);
		
		JsonBody jsonBody = new JsonBody();
		if(StringUtils.isEmpty(cmd)){
			jsonBody.setMsg("request parameter error");
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			return jsonBody;
		}
		//用户上报或者已经上报再次进来
		if(Constrant.REPORT_USER.equals(cmd)){
			return jsonBody;
		}
		return jsonBody;
	}

}



