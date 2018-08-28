package Table;

import model.*;
import java.io.*;
import java.util.*;

public class GetSeedTableSets {

    public static CandidateTable getCandidateTable(QueryTable qt, SeedTableSet seedSet, TableBean tableBean) {

        //获取种子集中最新候选表
        CandidateTable lastCandTable = seedSet.seedTables.get(seedSet.seedTables.size() - 1);
        CandidateTable cand = TableToTableSim.getPotential(qt, seedSet.getSetFillCells(), tableBean, lastCandTable.getTableBean(), lastCandTable.getFillcell());

        return cand;
    }

    public static SeedTableSet getNewCoverRate(QueryTable qt, SeedTableSet lastSeedSet, CandidateTable candidateTable) {

        SeedTableSet seedTableSet = new SeedTableSet();
        seedTableSet.setScore(lastSeedSet.getScore() + candidateTable.getScore());
        seedTableSet.seedTables = lastSeedSet.seedTables;
        seedTableSet.seedTables.add(candidateTable);

        List<Cell> cells = lastSeedSet.getSetFillCells();
        List<Cell> candcells = new ArrayList<Cell>();
        List<String> candEntity = candidateTable.getFillcell().getEntitys();
        List<String> candAttri = candidateTable.getFillcell().getAttributes();

        List<Cell> newFillCell = new ArrayList<Cell>();
        newFillCell.addAll(cells);
        for (String entity : candEntity) {
            for (String attri : candAttri) {
                Cell cell = new Cell();
                cell.setEntity(entity);
                cell.setAttribute(attri);
                candcells.add(cell);
            }
        }

        for (Cell cell : candcells) {
            boolean flag = false;
            for (Cell cell1 : cells) {
                if (cell1.equals(cell)) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                newFillCell.add(cell);
            }
        }
        seedTableSet.setSetFillCells(newFillCell);
        double up = newFillCell.size();
        double down = qt.getEntity().size() * qt.getAttributes().size();
        seedTableSet.setCoverage(up / down);
        return seedTableSet;
    }
}
