package model;


import java.io.Serializable;
import java.util.List;

public class SeedTableSet implements Serializable {
	public List<CandidateTable> seedTables;  //答案表
	private List<Cell> setFillCells;	//可以填充的单元格
	public List<Cell> getSetFillCells() {
		return setFillCells;
	}
	public void setSetFillCells(List<Cell> setFillCells) {
		this.setFillCells = setFillCells;
	}
	private double score; //所有答案表的潜能之和
	private double coverage; //覆盖率
	public double getCoverage() {
		return coverage;
	}
	public void setCoverage(double coverage) {
		this.coverage = coverage;
	}
	public List<CandidateTable> getSeedTables() {
		return seedTables;
	}
	public void setSeedTables(List<CandidateTable> seedTables) {
		this.seedTables = seedTables;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

}
