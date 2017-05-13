package com.qy.service;

import java.sql.SQLException;

import com.qy.beans.BaseData;
import com.qy.vo.PageModel;

public abstract class BaseService {
	public abstract Object add( BaseData baseData)throws Exception;

	public abstract Object edit( BaseData baseData)throws Exception;

	public abstract PageModel list(BaseData baseData);

	public abstract Object del(BaseData baseData) throws SQLException ;
}
