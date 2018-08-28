package model;

import java.io.Serializable;

public class CandidateTable implements Serializable {
	private String id;
	private double score;
	private FillCell fillcell;
	private TableBean tableBean;
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FillCell getFillcell() {
		return fillcell;
	}
	public void setFillcell(FillCell fillcell) {
		this.fillcell = fillcell;
	}
	public TableBean getTableBean() {
		return tableBean;
	}
	public void setTableBean(TableBean tableBean) {
		this.tableBean = tableBean;
	}
}
