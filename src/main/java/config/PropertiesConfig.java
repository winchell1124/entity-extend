package config;

import utils.PropertieUtil;

public class PropertiesConfig {
    public static String originalTablePath = PropertieUtil.getProperty("original.table.path");
    public static String queryTablePath = PropertieUtil.getProperty("query.table.path");

//    public static void configInit() {
//        originalTablePath = PropertieUtil.getProperty("original.table.path");
//        queryTablePath = PropertieUtil.getProperty("query.table.path");
//    }
//    static {
//        configInit();
//    }

}
