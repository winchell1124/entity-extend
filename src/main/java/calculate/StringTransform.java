package calculate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTransform {

	// 判断一个字符串是否都为数字
	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	public static String stringTransform(String str) {
		if (isDigit(str)) {
			try
			{
				String s1=NumberText.getInstance(NumberText.Lang.English).getText(Integer.parseInt(str));
				return s1;
			}
			catch(NumberFormatException nfe)
			{}
			return str;

		}
		if (hasDigit(str)) {
			try
			{
				String temp = getNumbers(str);
				return temp;
			}
			catch(NumberFormatException nfe)
			{}
			return str;
		} 
		else
			return str;
	}

	// 截取数字
	public static String getNumbers(String content) {
		StringBuilder sb = new StringBuilder();
		int start = 0, end = 0, flag = 0;
		while (end < content.length()) {
			start = end;
			while (end < content.length() && !Character.isDigit(content.charAt(end))) {
				end++;
				flag = 1;

			}
			if (flag == 1) {
				sb.append(content.substring(start, end) + " ");

			}
			start = end;
			while (end < content.length() && Character.isDigit(content.charAt(end))) {
				end++;
				flag = -1;

			}

			if (flag == -1) {

				String temp = NumberText.getInstance(NumberText.Lang.English)
						.getText(Integer.parseInt(content.substring(start, end)));

				sb.append(temp + " ");

			}
		}
		return sb.toString();
	}

	// 截取非数字
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
	// 判断一个字符串是否含有数字

	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}
	public static void main(String[] args)
	{
		int t=0;
		for(int i=0;i<2;i++)
		{
			if(t==0)
			{
				t=1;
				break;
			}
			System.out.println("123");
		}
	}

}
