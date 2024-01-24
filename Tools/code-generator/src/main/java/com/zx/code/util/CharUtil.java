package com.zx.code.util;


public class CharUtil {
	/** * 字符串大写换小写 * @param str * @return */
	public static String lowerCaseChar(String str) {
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] >= 'A' && chars[i] <= 'Z') {
				chars[i] = (char) (chars[i] + 32);
			}
		}

		return new String(chars);
	}

	/**
	 * 首字母Da写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 下滑下转驼峰
	 * 
	 * @param columnName
	 * @return
	 */
	public static String lineToHump(String columnName) {
		columnName = columnName.toLowerCase();
		if (columnName.endsWith("_")) {
			System.out.println("非法的命名：" + columnName);
			columnName = columnName.replaceAll("_", "");
			// throw new RuntimeException();
		} else if (columnName.contains("_")) {
			String[] tempName = columnName.split("_");
			String tempstr = "";
			for (int j = 1; j < tempName.length; j++) {
				tempName[j] = CharUtil.toUpCaseFirstOne(tempName[j]);
				tempstr = tempstr + tempName[j];
			}
			columnName = tempName[0] + tempstr;
		}
		return columnName;
	}

}
