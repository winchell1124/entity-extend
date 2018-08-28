package configTest;

import Table.*;
import calculate.CreateDataSource;
import config.PropertiesConfig;
import excel.CreateAugTable;
import model.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.Process;
import scala.Tuple2;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) {
        long startTime=System.nanoTime();   //获取开始时间
        logger.info("--------read queryTable---------");
        final QueryTable queryTable = ReadQueryTable.readQT("F:/project/Entity Augmentation/QueryData/query-book2.xls", "sheet1");
        //读取json文件并转化为TableBean类型
        logger.info("--------read json file---------");
        String datasource = "F:/project/Entity Augmentation/DataSet/Experiments/test";
        File file = new File(datasource);
        String[] jsonPath = file.list();
        List<TableBean> tableBeanList = new ArrayList<TableBean>();
        for (int i = 0; i < jsonPath.length; i++) {
            System.out.println(i + ": " + jsonPath[i]);
            String path = datasource + "/" + jsonPath[i];
            TableBean webTable = ReadJson.ReadJsonFile(path);
            tableBeanList.add(webTable);
        }
        final List<CandidateTable> candidateTableList = new ArrayList<CandidateTable>();
        int ii = 0;
        for (TableBean tableBean : tableBeanList) {
            CandidateTable candidateTable = GetFirstCandidateTable.getTableScore(queryTable, tableBean);
            candidateTable.setTableBean(tableBean);
            candidateTableList.add(candidateTable);
            System.out.println(ii + ": " + tableBean.getId());
            ii++;
        }
        Comparator<CandidateTable> comparator = (t1, t2) -> {
            Double s1 = t1.getScore();
            Double s2 = t2.getScore();
            return s1.compareTo(s2);
        };
        candidateTableList.sort(comparator.reversed());

        //和查询表匹配分数最高的作为第一个种子表
        CandidateTable seedTable = candidateTableList.get(0);
        logger.info("-----------first seedTable  : " + seedTable.getTableBean().getEntity().get(0) + seedTable.getTableBean().getEntity().get(1));
        //种子集
        SeedTableSet seedSet = new SeedTableSet();
        seedSet.setSeedTables(new ArrayList<CandidateTable>());
        seedSet.seedTables.add(seedTable);
        seedSet.setScore(seedTable.getScore());
        List<Cell> cellList = new ArrayList<Cell>();
        for (String i : seedTable.getFillcell().getEntitys()) {
            for (String j : seedTable.getFillcell().getAttributes()) {
                Cell cell = new Cell();
                cell.setEntity(i);
                cell.setAttribute(j);
                cellList.add(cell);
            }
        }
        seedSet.setSetFillCells(cellList);
        //计算当前覆盖率
        double up = seedTable.getFillcell().getEntitys().size() * seedTable.getFillcell().getAttributes().size();
        double down = queryTable.getEntity().size() * queryTable.getAttributes().size();
        seedSet.setCoverage(up / down);
        SeedTableSet resultSet = getResultTables(queryTable, candidateTableList, seedSet, 1, 0.3);
        //计算结果保存为excel
        CreateAugTable.createAugTable("Data", queryTable, resultSet);
        long endTime=System.nanoTime(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)/1000000+"ms");
    }

    public static SeedTableSet getResultTables(final QueryTable queryTable, List<CandidateTable> candidateTables, SeedTableSet seedTableSet, int count, double coverRate) {
        System.out.println(String.format("---------cycle count is: %d     cover rate is: %f", count, seedTableSet.getCoverage()));
        if (seedTableSet.getCoverage() >= coverRate) {
            return seedTableSet;
        }
        if (seedTableSet.getSeedTables().size() > 20) {
            return seedTableSet;
        }
        final SeedTableSet finalSeedTableSet = seedTableSet;
        List<CandidateTable> newCandList = new ArrayList<>();
        for (CandidateTable candidateTable : candidateTables) {
            CandidateTable newCand = GetSeedTableSets.getCandidateTable(queryTable, finalSeedTableSet, candidateTable.getTableBean());
            newCandList.add(newCand);
        }
        Comparator<CandidateTable> comparator = (t1, t2) -> {
            Double s1 = t1.getScore();
            Double s2 = t2.getScore();
            return s1.compareTo(s2);
        };
        newCandList.sort(comparator.reversed());

        //改变覆盖率
        SeedTableSet nSeedTableSet = GetSeedTableSets.getNewCoverRate(queryTable, seedTableSet, newCandList.get(0));
        //覆盖率收敛
        double oldCoverRate = seedTableSet.getCoverage();
        double newCoverRate = nSeedTableSet.getCoverage();
        if (newCoverRate - oldCoverRate <= 0.0001) {
            return nSeedTableSet;
        }
        return getResultTables(queryTable, newCandList, nSeedTableSet, count+1, coverRate);
    }
}
