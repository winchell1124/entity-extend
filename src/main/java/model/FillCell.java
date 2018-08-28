package model;

import java.io.Serializable;
import java.util.List;

public class FillCell implements Serializable {
	private List<String> entitys;
	private List<String> attributes;
	public List<String> getEntitys() {
		return entitys;
	}
	public void setEntitys(List<String> entitys) {
		this.entitys = entitys;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	

}
