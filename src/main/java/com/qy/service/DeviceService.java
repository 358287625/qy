package com.qy.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qy.beans.App;
import com.qy.beans.BaseData;
import com.qy.beans.Device;
import com.qy.beans.Doc;
import com.qy.common.Constrant;
import com.qy.common.Tools;
import com.qy.repository.DeviceRepository;
import com.qy.repository.DocRepository;

@Service
public class DeviceService extends BaseService{
	private static  Logger log = LoggerFactory.getLogger(DeviceService.class);
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DocRepository docRepository;
	public void updateTask(Doc doc){
		docRepository.edit(doc);
	}
	public List getTasks(String pid){
		List<Doc> list= docRepository.list(pid);
		List<Map<String,String>> maps = new ArrayList<>();
		for(Doc doc : list){
			Map<String,String> map = new HashMap<String, String>();
			map.put("tid",doc.getDocid() );
			map.put("pid",doc.getDid() );
			map.put("pdf", doc.getPdfpath());
			map.put("num", ""+doc.getNum());
			map.put("ab", ""+doc.getAb());
			maps.add(map);
		}
		return maps;
	}
	
	public void reportAppStatus(final String aid){
		Tools.cachedThreadPool.execute(new Runnable() {
			public void run() {
				deviceRepository.updateAppAndPrintStatus(aid, Constrant.DEVICE_ON_LINE);
			}
		});
	}
	/**
	 * 第一次安装app
	 * @param app
	 */
	public void installApp(App app){
		//检查app是否已经安装
		List list= deviceRepository.listApps(app);
		if(list !=null && list.size()>0){//有重复
			app.setAid(((App) list.get(0)).getAid());
			return ;
		}
		final String aid = UUID.randomUUID().toString().replace("-", "");
		app.setAid(aid);
		//保存主题信息，返回aid
		deviceRepository.insertApp(app);
		//异步保存位置信息
		final String ip = app.getIp();
//		Tools.cachedThreadPool.execute(new Runnable() {
//			public void run() {
//				//省、市、区、详细地址、经度、纬度、地区编号
//				String[] locations =Tools.getLocationByIPIP(ip);
//				App app = new App();
//				app.setIp(ip);
//				app.setAid(aid);
//				app.setAddr(locations[3]);
//				app.setCity(locations[1]);
//				app.setCode(locations[6]);
//				app.setDistrict(locations[2]);
//				app.setLat(locations[5]);
//				app.setLng(locations[4]);
//				app.setProvince(locations[0]);	
//				deviceRepository.edit(app);
//			}
//		});
		
	}
	public Object add(BaseData baseData) {
		Device device = (Device)baseData;
		List list = deviceRepository.listByPid(device.getPid());//打印机id由设备端生成，传给后台，后台用于去重
		if(list !=null && list.size()>0){//有重复
			return list.get(0);
		}
		final String did = UUID.randomUUID().toString().replace("-", "");
		device.setDid(did);
	
		deviceRepository.add(device);
		final String aid=device.getAid();
		Tools.cachedThreadPool.execute(new Runnable() {
			public void run() {
				deviceRepository.updateAppAndPrintStatus(aid, Constrant.DEVICE_ON_LINE);
			}
		});
		return device;
	}
	

	
	public Object edit(BaseData baseData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<BaseData> listByPid(final String pid) {
		/*Tools.cachedThreadPool.execute(new Runnable() {
			public void run() {
				deviceRepository.updateAppAndPrintStatus(aid, Constrant.DEVICE_ON_LINE);
			}
		});*/
		return deviceRepository.listByPid(pid);
	}
	/**
	 * 查询出用户关联的所有设备
	 * @param uid
	 * @return
	 */
	public List<BaseData> listByUid(String uid) {
		return deviceRepository.listByUid(uid);
	}
	
	public Object del(BaseData baseData) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
