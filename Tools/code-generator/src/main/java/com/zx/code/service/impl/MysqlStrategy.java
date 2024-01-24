package com.zx.code.service.impl;

import org.springframework.stereotype.Service;

import com.zx.code.model.ColumnEntity;
import com.zx.code.service.ConverStrategy;

@Service("MYSQL")
public class MysqlStrategy implements ConverStrategy {
	
	/**
	 * 根据字段类型获取相应的java类型
	 * 
	 * @param dataType
	 * @return
	 */
	@Override
	public String converToJavaType(ColumnEntity columnEntity) {
		String dataType = columnEntity.getDataType();
		String javaType = "";
		if ("VARCHAR".equalsIgnoreCase(dataType) || "TEXT".equalsIgnoreCase(dataType)
				|| "CHAR".equalsIgnoreCase(dataType)) {
			javaType = "String";
		} else if ("int".equalsIgnoreCase(dataType) || "smallint".equalsIgnoreCase(dataType)) {
			javaType = "Integer";
		} else if ("tinyint".equalsIgnoreCase(dataType)) {
			javaType = "Byte";
		} else if ("BIGINT".equalsIgnoreCase(dataType)) {
			javaType = "Long";
		} else if ("DOUBLE".equalsIgnoreCase(dataType)) {
			javaType = "Double";
		} else if ("BIT".equalsIgnoreCase(dataType)) {
			javaType = "Boolean";
		} else if ("FLOAT".equalsIgnoreCase(dataType)) {
			javaType = "Float";
		} else if ("DATE".equalsIgnoreCase(dataType) || "TIMESTAMP".equalsIgnoreCase(dataType)
				|| "DATETIME".equalsIgnoreCase(dataType)) {
			javaType = "Date";
		} else if ("decimal".equalsIgnoreCase(dataType)) {
			javaType = "BigDecimal";
		} else if ("blob".equalsIgnoreCase(dataType)) {
			javaType = "Blob";
		}else {
			javaType = "String";
		}
		return javaType;
	}
	
	
	@Override
	public String getColumnInfoSql() {
		return "select COLUMN_NAME as name,DATA_TYPE as dataType,CASE COLUMN_KEY WHEN 'pri' THEN TRUE ELSE FALSE END AS isPk,COLUMN_COMMENT as comments,(CASE WHEN IS_NULLABLE = 'YES' THEN TRUE ELSE FALSE END) AS isNull" + 
				",COLUMN_TYPE AS columnType, COLUMN_DEFAULT AS defaultValue from information_schema.columns where table_schema =(select database()) and table_name = :tableName";
	}


	@Override
	public String getTableCommentSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
