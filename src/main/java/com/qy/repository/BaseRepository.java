package com.qy.repository;

import java.util.List;

import com.qy.beans.BaseData;

public abstract class BaseRepository {
	public abstract Object add(BaseData baseData);

	public abstract Object edit(BaseData baseData);

	public abstract List list1(BaseData baseData);

	public abstract Object del(BaseData baseData);
	
}
