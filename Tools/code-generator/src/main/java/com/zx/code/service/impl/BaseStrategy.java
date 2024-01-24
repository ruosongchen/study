package com.zx.code.service.impl;

import com.zx.code.model.ColumnEntity;

public class BaseStrategy {

	protected String[] stringTypes = new String[] { "VARCHAR", "TEXT", "CHAR" };
	protected String[] integerTypes = new String[] { "int", "smallint" };
	protected String[] boolTypes = new String[] { "BIT" };
	protected String[] longTypes = new String[] { "BIGINT" };
	protected String[] doubleTypes = new String[] { "DOUBLE" };
	protected String[] floatTypes = new String[] { "Float" };
	protected String[] dateTypes = new String[] { "DATE", "TIMESTAMP", "DATETIME" };
	protected String[] bigDecimalTypes = new String[] { "BigDecimal" };
	protected String[] byteTypes = new String[] { "tinyint" };

	protected boolean checkContains(String dataType, String[] types) {
		for (String type : types) {
			if (type.equalsIgnoreCase(dataType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据字段类型获取相应的java类型
	 * 
	 * @param dataType
	 * @return
	 */
	public String converToJavaType(ColumnEntity columnEntity) {
		String dataType = columnEntity.getDataType();
		String javaType = "";
		if (checkContains(dataType, stringTypes)) {
			javaType = "String";
		} else if (checkContains(dataType, integerTypes)) {
			javaType = "Integer";
		} else if (checkContains(dataType, byteTypes)) {
			javaType = "Byte";
		} else if (checkContains(dataType, longTypes)) {
			javaType = "Long";
		} else if (checkContains(dataType, doubleTypes)) {
			javaType = "Double";
		} else if (checkContains(dataType, boolTypes)) {
			javaType = "Boolean";
		} else if (checkContains(dataType, floatTypes)) {
			javaType = "Float";
		} else if (checkContains(dataType, dateTypes)) {
			javaType = "Date";
		} else if (checkContains(dataType, bigDecimalTypes)) {
			javaType = "BigDecimal";
		} else {
			javaType = "String";
		}
		return javaType;
	}

}
