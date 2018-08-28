package excel;


import Table.TableToTableSim;
import model.*;
import similarity.EditDistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAugTable {

    public static int[][] getCov(QueryTable qt, TableBean t) {
        int row = qt.getEntity().size();
        int column = qt.getAttributes().size();
        int[][] cov = new int[row][column];
        List<String> qtEntity = qt.getEntity();
        List<String> qtAttri = qt.getAttributes();
        List<String> tEntity = t.getEntity();
        List<String> tAttri = t.getAttrubutes();
        for (int i = 0; i < qtEntity.size(); i++) {
            String entity = qtEntity.get(i);
            for (int j = 0; j < qtAttri.size(); j++) {
                String attri = qtAttri.get(j);
                for (int m = 0; m < tEntity.size(); m++) {
                    boolean flag = false;
                    String entity1 = tEntity.get(m);
                    for (int n = 0; n < tAttri.size(); n++) {
                        String attri1 = tAttri.get(n);
                        if (EditDistance.similarity(entity, entity1) > 0.8 && EditDistance.similarity(attri, attri1) > 0.8) {
                            cov[i][j] = 1;
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                        break;
                }
            }
        }
        return cov;
    }

    public static void createAugTabletopk(String path, QueryTable qt, List<TableBean> tabs) throws IOException {
        List<List<String>> rowContent = new ArrayList<List<String>>();
        int rowNum = qt.getEntity().size();
        List<String> entity = qt.getEntity();
        int columnNum = qt.getAttributes().size() + 1;
        List<String> schema = qt.getSchema();
        for (int i = 0; i < rowNum; i++) {
            List<String> row = new ArrayList<String>();
            row.add(entity.get(i));
            for (int j = 0; j < columnNum - 1; j++) {
                row.add(null);
            }
            rowContent.add(row);
        }
        for (int i = 0; i < tabs.size(); i++) {
            TableBean t = tabs.get(i);
            String[][] data = getData(qt, t);
            for (int m = 0; m < data.length; m++) {
                for (int n = 0; n < data[0].length; n++) {
                    if (data[m][n] != null)
                        rowContent.get(m).set(n + 1, data[m][n]);
                }
            }
        }
        ExcelManage em = new ExcelManage();
        String title[] = schema.toArray(new String[schema.size()]);
        em.createExcel(path + "\\querytable-book.xls", "sheet1", title, 0);
        for (List<String> rows : rowContent) {
            em.writeToExcel(path + "\\querytable-book.xls", "sheet1", rows, 0);
        }
    }

    public static String[][] getData(QueryTable qt, TableBean t) {
        int row = qt.getEntity().size();
        int column = qt.getAttributes().size();
        String[][] data = new String[row][column];
        List<String> qtEntity = qt.getEntity();
        List<String> qtAttri = qt.getAttributes();
        List<String> tEntity = t.getEntity();
        List<String> tAttri = t.getAttrubutes();
        List<List<String>> rowContent = t.getRowContent();
        for (int i = 0; i < qtEntity.size(); i++) {
            String entity = qtEntity.get(i);
            for (int j = 0; j < qtAttri.size(); j++) {
                String attri = qtAttri.get(j);
                for (int m = 0; m < tEntity.size(); m++) {
                    boolean flag = false;
                    String entity1 = tEntity.get(m);
                    for (int n = 0; n < tAttri.size(); n++) {
                        String attri1 = tAttri.get(n);
                        if (EditDistance.similarity(entity, entity1) > 0.8 && EditDistance.similarity(attri, attri1) > 0.8) {
                            data[i][j] = rowContent.get(m).get(n + 1);
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                        break;
                }
            }
        }
        return data;
    }

    public static void createAugTable(String path, QueryTable qt, SeedTableSet seedTables) {

        List<String> querySchema = qt.getSchema();
        List<String> queryColumn = qt.getAttributes();
        List<List<String>> queryRow = qt.getRowContent();
        List<Cell> fillcells = seedTables.getSetFillCells();
        String storePath = path+"/result.xls";
        for (CandidateTable candidateTable : seedTables.seedTables) {
            TableBean t = candidateTable.getTableBean();
            List<String> seedEntity = t.getEntity();
            List<String> seedAttri = t.getAttrubutes();
            List<List<String>> rowContent = t.getRowContent();
            for (int l = 0; l < seedEntity.size(); l++) {
                List<String> row = rowContent.get(l);
                for (int j = 0; j < seedAttri.size(); j++) {
                    String s1 = seedAttri.get(j);
                    int column = TableToTableSim.getAttriData(t, s1);
                    Cell cell = new Cell();
                    cell.setEntity(seedEntity.get(l));
                    cell.setAttribute(seedAttri.get(j));
                    cell.setData(row.get(column));
                    for (Cell fillcell : fillcells) {
                        if (cell.equals1(fillcell) && !fillcell.isFalg()) {
                            fillcell.setData(cell.getData());
                            fillcell.setFalg(true);
                        }
                    }
                }
            }

        }
        for (int i = 0; i < queryRow.size(); i++) {
            String temp = queryRow.get(i).get(0);
            for (int j = 0; j < queryColumn.size(); j++) {
                Cell cell = new Cell();
                cell.setEntity(temp);
                cell.setAttribute(queryColumn.get(j));
                String data = null;
                int t = querySchema.indexOf(queryColumn.get(j));
                for (Cell c : fillcells) {
                    if (c.equals(cell)) {
                        data = c.getData();
                        break;
                    }
                }
                queryRow.get(i).set(t, data);
            }
        }
        ExcelManage em = new ExcelManage();
        String title[] = querySchema.toArray(new String[querySchema.size()]);
        em.createExcel(storePath, "sheet1", title, 0);
        for (List<String> rows : queryRow) {
            em.writeToExcel(storePath, "sheet1", rows, 0);
        }
    }

}
