package com.qy.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.qy.beans.App;
import com.qy.beans.BaseData;
import com.qy.beans.Doc;
import com.qy.common.Constrant;
import com.qy.common.Tools;
import com.qy.mq.MQTTSendMsg;
import com.qy.repository.DocRepository;
@Service
public class DocService extends BaseService {
	private static Logger log = LoggerFactory.getLogger(DeviceService.class);
	@Autowired
	private DocRepository docRepository;
	/**
	 * 把指定的路徑下的文件，打包為zip，名稱和源文件一致
	 * @param doc
	 */
	private void toZip(Doc doc) {

		File srcdir = new File(doc.getPdfpath());
		
		String zipPath =null;
		if(doc.getPdfpath().indexOf(Constrant.PDF_SUB_PATH)>0)
			zipPath = doc.getPdfpath().replace(Constrant.PDF_SUB_PATH, "/zip/");
		else if(doc.getPdfpath().indexOf(Constrant.PDF_PATH)>0)
			zipPath = doc.getPdfpath().replace(Constrant.PDF_PATH, "/zip/");
		File zipFile = new File(zipPath+System.currentTimeMillis()+".zip");
		if (!srcdir.exists())
			return;
		if (!zipFile.getParentFile().exists())
			zipFile.getParentFile().mkdirs();

		if (zipFile.exists())
		{
			zipFile.delete();
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		zip.addFileset(fileSet);

		zip.execute();
		doc.setZipPath(zipFile.getAbsolutePath());
		pushMsg(doc);
	}

	/**
	 * push给长连接，通知来下载打印
	 * @param doc
	 */
	private void pushMsg(Doc doc) {
		Gson gson = new Gson();
		String msgContent = gson.toJson(doc);
		try {
			MQTTSendMsg.sendP2pMsg("", doc.getDid(), msgContent);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void add(BaseData baseData) {
		Doc doc = (Doc)baseData;
		final String docId = doc.getDocid();
		final String   ip =doc.getIp(); 
		docRepository.add(doc);
		Tools.cachedThreadPool.execute(new Runnable() {
			public void run() {
				//省、市、区、详细地址、经度、纬度、地区编号
				String[] locations =Tools.getLocationByIPIP(ip);
				Doc editDoc = new Doc();
				editDoc.setIp(ip);
				editDoc.setDocid(docId);
				editDoc.setAddr(locations[3]);
				editDoc.setCity(locations[1]);
				editDoc.setCode(locations[6]);
				editDoc.setDistrict(locations[2]);
				editDoc.setLat(locations[5]);
				editDoc.setLng(locations[4]);
				editDoc.setProvince(locations[0]);	
				docRepository.edit(editDoc);
			}
		});
	}

	public void edit(BaseData baseData)  {
		docRepository.edit(baseData);
		final String taskId = ((Doc)baseData).getDocid();
		Tools.cachedThreadPool.execute(new Runnable() {
			
			public void run() {
				Doc doc = docRepository.getDocByDocId(taskId);
				if(null == doc)
					return;
		    	String path = DocConvert.getInstance().convert2PDF(doc);
		    	doc.setPdfpath(path);
				toZip(doc);
			}
		});
	}

	public List<BaseData> list(BaseData baseData) {
		return null;
	}

	public Object del(BaseData baseData) throws SQLException {
		return null;
	}

}
