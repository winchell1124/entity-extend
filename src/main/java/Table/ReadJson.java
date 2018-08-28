package Table;

import model.TableBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadJson {
	static CDict cd=new CDict();

	public static void main(String[] args)throws IOException
	{
		String path="DataSet\\Experiments\\test\\1438042988061.16_20150728002308-00000-ip-10-236-191-2_7114053_8.json";
		TableBean t=ReadJsonFile(path);
		System.out.println(t.getRowContent().get(0));
		System.out.println(t.getAbpath());
//		QueryTable qt=ReadQueryTable.readQT("querytable-book.xls","Sheet1");
//		List<String> query_entity = qt.getEntity();
//		List<String> webtable_entity =t.getEntity();
//		List<String> ent = new ArrayList<String>();
//		for (int i = 0; i < query_entity.size(); i++) {
//			double max = 0.0;
//			int temp = -1;
//			String queryentity = StringTransform.stringTransform(query_entity.get(i));
//			for (int j = 0; j < webtable_entity.size(); j++) {
//				String webentity = StringTransform.stringTransform(webtable_entity.get(j));
//				double sim = EditDistance.similarity(queryentity, webentity);
//				if (sim > max) {
//					max = sim;
//					temp = j;
//				}
//
//			}
//
//			if (max > 0.8) {
//				ent.add(webtable_entity.get(temp));
//				webtable_entity.remove(temp);
//				
//			}
//		}
//		List<String> l=new ArrayList<String>();
//		l.add("title The Secret Life of Bees");
//		l.add("title The Secret Life of Bees");
//		for(int i=0;i<l.size();i++)
//		{
//			String te=l.get(i).toLowerCase();
//			if(te.indexOf("title")==0)
//			{
//				l.remove(i);
//				l.add(i, te.substring("title".length()+1));
//			}
//			String te1=l.get(i).toLowerCase();
//			if(te1.indexOf("the")==0)
//			{
//				l.remove(i);
//				l.add(i, te1.substring("thle".length()+1));
//			}
//		}
//		System.out.println(l);
//		List<List<String>> columnContent=t.getColumnContent();
//		for(int i=0;i<columnContent.size();i++)
//		{
//			System.out.println(columnContent.get(i));
//		}
//		File file=new File(t.getAbpath());
//		file.delete();
//		System.out.println(t.getAbpath());
//		System.out.println(t.getId());
	}
	/**
	 * 读取一个json文件
	 * @param path:某个json文件的绝对路径路径
	 * @return
	 */
     public static TableBean ReadJsonFile(String path) {  
		
        File file = new File(path);  
        TableBean bean = new TableBean();
        BufferedReader reader = null;  
        //String laststr = "";  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;
            int lineCount = 0; //一行代表一个relation，即一个表格。
            while ((tempString = reader.readLine()) != null) {  
                try{
                tempString = "["+tempString+"]";
                //System.out.println(tempString);
                JSONArray jsonArray = JSONArray.fromObject(tempString);
                JSONArray array = (JSONArray) jsonArray.getJSONObject(0).get("relation");
                String hasHeader = jsonArray.getJSONObject(0).get("hasHeader").toString();
                
                if(hasHeader.equals("true")){  //判断是否有列标签
                	List<String> schema = new ArrayList<String>();
                	List<List<String>> columnContent = new ArrayList<List<String>>();
                	for(int i=0;i<array.size();i++){
                    	JSONArray tempAray = array.getJSONArray(i);
                    	List<String> column = new ArrayList<String>();
                    	for(int j=0;j<tempAray.size();j++){ 
                    		if(j == 0){ //每个relation的第一个元素为列标签，否则为列值
                    			schema.add(tempAray.getString(j));
                    		}else{
                    			column.add(tempAray.getString(j));
                    		}
                    	}
                    	columnContent.add(column); 	
                    }
                	int keyColumnIndex = Integer.parseInt(jsonArray.getJSONObject(0).get("keyColumnIndex").toString());
                	List<String> attributes=new ArrayList<String>();
                	for(int i=0;i<schema.size();i++)
                	{
                		if(i!=keyColumnIndex)
                		{
                			attributes.add(schema.get(i));
                		}
                	}
            		List<List<String>> rowContent=new ArrayList<List<String>>();
            		int rownum=columnContent.get(0).size();
            		for(int i=0;i<rownum;i++){ //i指代行数，从0开始
            			List<String> row=new ArrayList<String>();
            			for(int j=0;j<columnContent.size();j++){ //遍历每一列，将第i列的第num个值作为num行的第i个内容
            				List<String> column=columnContent.get(j);
            				row.add(j, column.get(i)); 
            			}
            			rowContent.add(i, row); 
            		}
            		List<String> entity = new ArrayList<String>();
            		String hasKeyColumn = jsonArray.getJSONObject(0).get("hasKeyColumn").toString();
            		//System.out.println(hasKeyColumn);
            		
            		if(hasKeyColumn.equals("true")){
            			//System.out.println(keyColumnIndex);
                		entity.addAll(columnContent.get(keyColumnIndex));
                		
                	}else{
                		entity=null;
                	}
            		
                	String pageTitle = jsonArray.getJSONObject(0).get("pageTitle").toString();
                	String url=jsonArray.getJSONObject(0).get("url").toString();
//                	String contextb=jsonArray.getJSONObject(0).get("textBeforeTable").toString();
//                	contextb=cd.removeStopWord(contextb); //将上下文去除停用词后保存
//                	String contexta=jsonArray.getJSONObject(0).get("textAfterTable").toString();
//                	contexta=cd.removeStopWord(contexta); //将上下文去除停用词后保存
                	
                	lineCount++;
                	String[] arrs=path.split("/");
                	String id = arrs[arrs.length-1];
                	
                	
                	bean.setSchema(schema);
                	bean.setColumnContent(columnContent);
                	bean.setRowContent(rowContent);
                	bean.setEntity(entity);
                	bean.setAttributes(attributes);
                	bean.setPageTitle(pageTitle);
                	bean.setId(id);
                	bean.setUrl(url);
                	
//                	bean.setContextb(contextb);
//                	bean.setContexta(contexta);
                	bean.setAbpath(path);
                	bean.setKeyColumnIndex(keyColumnIndex);
                	
                	
                	
                  }
                }catch(JSONException e){
                	continue;
                }
                
            }
            //System.out.println("读取表格数目："+ReadTableList.tableList.size());
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
//        Iterator<TableBean> it=tableList.iterator();  
//        while(it.hasNext())
//        {
//        	TableBean t=(TableBean)it.next();
//        	System.out.println(t.getPageTitle()+"\r\n"+t.getAbpath()+"\r\n"+t.getContexta()+"\r\n"+t.getContextb()+"\r\n");
//        	List<String> entity1=t.getEntity();
//        	//System.out.println(entity1.size());
//        	Iterator<String> it1=entity1.iterator();
//        	while(it1.hasNext())
//        	{
//        		String e=(String)it1.next();
//        		System.out.println(e);
//        	}
//        }
        return bean;
    } 

}
