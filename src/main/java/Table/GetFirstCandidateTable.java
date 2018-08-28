package Table;

import calculate.StringTransform;
import model.CandidateTable;
import model.FillCell;
import model.QueryTable;
import model.TableBean;
import similarity.EditDistance;

import java.io.*;
import java.util.*;

public class GetFirstCandidateTable {

    public static CandidateTable getTableScore(QueryTable querytable, TableBean webtable1) {
        double SIMITHRED = 0.7;
        TableBean webtable = webtable1;
        List<Integer> log1 = new ArrayList<Integer>();
        List<Integer> log2 = new ArrayList<Integer>();
        List<String> query_entity = querytable.getEntity();
        List<String> webtable_entity = webtable.getEntity();
        List<String> query_attribute = querytable.getAttributes();
        List<String> webtable_attribute = webtable.getAttrubutes();
        List<Double> entitySimScore = new ArrayList<Double>();
        List<Double> attributeSimScore = new ArrayList<Double>();
        CandidateTable ct = new CandidateTable();
        List<String> ent = new ArrayList<String>();
        List<String> attri = new ArrayList<String>();
        FillCell fc = new FillCell();
        //查看查询表实体和网络表实体的相似度
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

            if (max > SIMITHRED) {
                ent.add(webtable_entity.get(temp));
                log1.add(temp);
                entitySimScore.add(max);
            }
        }
        //查找相同的属性名
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
            if (max > SIMITHRED) {
                attri.add(webtable_attribute.get(temp));
                log2.add(temp);
                attributeSimScore.add(max);
            }
        }
        fc.setAttributes(attri);
        fc.setEntitys(ent);
        double sum1 = 0.0;
        for (int i = 0; i < entitySimScore.size(); i++) {
            sum1 += entitySimScore.get(i);
        }
        double sum2 = 0.0;
        for (int i = 0; i < attributeSimScore.size(); i++) {
            sum2 += attributeSimScore.get(i);
        }
        double score = GetTableCon.getMatchSim(querytable, webtable1);
        double similarity = sum1 * sum2 * score;
        ct.setScore(similarity);
        ct.setFillcell(fc);
        ct.setId(webtable.getAbpath());
        return ct;
    }


}
