package calculate;

import model.QueryTable;
import model.TableBean;
import similarity.EditDistance;

import java.util.List;

public class CreateDataSource {


    public static boolean getWebTables(QueryTable queryTable, TableBean webTables) {

        List<String> queryEntity = queryTable.getEntity();
        List<String> webEntity = webTables.getEntity();
        if (webEntity != null && queryEntity != null) {
            int m = webEntity.size();
            int n = queryEntity.size();
            for (int k = 0; k < m; k++) {
                for (int l = 0; l < n; l++) {
                    //System.out.println("column1:"+column1.get(i)+" column2:"+column2.get(j));
                    //若两个属性列属性名为空，则视为相同
                    if ((webEntity.get(k) != " ") && (queryEntity.get(l) != " ") && EditDistance.similarity(webEntity.get(k).toLowerCase(), queryEntity.get(l).toLowerCase()) >= 0.7) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}