package Table;

import calculate.ColumnType;
import calculate.HashMapSort;
import calculate.StringTransform;
import model.*;
import similarity.EditDistance;
import similarity.NumricSim;

import java.io.IOException;
import java.util.*;

public class TableToTableSim {

    public static int getAttriData(TableBean table, String attriName) {
        List<String> schema = table.getSchema();
        int temp = -1;
        for (int i = 0; i < schema.size(); i++) {
            if (attriName.equals(schema.get(i))) {
                temp = i;
                break;
            } else if (EditDistance.similarity(schema.get(i), attriName) > 0.8) {
                temp = i;
                break;
            }
        }
        return temp;
    }

    public static double getEdgePotential(TableBean seedTable, FillCell seedFillCell1, TableBean candidateTable) {
        double d1 = getTableToTableSim(seedTable, seedFillCell1, candidateTable);
        return d1;
    }

    public static double getTableToTableSim(TableBean seedTable, FillCell seedFillCell1, TableBean candidateTable) {
        FillCell seedFillCell = seedFillCell1;
        List<Integer> log1 = new ArrayList<Integer>();
        List<Integer> log2 = new ArrayList<Integer>();
        int seedkeyColumn = seedTable.getKeyColumnIndex();
        int keyColumn = candidateTable.getKeyColumnIndex();
        List<String> seedAttributes = seedTable.getAttrubutes();
        List<String> candidateAttributes = candidateTable.getAttrubutes();
        List<String> seedEntity = seedTable.getEntity();
        List<String> candidateEntity = candidateTable.getEntity();
        List<String> seedSchema = seedTable.getSchema();
        List<String> candidateSchema = candidateTable.getSchema();
        List<List<String>> seedColumnContent = seedTable.getColumnContent();
        List<List<String>> candidateColumnContent = candidateTable.getColumnContent();
        Map<String, String> mapAttributes = new HashMap<String, String>();
        Map<Integer, Integer> mapEntitys = new HashMap<Integer, Integer>();
        List<Integer> seedFoundSim = new ArrayList<Integer>();
        List<Integer> candFoundSim = new ArrayList<Integer>();

        //format实体名的内容
        for (int l = 0; l < seedEntity.size(); l++) {
            String string1 = seedSchema.get(seedkeyColumn);
            if (seedEntity.get(l).contains(string1)) {
                seedEntity.set(l, seedEntity.get(l).replace(string1, "").replaceAll("\\(.*?\\)", ""));
            }
        }
        for (int l = 0; l < candidateEntity.size(); l++) {
            String string1 = candidateSchema.get(keyColumn);
            if (candidateEntity.get(l).contains(string1)) {
                candidateEntity.set(l, candidateEntity.get(l).replace(string1, "").replaceAll("\\(.*?\\)", ""));
            }
        }
        double SIMITHRED = 0.8;
        for (int i = 0; i < seedEntity.size(); i++) {
            double max = 0.0;
            int temp = -1;
            String seed_Entity = StringTransform.stringTransform(seedEntity.get(i));
            for (int j = 0; j < candidateEntity.size(); j++) {
                if (log1.contains(j))
                    continue;
                String candidate_Entity = StringTransform.stringTransform(candidateEntity.get(j));
                double sim = EditDistance.similarity(seed_Entity, candidate_Entity);
                if (sim > max) {
                    max = sim;
                    temp = j;
                }
            }

            if (max > SIMITHRED) {
                mapEntitys.put(i, temp);
                log1.add(temp);
            }
        }
        if (mapEntitys.size() == 0) return 0;

        for (int i = 0; i < seedAttributes.size(); i++) {
            double max = 0.0;
            int temp = -1;
            String seed_attribute = StringTransform.stringTransform(seedAttributes.get(i));
            for (int j = 0; j < candidateAttributes.size(); j++) {
                if (log2.contains(j))
                    continue;
                String candidate_attribute = StringTransform.stringTransform(candidateAttributes.get(j));
                double sim = EditDistance.similarity(seed_attribute, candidate_attribute);

                if (sim > max) {
                    max = sim;
                    temp = j;
                }
            }

            if (max > SIMITHRED) {
                log2.add(temp);
                mapAttributes.put(seedAttributes.get(i), candidateAttributes.get(temp));
            }
        }

        Set<String> seedKeySet = mapAttributes.keySet();
        Iterator<String> seedIt = seedKeySet.iterator();
        double score = 0.0;
        while (seedIt.hasNext())//找到两个属性名相同的列
        {
            String string = seedIt.next();  //seed表属性名
            String string1 = mapAttributes.get(string);  //cand表相匹配的属性名
            int temp = -1, temp1 = -1;
            for (int i = 0; i < seedSchema.size(); i++) {
                if (seedSchema.get(i).equals(string))  //seed表中找到该属性id
                {
                    temp = i;
                    seedFoundSim.add(temp);
                    break;
                }
            }
            //从种子表中取一列，再从要查询的表中取相同列名的列
            List<String> seedColumn = seedColumnContent.get(temp);
            for (int i = 0; i < candidateSchema.size(); i++) {
                if (candidateSchema.get(i).equals(string1))  //cand表中取出匹配列的值
                {
                    temp1 = i;
                    candFoundSim.add(temp1);
                    break;
                }
            }
            List<String> candidateColumn = candidateColumnContent.get(temp1);
            for (int l = 0; l < candidateColumn.size(); l++) {
                if (candidateColumn.get(l).contains(string1)) {
                    candidateColumn.set(l, string1.replace(string1, ""));
                }
            }
            if (getScore(string1, seedColumn, candidateColumn, mapEntitys) > 0.6)  //相同实体时属性值匹配度
            {
                score++;
            }
        }

        /*
        属性名传递
         */
        List<String> name = new ArrayList<String>();
        List<String> name1 = new ArrayList<String>();
        Map<String, String> log = new HashMap<String, String>();
        List<String> seedAttri = seedFillCell.getAttributes();
        List<List<String>> seedAttriList = new ArrayList<List<String>>();
        List<List<String>> candAttriList = new ArrayList<List<String>>();
        for (int i = 0; i < seedAttri.size(); i++)  //已填充的空格
        {
            String attri = seedAttri.get(i);
            int address = getAttriData(seedTable, attri);
            if (!seedFoundSim.contains(address)) {
                if (seedColumnContent.size() > address && address >= 0) {
                    name.add(attri);
                    List<String> list = seedColumnContent.get(address);
                    seedAttriList.add(list);
                }
            }
        }
        for (int i = 0; i < candidateAttributes.size(); i++) {
            String attri = candidateAttributes.get(i);

            int address = getAttriData(candidateTable, attri);
            if (!candFoundSim.contains(address)) {
                if (candidateColumnContent.size() > address && address >= 0) {
                    name1.add(attri);
                    List<String> list = candidateColumnContent.get(address);
                    candAttriList.add(list);
                }
            }
        }

        for (int i = 0; i < seedAttriList.size(); i++) {
            double max = 0.0;
            int flag = -1;
            for (int j = 0; j < candAttriList.size(); j++) {
                double d = getScore(name.get(i), seedAttriList.get(i), candAttriList.get(j), mapEntitys);
                if (d > max) {
                    max = d;
                    flag = j;
                }
            }
            if (max > 0.2) {
                score++;
                mapAttributes.put(name.get(i), name1.get(flag));
                log.put(name1.get(flag), name.get(i));
            }
        }
        if (mapAttributes.size() == 0) return 0;
        //属性名传递 //TODO 未传递出去
        if (log.size() != 0) {
            Set<String> set = log.keySet();
            Iterator<String> it_0 = set.iterator();
            while (it_0.hasNext()) {
                String str1 = (String) it_0.next();
                String str2 = log.get(str1);
                int temp = candidateSchema.indexOf(str1);
                candidateSchema.set(temp, str2);
            }
        }
        return score / mapAttributes.size();
    }

