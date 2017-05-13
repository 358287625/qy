package com.qy.vo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class PageModel {
    /**每页显示多少条记录*/
    private int pageSize;   
    /**记录总数*/
    private int count;    //记录总数
    /**开始记录数*/
    private int start;
    /***/
    private List datas;
    /**当前页码*/
    private int curPage;
    /**总页数*/
    private int   totalPage;
    /*查询时传递的查询参数*/
    private String searchParam;
    //放置页面需要的其他参数
    private Map<String,Object> resultMap;
    private Map<String,Object> paramMap = new HashMap<String,Object>();
    private long num;
    
    public static final String JSON_CAST_NAME = "page";
    
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
		setTotalPage(this.count==0?1:(this.count +this.pageSize - 1)/(this.pageSize==0?10:this.pageSize));
	}
	public List getDatas() {
		return datas;
	}
	public void setDatas(List datas) {
		this.datas = datas;
	}
	public int getCurPage() {
		return curPage+1;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	
	public int getTotalPage() {
		if(totalPage == 0 )
			totalPage = 1;
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getStart() {
		return getCurPage() -1;
	}
	public void setStart(int start) {
		this.start = start * pageSize;
	}
	
	public static String getJsonCastName() {
		return JSON_CAST_NAME;
	}
	public String getSearchParam()
	{
		return searchParam;
	}
	public void setSearchParam(String searchParam)
	{
		this.searchParam = searchParam;
	}
	public Map<String,Object> getResultMap()
	{
		if(resultMap==null){
			resultMap = new HashMap<String, Object>();
		}
		return resultMap;
	}
	public void setResultMap(Map<String,Object> resultMap)
	{
		this.resultMap = resultMap;
	}
	
	public Map<String, Object> getParamMap()
	{
		return paramMap;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	
}
