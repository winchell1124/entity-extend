package model;

import java.io.Serializable;
import java.util.List;

public class TableBean implements Serializable {
	List<String> schema; //表头（只读取有表头的表格，json文件中"hasHeader"==1）
	List<List<String>> columnContent;  //按列存放表内容
	List<List<String>> rowContent; //按行存放表内容
	List<String> entity;
	List<String> attributes;
	public double edgeScore = 0;
	public List<String> getAttrubutes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	private String pageTitle; //页面标题（用来提取主题域）
	private String id; //表格文件名（用来建立表格标记）
	private String url; //网址(进行网址相似度计算)
	
	private String contextb; //上下文（网页中表格前面的文字）
	private String contexta; //上下文（网页中表格后面的文字）
	
	String abpath; //表格文件绝对路径
	int keyColumnIndex; //指示主列索引号（若==-1，则无主列）
	
	//List<Map<String,Double>> listmapsim; //用来存放
	
	public List<String> getSchema() {
		return schema;
	}
	public void setSchema(List<String> schema) {
		this.schema = schema;
	}
	public List<List<String>> getColumnContent() {
		return columnContent;
	}

	public void setColumnContent(List<List<String>> columnContent) {
		this.columnContent = columnContent;
	}

	public List<List<String>> getRowContent() {
		return rowContent;
	}

	public void setRowContent(List<List<String>> rowContent) {
		this.rowContent = rowContent;
	}
	
	public List<String> getEntity() {
		return entity;
	}
	public void setEntity(List<String> entity) {
		this.entity = entity;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContextb() {
		return contextb;
	}
	public void setContextb(String contextb) {
		this.contextb = contextb;
	}
	public String getContexta() {
		return contexta;
	}
	public void setContexta(String contexta) {
		this.contexta = contexta;
	}
	public String getAbpath() {
		return abpath;
	}
	public void setAbpath(String abpath) {
		this.abpath = abpath;
	}
	public int getKeyColumnIndex() {
		return keyColumnIndex;
	}
	public void setKeyColumnIndex(int keyColumnIndex) {
		this.keyColumnIndex = keyColumnIndex;
	}

}
