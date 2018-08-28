package Table;

import excel.ExcelManage;
import model.QueryTable;
import model.TableBean;

public class ReadQueryTable {
	
	public static QueryTable readQT(String path, String sheetName)
	{
		QueryTable qt=new QueryTable();
		ExcelManage em=new ExcelManage();
		TableBean table=em.readFromExcel(path, sheetName);
		qt.setAttributes(table.getAttrubutes());
		qt.setEntity(table.getEntity());
		qt.setRowContent(table.getRowContent());
		qt.setSchema(table.getSchema());
		return qt;
	}
}