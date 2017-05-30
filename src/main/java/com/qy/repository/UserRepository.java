package com.qy.repository;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.qy.beans.BaseData;
import com.qy.beans.UserInfor;
import com.qy.beans.UserPrinter;

@Repository
public class UserRepository/* extends BaseRepository*/ {
	private static Logger log = LoggerFactory.getLogger(UserRepository.class);
	@Autowired
	private SqlMapClient sqlMapClient;

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	
	public Object add(BaseData baseData) {
		try {
			return sqlMapClient.insert("insertUser", (UserInfor) baseData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void insertUserPrinter(UserPrinter up){
		try {
			sqlMapClient.insert("insertuserprinter", up);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void incUserPrintTimes(String uid) {
		try {
			sqlMapClient.update("incUserPrintTimes", uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public Object edit(BaseData baseData) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List list(BaseData baseData) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object del(BaseData baseData) {
		// TODO Auto-generated method stub
		return null;
	}

}
