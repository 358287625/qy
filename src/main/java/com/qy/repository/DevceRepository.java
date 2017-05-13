package com.qy.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.qy.beans.BaseData;
@Repository 
public class DevceRepository extends BaseRepository{
	private static  Logger log = LoggerFactory.getLogger(DevceRepository.class);
	@Autowired
  	private SqlMapClient sqlMapClient;
	  
	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	@Override
	public Object add(BaseData baseData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object edit(BaseData baseData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list(BaseData baseData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object del(BaseData baseData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
