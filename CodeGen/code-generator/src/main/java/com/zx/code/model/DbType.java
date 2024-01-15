package com.zx.code.model;

public enum DbType {
	MYSQL,ORACLE,SQLSERVER;
	
	public String getTableColumnSql(String tableName) {
		return "";
	}
	
}