    public static double getScore(String attri, List<String> seedColumn, List<String> candidateColumn, Map<Integer, Integer> mapEntitys) {
        Map<Integer, Integer> mapEntity = mapEntitys;
        double THRESHSTRING = 0.8;
        if (mapEntity.size() > 0) {
            boolean flag = false;
            attri = attri.toLowerCase();
            if (attri.contains("year") || attri.contains("date"))
                flag = true;
//			System.out.println(ColumnType.isDate(seedColumn)+" "+ColumnType.isDate(candidateColumn));
            if ((ColumnType.isDate(seedColumn) && ColumnType.isDate(candidateColumn)) || flag) {
                //System.out.println(seedColumn);
                //System.out.println(candidateColumn);
                Set<Integer> keySet = mapEntity.keySet();
                Iterator<Integer> it = keySet.iterator();
                double up = 0;
                while (it.hasNext()) {
                    Integer t = (Integer) it.next();
                    Integer t1 = mapEntitys.get(t);

                    if (seedColumn.get(t).equals(candidateColumn.get(t1))) {
                        //System.out.println(seedColumn.get(t)+"  "+candidateColumn.get(t1));
                        up++;
                    }
                }
                //System.out.println("parse"+up);
                return up / mapEntity.size();
            } else if (ColumnType.isNumeric(seedColumn) && ColumnType.isNumeric(candidateColumn)) {
                //	System.out.println("number");
                NumricSim numricSim = new NumricSim(0.8);
                Map<String, Integer> map = new HashMap<String, Integer>();
                List<Double> score = new ArrayList<Double>();
                Set<Integer> keySet = mapEntity.keySet();
                Iterator<Integer> it = keySet.iterator();
                double zero = 0;
                while (it.hasNext()) {
                    Integer t = (Integer) it.next();
                    Integer t1 = mapEntitys.get(t);
                    String seed = seedColumn.get(t);
                    String candidate = candidateColumn.get(t1);
                    seed = seed.replaceAll(",", "");
                    candidate = candidate.replace(",", "");
                    try {
                        double s = Double.parseDouble(seed);
                        double c = Double.parseDouble(candidate);
                        if (s == 0.0 && c == 0.0) {
                            zero++;

                        } else if ((s != 0.0 && c == 0.0) || (s == 0.0 && c != 0.0)) {
                            continue;
                        } else {
                            score.add(s / c);
                        }

                    } catch (NumberFormatException e) {
                    }
                }
                int sum = mapEntity.size();
                if (score.size() != 0) {
                    for (int i = 0; i < score.size(); i++) {
                        int up = 0;
                        for (int j = 0; j < score.size(); j++) {
                            if (numricSim.equal(score.get(i), score.get(j))) {
                                up++;
                            }
                        }
                        map.put(score.get(i).toString(), up);
                    }
                    map = HashMapSort.sortMapByIntValue(map);
                    Set<String> keyset = map.keySet();
                    Iterator<String> keyit = keyset.iterator();
                    int temp = map.get((String) keyit.next());
                    if (sum > 1)
                        return (double) ((temp + zero) / sum);
                    if (sum == 1) {
                        double dou1 = 0.0;
                        int log1 = 0;
                        for (int m = 0; m < seedColumn.size(); m++) {
                            try {
                                dou1 += Double.parseDouble(seedColumn.get(m));
                            } catch (NumberFormatException nfe) {
                                log1++;
                            }
                        }
                        dou1 = dou1 / (seedColumn.size() - log1);
                        double dou2 = 0.0;
                        int log2 = 0;
                        for (int m = 0; m < candidateColumn.size(); m++) {
                            try {
                                dou2 += Double.parseDouble(candidateColumn.get(m));
                            } catch (NumberFormatException nfe) {
                                log2++;
                            }
                        }
                        dou2 = dou2 / (candidateColumn.size() - log2);
                        double dou = dou1 / dou2;
                        if (numricSim.equal(dou, score.get(0))) {
                            return 1;
                        } else
                            return 0;

                    } else
                        return 0;
                }

            } else {
                //System.out.println("string");
                Set<Integer> keySet = mapEntity.keySet();
                Iterator<Integer> it = keySet.iterator();
                double up = 0;
                while (it.hasNext()) {
                    Integer t = (Integer) it.next();
                    Integer t1 = mapEntitys.get(t);
                    String seed = StringTransform.stringTransform(seedColumn.get(t));
                    String candidate = StringTransform.stringTransform(candidateColumn.get(t1));
                    if (EditDistance.similarity(seed, candidate) > THRESHSTRING) {
                        up++;
                    }
                }
                return up / mapEntity.size();
            }
        }
        return 0.0;
    }


