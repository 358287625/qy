package com.qy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.qy.beans.App;
import com.qy.beans.Device;
import com.qy.beans.Doc;
import com.qy.common.Constrant;
import com.qy.common.Tools;
import com.qy.service.DeviceService;
import com.qy.vo.JsonBody;

@Controller
@RequestMapping(value="/dev")
public class DeviceController extends BaseController {
	private static  Logger log = LoggerFactory.getLogger(DeviceController.class);  
	@Autowired
	private DeviceService deviceService;
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
		//打印任务状态上报
		if(Constrant.REPORT_TASK_STATUS.equals(cmd)){
			String taskId =getParams(req, "tid");
			String did =getParams(req, "did");
			String status =getParams(req, "st");
			String aid = getParams(req, "aid");//设置在线状态
			
			if(StringUtils.isEmpty(aid) ||StringUtils.isEmpty(taskId) 
			   || StringUtils.isEmpty(did) || StringUtils.isEmpty(status)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			
			Doc doc = new Doc();
			doc.setDid(did);
			doc.setSt(Integer.valueOf(status));
			doc.setDocid(taskId);
			doc.setAid(aid);
			
			deviceService.updateTask(doc);
			return jsonBody;
		}
		//扫描打印机二位码
		if(Constrant.QRCODE_SCAN.equals(cmd)){
			String pid =getParams(req, "pid");//打印path上面的编码
			String uid = getParams(req, "uid");//用户id，第一期先不管
			
			if(StringUtils.isEmpty(pid)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			List list =deviceService.listByPid(pid, "");
			if(list == null || list.size()<=0){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("printer not exist");
				return jsonBody;	
			}
			
			jsonBody.setObj(list);
			return jsonBody;
		}
		//上报app的在线状态
		if(Constrant.REPORT_APP_STATUS.equals(cmd)){
			String aid = getParams(req, "aid");//设置在线状态
			
			if(StringUtils.isEmpty(aid)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			deviceService.reportAppStatus(aid);
			return jsonBody;
		}
		
		//获取打印任务
		if(Constrant.GET_TASK.equals(cmd)){
			String taskId =getParams(req, "tid");
			String did =getParams(req, "did");
			String aid = getParams(req, "aid");//设置在线状态
			
			if(StringUtils.isEmpty(aid) ||StringUtils.isEmpty(taskId) || StringUtils.isEmpty(did)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			
			Doc doc = new Doc();
			doc.setDid(did);
			doc.setDocid(taskId);
			doc.setAid(aid);
			List list =deviceService.getTasks(doc);
			jsonBody.setObj(list);
			return jsonBody;
		}
		//安装app上报
		if(Constrant.INSTALL_APP.equals(cmd)){
			String mac =getParams(req, "mac");//打印设备pc mac
			String os =getParams(req, "os");//打印设备pc操作系统类型
			String ver =getParams(req, "ver");//打印设备pc 版本
			String ip=Tools.getRequestIp(req);
			
			if(StringUtils.isEmpty(mac) ||StringUtils.isEmpty(os) 
				|| StringUtils.isEmpty(ver)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			App app = new App();
			app.setMac(mac);
			app.setVer(ver);
			app.setOs(os);
			app.setIp(ip);
			deviceService.installApp(app);
			jsonBody.setObj(app);
			return jsonBody;
		}
		//app发现打印机上报
		if(Constrant.REPORT_DEVICE.equals(cmd)){
			String pid =getParams(req, "pid");//打印机id
			String aid =getParams(req, "aid");//打印机aid
			String tag =getParams(req, "tag");//打印机tag
			if(StringUtils.isEmpty(aid) ||StringUtils.isEmpty(tag) || StringUtils.isEmpty(pid)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			
			Device device = new Device();
			device.setPid(pid);
			device.setTag(tag);
			device.setAid(aid);
			Object data = deviceService.add(device);
			jsonBody.setObj(data);
			return jsonBody;
		}
		//获取用户关联的设备
		if(Constrant.LIST_DEVICE.equals(cmd)){
			String uid = getParams(req, "uid");
			
			if(StringUtils.isEmpty(uid)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			
			List list = deviceService.listByUid(uid);
			jsonBody.setObj(list);
			return jsonBody;
		}
		//获取单台打印机的状态
		if(Constrant.GET_DEVICE_STATUS.equals(cmd)){ 
			String pid = getParams(req, "pid");
			String aid = getParams(req, "aid");//设置在线状态
			
			if(StringUtils.isEmpty(aid) || StringUtils.isEmpty(pid)){
				jsonBody.setCode(Constrant.REQUEST_FAIL);
				jsonBody.setMsg("params error");
				return jsonBody;
			}
			List list = deviceService.listByPid(pid,aid);
			if(list!=null && list.size()>0)
			{
				jsonBody.setObj(list.get(0));
				return jsonBody;
			}
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			jsonBody.setMsg("device not exsit");
			return jsonBody;
		}
		return jsonBody;
	}
}
