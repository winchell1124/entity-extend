package process;

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
import org.stringtemplate.v4.ST;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Process {

    private static final Logger logger = LoggerFactory.getLogger(Process.class);

    public static void main(String[] args) {

        logger.info("--------read queryTable---------");
        final QueryTable queryTable = ReadQueryTable.readQT(PropertiesConfig.queryTablePath, "sheet1");
        SparkConf conf = new SparkConf();
        JavaSparkContext sc = new JavaSparkContext(conf);
        //读取json文件并转化为TableBean类型
        logger.info("--------read json file---------");
        JavaRDD<String> input = sc.textFile(PropertiesConfig.originalTablePath, 1);
        logger.info("--------json turn to table---------");
        JavaRDD<TableBean> originalRDD = input.map((String json) -> {
            TableBean tableBean = TableProcess.StringToTable(json);
            return tableBean;
        });

        //筛选和查询表有相同实体的网络表
        logger.info("--------filter the same entity---------");
        JavaRDD<TableBean> tableRDD = originalRDD.filter((TableBean tableBean) -> {
            return CreateDataSource.getWebTables(queryTable, tableBean);
        });

        //计算每个表的分数和匹配的元素并排序
        logger.info("--------计算每个表的分数和匹配的元素并排序---------");
        JavaRDD<CandidateTable> candRDD = tableRDD.map(new Function<TableBean, CandidateTable>() {
            public CandidateTable call(TableBean tableBean) throws Exception {
                CandidateTable cand = GetFirstCandidateTable.getTableScore(queryTable, tableBean);
                cand.setTableBean(tableBean);
                return cand;
            }
        }).filter((CandidateTable candidateTable) -> {
            if (candidateTable.getScore() >= 0.01)
                return true;
            return false;
        }).sortBy(new Function<CandidateTable, Double>() {
            public Double call(CandidateTable candidateTable) throws Exception {
                return candidateTable.getScore();
            }
        }, false, 1);
//        List<CandidateTable> candidateTableList = candRDD.collect();
//        Comparator<CandidateTable> comparator = (t1, t2) -> {
//            Double s1 = t1.getScore();
//            Double s2 = t2.getScore();
//            return s1.compareTo(s2);
//        };
//        candidateTableList.sort(comparator.reversed());

        //和查询表匹配分数最高的作为第一个种子表
//        CandidateTable seedTable = candidateTableList.get(0);
        CandidateTable seedTable = candRDD.first();
        logger.info("-----------first seedTable  : " + seedTable.getTableBean().getEntity().get(0) + seedTable.getTableBean().getEntity().get(1));
        //种子集
        SeedTableSet seedSet = new SeedTableSet();
        seedSet.setSeedTables(new ArrayList<>());
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
        SeedTableSet resultSet = getResultTables(queryTable, candRDD, seedSet, 1, 0.8);
//        SeedTableSet resultSet = getResultTables(queryTable, candidateTableList, seedSet, 1, 0.8);
        //计算结果保存为excel
        CreateAugTable.createAugTable(PropertiesConfig.resultStorePath, queryTable, resultSet);
    }

    //通过spark进行迭代
    public static SeedTableSet getResultTables(final QueryTable queryTable, JavaRDD<CandidateTable> candRDD, SeedTableSet seedTableSet, int count, double coverRate) {
        logger.info(String.format("---------cycle count is: %d     cover rate is: %f", count, seedTableSet.getCoverage()));
        if (seedTableSet.getCoverage() >= coverRate) {
            return seedTableSet;
        }
//        if (seedTableSet.getSeedTables().size() > 20) {
//            return seedTableSet;
//        }
        final SeedTableSet finalSeedTableSet = seedTableSet;
        JavaRDD<CandidateTable> nCandRDD = candRDD.map(new Function<CandidateTable, CandidateTable>() {
            public CandidateTable call(CandidateTable v1) throws Exception {
                CandidateTable candidateTable = GetSeedTableSets.getCandidateTable(queryTable, finalSeedTableSet, v1.getTableBean());
                return candidateTable;
            }
        });

//        candRDD.mapToPair(new PairFunction<CandidateTable, String, Double>() {
//            public Tuple2<String, Double> call(CandidateTable candidateTable) throws Exception {
//                return new Tuple2<String, Double>(candidateTable.getTableBean().getEntity().get(0), candidateTable.getScore());
//            }
//        }).repartition(1).saveAsTextFile("/data/hwc/outputTest/" + count);

        CandidateTable candidateTable = nCandRDD.sortBy(new Function<CandidateTable, Double>() {
            public Double call(CandidateTable v1) throws Exception {
                return v1.getScore();
            }
        }, false, 1).first();
        //改变覆盖率
        SeedTableSet nSeedTableSet = GetSeedTableSets.getNewCoverRate(queryTable, seedTableSet, candidateTable);

        //覆盖率收敛
        double oldCoverRate = seedTableSet.getCoverage();
        double newCoverRate = nSeedTableSet.getCoverage();
        if (newCoverRate - oldCoverRate <= 0.0001) {
            return nSeedTableSet;
        }
        return getResultTables(queryTable, nCandRDD, nSeedTableSet, count + 1, coverRate);
    }

    //通过list，单节点迭代
    public static SeedTableSet getResultTables(final QueryTable queryTable, List<CandidateTable> candidateTables, SeedTableSet seedTableSet, int count, double coverRate) {
        System.out.println(String.format("---------cycle count is: %d     cover rate is: %f", count, seedTableSet.getCoverage()));
        logger.info(String.format("---------cycle count is: %d     cover rate is: %f", count, seedTableSet.getCoverage()));
        if (seedTableSet.getCoverage() >= coverRate) {
            return seedTableSet;
        }
        if (seedTableSet.getSeedTables().size() >= 2) {
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
        return getResultTables(queryTable, newCandList, nSeedTableSet, count + 1, coverRate);
    }
}