    public static double getNodePotential(QueryTable qt, List<Cell> seedFillCell, TableBean candidateTable) {
        List<String> queryEntitys = qt.getEntity();
        List<String> queryAttris = qt.getAttributes();
        List<Cell> queryCells = new ArrayList<Cell>();
        List<String> candAttrisList = candidateTable.getAttrubutes();
        List<String> candEntitysList = candidateTable.getEntity();
        List<Cell> candCells = new ArrayList<Cell>();
        List<Cell> seedCells = seedFillCell;   //已经填充的空格
        double count = 0.0;
        double count0 = 0.0;
        for (int i = 0; i < candEntitysList.size(); i++) {
            for (int j = 0; j < candAttrisList.size(); j++) {

                Cell cell = new Cell();
                cell.setEntity(candEntitysList.get(i));
                cell.setAttribute(candAttrisList.get(j));
                candCells.add(cell);
            }
        }
        for (int i = 0; i < queryEntitys.size(); i++) {
            for (int j = 0; j < queryAttris.size(); j++) {
                Cell cell = new Cell();
                cell.setEntity(queryEntitys.get(i));
                cell.setAttribute(queryAttris.get(j));
                queryCells.add(cell);
            }
        }
        //除去已经填充的空格
        double sum = queryCells.size();
        for (int i = 0; i < queryCells.size(); i++) {
            for (int j = 0; j < seedCells.size(); j++) {
                if (queryCells.get(i).equals(seedCells.get(j))) {

                    count0++;
                    queryCells.remove(i);
                    i--;
                    break;
                }
            }
        }
        //候选表可以填充的空格
        for (int i = 0; i < queryCells.size(); i++) {
            for (int j = 0; j < candCells.size(); j++) {
                if (queryCells.get(i).equals(candCells.get(j))) {
                    count++;
                    break;
                }
            }
        }
        return count / (sum - count0);
    }

