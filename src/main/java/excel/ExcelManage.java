package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.TableBean;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * 从excel读取数据/往excel中写入 excel有表头，表头每列的内容对应实体类的属性
 * 
 * @author nagsh
 * 
 */
public class ExcelManage {
	private HSSFWorkbook workbook = null;

	/**
	 * 判断文件是否存在.
	 * 
	 * @param fileDir
	 *            文件路径
	 * @return
	 */
	public boolean fileExist(String fileDir) {
		boolean flag = false;
		File file = new File(fileDir);
		flag = file.exists();
		return flag;
	}

	/**
	 * 判断文件的sheet是否存在.
	 * 
	 * @param fileDir
	 *            文件路径
	 * @param sheetName
	 *            表格索引名
	 * @return
	 */
	public boolean sheetExist(String fileDir, String sheetName) {
		boolean flag = false;
		File file = new File(fileDir);
		if (file.exists()) { // 文件存在
			// 创建workbook
			try {
				workbook = new HSSFWorkbook(new FileInputStream(file));
				// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
				HSSFSheet sheet = workbook.getSheet(sheetName);
				if (sheet != null)
					flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else { // 文件不存在
			flag = false;
		}

		return flag;
	}

	/**
	 * 创建新excel.
	 * 
	 * @param fileDir
	 *            excel的路径
	 * @param sheetName
	 *            要创建的表格索引
	 * @param titleRow
	 *            excel的第一行即表格头
	 */
	public void createExcel(String fileDir, String sheetName, String titleRow[],int keyColumn) {
		// 创建workbook
		workbook = new HSSFWorkbook();
		String temp=null;
		temp=titleRow[0];
		titleRow[0]=titleRow[keyColumn];
		titleRow[keyColumn]=temp;
		// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
		Sheet sheet1 = workbook.createSheet(sheetName);
		// 新建文件
		FileOutputStream out = null;
		try {
			// 添加表头
			Row row = workbook.getSheet(sheetName).createRow(0); // 创建第一行
			for (int i = 0; i < titleRow.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(titleRow[i]);
			}

			out = new FileOutputStream(fileDir);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 删除文件.
	 * 
	 * @param fileDir
	 *            文件路径
	 */
	public boolean deleteExcel(String fileDir) {
		boolean flag = false;
		File file = new File(fileDir);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				file.delete();
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 往excel中写入(已存在的数据无法写入).
	 * 
	 * @param fileDir
	 *            文件路径
	 * @param sheetName
	 *            表格索引
	 * @param
	 */
	public void writeToExcel(String fileDir, String sheetName, List<String> rowContent,int keyColumn) {
		// 创建workbook
		
		String temp=rowContent.get(0);
		rowContent.set(0, rowContent.get(keyColumn));
		rowContent.set(keyColumn, temp);
		File file = new File(fileDir);
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 流
		FileOutputStream out = null;
		HSSFSheet sheet = workbook.getSheet(sheetName);
		// 获取表格的总行数
		int rowCount = sheet.getLastRowNum() + 1; // 需要加一
		// 获取表头的列数
		int columnCount = sheet.getRow(0).getLastCellNum();
		try {
			Row row = sheet.createRow(rowCount); // 最新要添加的一行
			// 通过反射获得object的字段,对应表头插入
			// 获取该对象的class对象
			// 获得表头行对象
			HSSFRow titleRow = sheet.getRow(0);
			if (titleRow != null) {
				for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) { // 遍历表头
					// 执行该get方法,即要插入的数据
					Cell cell = row.createCell(columnIndex);
					cell.setCellValue(rowContent.get(columnIndex));
				}
			}

			out = new FileOutputStream(fileDir);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取excel表中的数据.
	 * 
	 * @param fileDir
	 *            文件路径
	 * @param sheetName
	 *            表格索引(EXCEL 是多表文档,所以需要输入表索引号，如sheet1)
	 */
	public TableBean readFromExcel(String fileDir, String sheetName) {
		// 创建workbook
		File file = new File(fileDir);
		List<String> schema=new ArrayList<String>();
		List<List<String>> columnContent=new ArrayList<List<String>>();  //按列存放表内容
		List<List<String>> rowContent=new ArrayList<List<String>>(); //按行存放表内容
		List<String> entity=new ArrayList<String>();
		List<String> attributes=new ArrayList<String>();
		String id=null; //表格文件名（用来建立表格标记）
		String abpath=null; //表格文件绝对路径
		int keyColumnIndex=-1; //指示主列索引号（若==-1，则无主列）
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TableBean result = new TableBean();
		// 获取该对象的class对象
		

		// 读取excel数据
		// 获得指定的excel表
		HSSFSheet sheet = workbook.getSheet(sheetName);
		// 获取表格的总行数
		int rowCount = sheet.getLastRowNum() + 1; // 需要加一
//		System.out.println("rowCount:" + rowCount);
		if (rowCount < 1) {
			return result;
		}
		keyColumnIndex=0;
		abpath=fileDir;
		String[] arrs=fileDir.split("/");
		id=arrs[arrs.length-1];
		// 获取表头的列数
		int columnCount = sheet.getRow(0).getLastCellNum();
		// 读取表头信息,确定需要用的方法名---set方法
		// 用于存储方法名
		String[] methodNames = new String[columnCount]; // 表头列数即为需要的set方法个数
		// 用于存储属性类型
		String[] fieldTypes = new String[columnCount];
		// 获得表头行对象
		HSSFRow titleRow = sheet.getRow(0);
		// 遍历
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) { // 遍历表头列
			String data = titleRow.getCell(columnIndex).toString(); // 某一列的内容
			schema.add(data);
		}
		// 逐行读取数据 从1开始 忽略表头
		for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
			// 获得行对象
			HSSFRow row = sheet.getRow(rowIndex);
			if (row != null) {
                 List<String> row1=new ArrayList<String>();
				// 获得本行中各单元格中的数据
				for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
					if(row.getCell(columnIndex)!=null)
					{
						String data = row.getCell(columnIndex).toString();
						row1.add(data);
					}
					else
						row1.add(" ");
					// 获取要调用方法的方法名
					
				}
				rowContent.add(row1);
			}
		}
		for(int columnIndex=0;columnIndex<columnCount;columnIndex++)
		{
			List<String> column=new ArrayList<String>();
			for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
				// 获得行对象
				HSSFRow row = sheet.getRow(rowIndex);
				if(row.getCell(columnIndex)!=null)
				{
					String data = row.getCell(columnIndex).toString();
					column.add(data);
				}
				else
					column.add(" ");
				
			}
			columnContent.add(column);
		}
		entity=columnContent.get(keyColumnIndex);
		for(int i=0;i<schema.size();i++)
    	{
    		if(i!=keyColumnIndex)
    		{
    			attributes.add(schema.get(i));
    		}
    	}
//		System.out.println(attributes);
		result.setAbpath(abpath);
		result.setAttributes(attributes);
		result.setColumnContent(columnContent);
		result.setEntity(entity);
		result.setId(id);
		result.setKeyColumnIndex(keyColumnIndex);
		result.setRowContent(rowContent);
		result.setSchema(schema);
		return result;
	}

	public static void main(String[] args) {
		
		// 判断文件是否存在
//		System.out.println(em.fileExist("DataSet\\Experiments\\test2.xls"));
		// 创建文件
//		TableBean t=ReadJson.ReadJsonFile("DataSet\\Experiments\\1438042988061.16_20150728002308-00051-ip-10-236-191-2_475459181_1.json");
//		List<String> l=t.getSchema();
//		int keyColumn=t.getKeyColumnIndex();
//		String title[] = l.toArray(new String[l.size()]);
//		em.createExcel("DataSet\\Experiments\\test2.xls", "sheet1", title,keyColumn);
//		// 判断sheet是否存在
//		System.out.println(em.sheetExist("DataSet\\Experiments\\test2.xls", "sheet1"));
		// 写入到excel
//		List<List<String>> RowContent=t.getRowContent();
//		for(int i=0;i<RowContent.size();i++)
//		{
//			em.writeToExcel("DataSet\\Experiments\\test2.xls", "sheet1", RowContent.get(i),keyColumn);
//		}
		// 读取excel
//
		ExcelManage em = new ExcelManage();
		TableBean table=em.readFromExcel("DataSet\\Experiments\\Books_Excel\\1438042988061.16_20150728002308-00296-ip-10-236-191-2_435282719_1.json.xls", "sheet1");
//		List<List<String>> rowContent=table.getRowContent();
//		for(int i=0;i<rowContent.size();i++)
//		{
//			System.out.println(rowContent.get(i));
//		}

		System.out.println(table.getEntity());
		System.out.println(table.getSchema());
//		for (int i = 0; i < list.size(); i++) {
//			User newUser = (User) list.get(i);
//			System.out.println(newUser.getId() + " " + newUser.getName() + " " + newUser.getPassword());
//		}
		// 删除文件
		// System.out.println(em.deleteExcel("E:/test2.xls"));
	}

}
