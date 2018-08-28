package Table;

import model.TableBean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TableProcess {
    static CDict cd = new CDict();

    public static TableBean StringToTable(String json) {
        TableBean bean = new TableBean();
//        JSONArray jsonArray = JSONArray.fromObject(json);
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray array = (JSONArray) jsonObject.get("relation");
        String hasHeader = jsonObject.get("hasHeader").toString();

        if (hasHeader.equals("true")) {  //判断是否有列标签
            List<String> schema = new ArrayList<String>();
            List<List<String>> columnContent = new ArrayList<List<String>>();
            for (int i = 0; i < array.size(); i++) {
                JSONArray tempAray = array.getJSONArray(i);
                List<String> column = new ArrayList<String>();
                for (int j = 0; j < tempAray.size(); j++) {
                    if (j == 0) { //每个relation的第一个元素为列标签，否则为列值
                        schema.add(tempAray.getString(j));
                    } else {
                        column.add(tempAray.getString(j));
                    }
                }
                columnContent.add(column);
            }
            int keyColumnIndex = Integer.parseInt(jsonObject.get("keyColumnIndex").toString());
            List<String> attributes = new ArrayList<String>();
            for (int i = 0; i < schema.size(); i++) {
                if (i != keyColumnIndex) {
                    attributes.add(schema.get(i));
                }
            }
            List<List<String>> rowContent = new ArrayList<List<String>>();
            int rownum = columnContent.get(0).size();
            for (int i = 0; i < rownum; i++) { //i指代行数，从0开始
                List<String> row = new ArrayList<String>();
                for (int j = 0; j < columnContent.size(); j++) { //遍历每一列，将第i列的第num个值作为num行的第i个内容
                    List<String> column = columnContent.get(j);
                    row.add(j, column.get(i));
                }
                rowContent.add(i, row);
            }
            List<String> entity = new ArrayList<String>();
            String hasKeyColumn = jsonObject.get("hasKeyColumn").toString();
            //System.out.println(hasKeyColumn);

            if (hasKeyColumn.equals("true")) {
                //System.out.println(keyColumnIndex);
                entity.addAll(columnContent.get(keyColumnIndex));

            } else {
                entity = null;
            }

            String pageTitle = jsonObject.get("pageTitle").toString();
            String url = jsonObject.get("url").toString();
            String contextb = jsonObject.get("textBeforeTable").toString();
            //暂时不用上下文
//            contextb = cd.removeStopWord(contextb); //将上下文去除停用词后保存
            String contexta = jsonObject.get("textAfterTable").toString();
//            contexta = cd.removeStopWord(contexta); //将上下文去除停用词后保存

            bean.setSchema(schema);
            bean.setColumnContent(columnContent);
            bean.setRowContent(rowContent);
            bean.setEntity(entity);
            bean.setAttributes(attributes);
            bean.setPageTitle(pageTitle);
            bean.setId("");
            bean.setUrl(url);

            bean.setContextb(contextb);
            bean.setContexta(contexta);
            bean.setAbpath("");
            bean.setKeyColumnIndex(keyColumnIndex);

        }
        return bean;
    }
}
