package com.qy.repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.qy.beans.App;
import com.qy.beans.BaseData;
import com.qy.beans.Device;
@Repository 
public class DeviceRepository /*extends BaseRepository */{
	private static  Logger log = LoggerFactory.getLogger(DeviceRepository.class);
	@Autowired
  	private SqlMapClient sqlMapClient;
	  
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	
	public Object add(BaseData baseData){
		try {
			sqlMapClient.insert("insertDevice", (Device)baseData);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	
	public void edit(App app){
		try {
			sqlMapClient.update("updateAppinfor",app);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}
	}

	
	public List listByPid(String pid) {
		List<Device> list=null;
		try {
			list = sqlMapClient.queryForList("listDevice",pid );
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	
	public List listByUid(String uid) {
		List<Device> list=null;
		try {
			list = sqlMapClient.queryForList("listDeviceByUid", uid);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	public List listApps(App app){
		List<Device> list=null;
		try {
			list = sqlMapClient.queryForList("listApps", app);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	
	public List installApp(App app){
		List<Device> list=null;
		try {
			list = sqlMapClient.queryForList("installApp", app);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	/**
	 * 更新设备状态
	 * @param aid
	 * @param st
	 */
	public void updateAppAndPrintStatus(String aid,String st){
		if(StringUtils.isEmpty(aid))
			return;
		
		try {
			Map<String,String> map = new HashMap<String, String>();
			map.put("aid", aid);
			map.put("st", st);
			sqlMapClient.update("updateAppAndPrintStatus", map);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public Object del(BaseData baseData){
		// TODO Auto-generated method stub
		return null;
	}
}
