package calculate;

import calculate.StringTypeValidation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnType {

	/**
	 * 判断某列内容是否为数字,必须每个属性值均为数值型时才断定该列为数值型
	 * @param column:需要判断的列内容集合
	 * @return
	 */
	public static boolean isNumeric(List<String> column){ 
		//
		String s=column.get(0).replaceAll("\\(.*\\)", "");
		if(StringTypeValidation.isRealNumber(s)==true){
			double n=0;
			//确定该列每一个属性值是否均为数值
			for(String ss:column){
				ss=ss.replaceAll("\\(.*\\)", "");
				if(StringTypeValidation.isRealNumber(ss)==false){
					continue;
				}else
					n++;
			}
			if(n/column.size()>0.8){  //确定该列全为数值
				return true; 
			}else
				return false;
		}
		else
			return false;
	}
	
	/**
	 * 判断某列内容是否为日期格式(只能判断yyyy-mm-dd/yyyy.mm.dd hh:MM:ss)
	 * @param column:需要判断的列内容集合
	 * @return
	 */
	public static boolean isDate(List<String> column){
		String s=column.get(0).replaceAll("\\(.*\\)", "");
		if(StringTypeValidation.isDate(s)==true){
			double n=0;
			//确定该列每一个属性值是否均为数值
			for(String ss:column){
				ss=ss.replaceAll("\\(.*\\)", "");
				if(StringTypeValidation.isDate(ss)==false){
					continue;
				}else
					n++;
			}
			
			if(n/column.size()>0.8){  //确定该列全为数值
				return true; 
			}else
				return false;
		}
		else
			return false;
	}
	
	
	public static void main(String[] args){
		List<String> l=new ArrayList<String>();
		int[] s={1976,
				1859,
				1884,
				1987,
				1928,
				1949,
				1945,
				1909,
				1970,
				1930,
				1791,
				1899,
				1922,
				1956,
				1943,
				1943,
				1924,
				1926,
				1982,
				1981,
				1931,
				1927,
				1843,
				1977,
				1954,
				1903,
				1987,
				1985,
				1984,
				1969,
				1967,
				1967,
				1844,
				1948,
				1946,
				1985,
				1980,
				1927,
				1957,
				1957,
				1605,
				1983,
				1927,
				1925,
				1927,
				1815,
				1911,
				1983,
				1988,
				1982,
				1956,
				1961,
				1985,
				1985,
				1988,
				1987,
				1986,
				1959,
				1982,
				1989,
				1970,
				1972,
				1990,
				1988,
				1978,
				1981,
				1973,
				1975,
				1967,
				1925,
				1974,
				1977,
				1854,
				1989,
				1983,
				1976,
				1978,
				1986,
				1989,
				1983,
				1984,
				1986,
				1967,
				1986,
				1977,
				1977,
				1973,
				1989,
				1989,
				1974,
				1988,
				1988,
				1949,
				1975,
				1971,
				1952,
				1964,
				1988,
				1819,
				1987,
				1847,
				1959,
				1984,
				1970,
				1981,
				1988,
				1987,
				1973,
				1974,
				1983,
				1992,
				1862,
				1981,
				1957,
				1960,
				1971,
				1949,
				1954,
				1979,
				1979,
				1920,
				1976,
				1966,
				1984,
				1981,
				1990,
				1974,
				1970,
				1989,
				1851,
				1980,
				1966,
				1978,
				1990,
				1988,
				1925,
				1971,
				1983,
				1972,
				1986,
				1940,
				1983,
				1987,
				1949,
				1817,
				1949,
				1937,
				1951,
				1980,
				1992,
				1838,
				1988,
				1984,
				1987,
				1940,
				1988,
				1984,
				1965,
				1950,
				1968,
				1987,
				1975,
				1972,
				1971,
				1985,
				1813,
				1980,
				1988,
				1922,
				1990,
				1977,
				1979,
				1976,
				1988,
				1969,
				1977,
				1942,
				1850,
				1905,
				1904,
};
		String[] str=new String[s.length];
		for(int i=0;i<s.length;i++)
		{
			str[i]=String.valueOf(s[i]);
		}
		l=Arrays.asList(str);
		System.out.println(isDate(l));
	}
}