    public static CandidateTable getPotential(QueryTable qt, List<Cell> cellList, TableBean candidateTable, TableBean seedTable, FillCell fillCell) {
        CandidateTable ct = new CandidateTable();
        double node = getNodePotential(qt, cellList, candidateTable);
        double edge = getEdgePotential(seedTable, fillCell, candidateTable);
        edge += candidateTable.edgeScore;  //加上与之前种子节点的边潜能
        candidateTable.edgeScore = edge;

        List<String> query_entity = qt.getEntity();
        List<String> webtable_entity = candidateTable.getEntity();
        List<String> query_attribute = qt.getAttributes();
        List<String> webtable_attribute = candidateTable.getAttrubutes();
        List<String> ent = new ArrayList<String>();
        List<String> attri = new ArrayList<String>();
        List<Integer> log1 = new ArrayList<Integer>();
        List<Integer> log2 = new ArrayList<Integer>();
        FillCell fc = new FillCell();
        for (int i = 0; i < query_entity.size(); i++) {
            double max = 0.0;
            int temp = -1;
            String queryentity = StringTransform.stringTransform(query_entity.get(i));
            for (int j = 0; j < webtable_entity.size(); j++) {
                if (log1.contains(j))
                    continue;
                String webentity = StringTransform.stringTransform(webtable_entity.get(j));
                double sim = EditDistance.similarity(queryentity, webentity);
                if (sim > max) {
                    max = sim;
                    temp = j;
                }
            }
            if (max > 0.7) {
                ent.add(webtable_entity.get(temp));
                log1.add(temp);
            }
        }
        for (int i = 0; i < query_attribute.size(); i++) {
            double max = 0.0;
            int temp = -1;
            String queryattribute = StringTransform.stringTransform(query_attribute.get(i));
            for (int j = 0; j < webtable_attribute.size(); j++) {
                if (log2.contains(j))
                    continue;
                String webattribute = StringTransform.stringTransform(webtable_attribute.get(j));
                double sim = EditDistance.similarity(queryattribute, webattribute);
                if (sim > max) {
                    max = sim;
                    temp = j;
                }
            }
            if (max > 0.7) {
                attri.add(webtable_attribute.get(temp));
                log2.add(temp);
            }
        }
        fc.setAttributes(attri);
        fc.setEntitys(ent);
        ct.setFillcell(fc);
        ct.setTableBean(candidateTable);

        if (node <= 0.00000000001)   //节点潜能为0，不能补充单元格
            ct.setScore(0);
        else
            ct.setScore(edge + node);
        return ct;
    }

}
