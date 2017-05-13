package com.qy.repository;

import java.util.List;

import com.qy.beans.BaseData;

public abstract class BaseRepository {
	public abstract Object add(BaseData baseData)throws Exception;

	public abstract Object edit(BaseData baseData)throws Exception;

	public abstract List list(BaseData baseData)throws Exception;

	public abstract Object del(BaseData baseData)throws Exception;
	
}
