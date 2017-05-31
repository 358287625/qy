package com.qy.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.qy.beans.Doc;
import com.qy.common.Constrant;
import com.qy.common.Tools;
import com.qy.service.DocConvert;
import com.qy.service.DocService;
import com.qy.vo.JsonBody;

@Controller
@RequestMapping(value = "/doc")
public class DocController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(DeviceController.class);
	@Autowired
	private DocService docService;

	/**
	 * 
	  * @ClassName UploadAction    @Description 
	 * 1.form的enctype=”multipart/form-data” 这个是上传文件必须的  * @author liujx 
	 *  @date 2017年5月23日      
	 */

	@RequestMapping(value = "/upload")
	@ResponseBody
	public JsonBody upload(
			@RequestParam(value = "myfile", required = true) MultipartFile file,
			final HttpServletRequest request, final String uid, final String pid,final String aid) {
		final String ua = getHeaderVal(request, Constrant.UA);
		log.info("ua="+ua);
		JsonBody jsonBody = new JsonBody();
		if (StringUtils.isEmpty(aid) ||StringUtils.isEmpty(uid) || StringUtils.isEmpty(pid)
				|| pid.length() > 32) {
			jsonBody.setMsg("request parameter error");
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			return jsonBody;
		}
		// uid 防止多人上传同一个名称的文档
		// pid 打印机编号
		String path = request.getSession().getServletContext()
				.getRealPath("upload");// file_store_path
		final String absolutePath = path + File.separator+file.getOriginalFilename();
		log.info(absolutePath);
		final String printDocUuid = UUID.randomUUID().toString()
				.replace("-", "");
		File targetFile = new File(absolutePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		} else {
			targetFile.delete();
		}
		// 保存
		try {
			file.transferTo(targetFile);
			Tools.cachedThreadPool.execute(new Runnable() {
				public void run() {
					// 打印文档路径，打印用户uid，那台打印机aid 打印的文档文档入库
					Doc doc = new Doc();
					doc.setDid(pid);
					doc.setUid(uid);
					doc.setAid(aid);
					doc.setSrcpath(absolutePath);
					doc.setDocid(printDocUuid);
					doc.setUa(ua);
					doc.setIp(Tools.getRequestIp(request));
					doc.setNum(1);// 默认打印一张
					// 经度纬度解析后面再解析
					docService.add(doc);
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Map map = new HashMap<String, String>();
		map.put("tid", printDocUuid);
		jsonBody.setObj(map);
		return jsonBody; // 后天提交打印的配置，需要携带改参数
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JsonBody handleException(Exception ex, HttpServletRequest request) {
		Map<Object, Object> model = new HashMap<Object, Object>();
		JsonBody jsonBody = new JsonBody();
		if (ex instanceof MaxUploadSizeExceededException) {
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			jsonBody.setMsg("file upload exceeded limit max size");
		} else {
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			jsonBody.setMsg("operate failed");
		}
		return jsonBody;
	}

	/**
	 * 
	 * @Title: printSet
	 * @Description: 一个打印文档的打印配置，双面打印，从几页打印到几页
	 * @param req
	 * @param res
	 * @return 参数说明
	 * @return String 返回类型
	 */
	@RequestMapping(value = "/set")
	@ResponseBody
	public JsonBody printSet(HttpServletRequest req, HttpServletResponse res) {
		String fromPage = req.getParameter("f");// 客户传值从1开始，如果fromPage、toPage为零或者为空，表示全部打印
		String toPage = req.getParameter("t");
		String num = req.getParameter("num");// 打印分数
		String ab = req.getParameter("ab");// 双面打印
		String taskId = req.getParameter("tid"); // 该步保存后就可以开始文档转换了

		JsonBody jsonBody = new JsonBody();
		if (StringUtils.isEmpty(taskId)) {
			jsonBody.setCode(Constrant.REQUEST_FAIL);
			jsonBody.setMsg("params error");
			return jsonBody;
		}
		// 入库
		Doc doc = new Doc();
		doc.setAb("1".equals(ab) ? 1 : 0);
		doc.setNum(Integer.valueOf(num));
		doc.setPageFrom(StringUtils.isNumeric(fromPage) ? Integer
				.valueOf(fromPage) : 0);
		doc.setPageTo(StringUtils.isNumeric(toPage) ? Integer.valueOf(toPage)
				: 0);
		doc.setDocid(taskId);
		docService.edit(doc);

		return jsonBody;
	}

	/**
	 * 
	 * @Title: exchangeDoc
	 * @Description: 文档转换接口
	 * @param req
	 * @param res
	 * @return 参数说明
	 * @return String 返回类型
	 */
	@RequestMapping(value = "exc")
	@ResponseBody
	public JsonBody exchangeDoc(HttpServletRequest req, HttpServletResponse res) {
		String path = req.getParameter("p");// 转换文档的路径
		String taskId = req.getParameter("tid");// 文档转换的任务id
		String fromPage = req.getParameter("f");// 客户传值从1开始，如果fromPage、toPage为零或者为空，表示全部打印
		String toPage = req.getParameter("t");
		String did = req.getParameter("did");// 那台打印机
		String ab = req.getParameter("ab");// 双面打印
		System.out.println("exchange_task - " + taskId + " - " + fromPage
				+ " - " + toPage + " - " + did + " - " + ab + " - " + path);

		Doc doc = new Doc();
		doc.setAb("1".equals(ab) ? 1 : 0);
		doc.setDocid(taskId);
		doc.setPageFrom(StringUtils.isNumeric(fromPage) ? Integer
				.valueOf(fromPage) : 0);
		doc.setPageTo(StringUtils.isNumeric(toPage) ? Integer.valueOf(toPage)
				: 0);
		doc.setSrcpath(path);
		doc.setDid(did);
		DocConvert.getInstance().convert2PDF(doc);
		JsonBody jsonBody = new JsonBody();
		return jsonBody;
	}
}
