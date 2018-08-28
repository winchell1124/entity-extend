package config;

import utils.PropertieUtil;

public class PropertiesConfig {
    public static String originalTablePath = PropertieUtil.getProperty("original.table.path");
    public static String queryTablePath = PropertieUtil.getProperty("query.table.file");
    public static String stopwordPath = PropertieUtil.getProperty("stop.word.path");
    public static String resultStorePath = PropertieUtil.getProperty("result.store.path");
//    public static void configInit() {
//        originalTablePath = PropertieUtil.getProperty("original.table.path");
//        queryTablePath = PropertieUtil.getProperty("query.table.path");
//    }
//    static {
//        configInit();
//    }

}
