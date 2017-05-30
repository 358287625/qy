package com.qy.beans;

import java.util.Date;

/**
 * 
 * @author Administrator
 * 
 */
public class Doc extends BaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9009149688772258594L;
	private String docid;// '文档唯一标记',
	private String uid;// '用户唯一标记',
	private String did;// '打印设备唯一标记',
	private String aid;//appid
	private String srcpath;// '原文件路径',
	private String pdfpath;// 'pdf文件路径',
	private String zipPath;
	private int num;// '打印份数',
	private int ab;// '正反打印.0单面打印，1双面打印',
	private int st;// '文档状态：0已上传，1已通知下载，2下载中，3已打印cg，4打印失败，失败时error有错误原因',
	private int pageFrom;
	private int pageTo;
	private int fee;// '支付金额',
	private int feetype;// '支付类型',
	private String code;// '用户打印时政区编码',
	private String province;// '用户打印时省',
	private String city;// '用户打印时市',
	private String district;// '用户打印时区',
	private String addr;// '用户打印时详细地址',
	private String ip;// '用户打印时上网地址',
	private String ua;// '用户打印时浏览器ua',
	private String error;// '用户打印文档错误原因标号',
	private String lng;
	private String lat;
	private Date ctime;// '创建时间',
	private Date utime;// '更新时间',
	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public int getPageFrom() {
		return pageFrom;
	}

	public void setPageFrom(int pageFrom) {
		this.pageFrom = pageFrom;
	}

	public int getPageTo() {
		return pageTo;
	}

	public void setPageTo(int pageTo) {
		this.pageTo = pageTo;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getSrcpath() {
		return srcpath;
	}

	public void setSrcpath(String srcpath) {
		this.srcpath = srcpath;
	}

	public String getPdfpath() {
		return pdfpath;
	}

	public void setPdfpath(String pdfpath) {
		this.pdfpath = pdfpath;
	}

	

	public String getZipPath() {
		return zipPath;
	}

	public void setZipPath(String zipPath) {
		this.zipPath = zipPath;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getAb() {
		return ab;
	}

	public void setAb(int ab) {
		this.ab = ab;
	}

	public int getSt() {
		return st;
	}

	public void setSt(int st) {
		this.st = st;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getFeetype() {
		return feetype;
	}

	public void setFeetype(int feetype) {
		this.feetype = feetype;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

}
