package model;


import calculate.StringTransform;
import similarity.EditDistance;

import java.io.Serializable;

public class Cell implements Serializable {
	private String data;
	private boolean falg;
	public String getData() {
		return data;
	}
	public boolean isFalg() {
		return falg;
	}
	public void setFalg(boolean falg) {
		this.falg = falg;
	}
	public void setData(String data) {
		this.data = data;
	}
	private String entity;
	private String attribute;
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public boolean equals1(Object obj)
	{
		double SIMITHRED = 0.7;
		if(obj instanceof Cell)
		{
			Cell c=(Cell)obj;
			String entity = this.entity;
			String entity1=c.entity;
			//double sim = EditDistance.similarity(entity, entity1);
			String attri = this.attribute;
			String attri1=c.attribute;
			//double sim1 = EditDistance.similarity(attri, attri1);
			if(entity.equals(entity1)&&attri.equals(attri1))
				return true;
			else 
				return false;
		}
		return false;
	}
	public boolean equals(Object obj)
	{
		double SIMITHRED = 0.8;
		if(obj instanceof Cell)
		{
			Cell c=(Cell)obj;
			String entity = StringTransform.stringTransform(this.entity);
			String entity1=StringTransform.stringTransform(c.entity);
			double sim = EditDistance.similarity(entity, entity1);
			String attri = StringTransform.stringTransform(this.attribute);
			String attri1=StringTransform.stringTransform(c.attribute);
			double sim1 = EditDistance.similarity(attri, attri1);
			if(sim>SIMITHRED&&sim1>SIMITHRED)
				return true;
			else 
				return false;
		}
		return false;
	}
	public static void main(String[] args)
	{
		Cell cell=new Cell();
		System.out.println(cell.isFalg());
		cell.setFalg(true);
		System.out.println(cell.isFalg());
	}
}
