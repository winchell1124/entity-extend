package model;

import java.io.Serializable;
import java.util.List;

public class QueryTable implements Serializable {
	private List<String> entity;
	private List<String> attributes;
	private List<String> schema;
	private List<List<String>> rowContent;
	public List<String> getEntity() {
		return entity;
	}
	public List<String> getSchema() {
		return schema;
	}
	public void setSchema(List<String> schema) {
		this.schema = schema;
	}
	public List<List<String>> getRowContent() {
		return rowContent;
	}
	public void setRowContent(List<List<String>> rowContent) {
		this.rowContent = rowContent;
	}
	public void setEntity(List<String> entity) {
		this.entity = entity;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	

}
