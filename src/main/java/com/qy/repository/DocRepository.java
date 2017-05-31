package com.qy.repository;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.qy.beans.BaseData;
import com.qy.beans.Doc;

@Repository 
public class DocRepository/* extends BaseRepository*/{
	private static  Logger log = LoggerFactory.getLogger(DocRepository.class);
	@Autowired
  	private SqlMapClient sqlMapClient;
	  
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	
	public Object add(BaseData baseData)  {
		try {
			return sqlMapClient.insert("insertDoc", (Doc)baseData);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	
	public Object edit(BaseData baseData)  {
		try {
			return sqlMapClient.update("updateDocPrintTask", (Doc)baseData);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return 0;
	}

	public Doc getDocByDocId(String docId){
		Object doc=null;
		try {
			doc= sqlMapClient.queryForObject("getPrintTasksByDocId", docId);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			
		}
		return doc==null?null:(Doc)doc;
	}
	public List list(String pid)  {
		try {
			return sqlMapClient.queryForList("getPrintTasks", pid);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	
	public Object del(BaseData baseData)  {
		return null;
	}
}
