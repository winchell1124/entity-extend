package configTest;


import config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);
    public static void main(String args[]){
//        PropertiesConfig.configInit();
        System.out.println(PropertiesConfig.originalTablePath);
        System.out.println(PropertiesConfig.queryTablePath);
    }
}
