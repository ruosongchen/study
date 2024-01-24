package com.zx.code.service.impl;

import org.springframework.stereotype.Service;

import com.zx.code.service.ConverStrategy;

@Service("SQLSERVER")
public class SqlserverStrategy extends BaseStrategy implements ConverStrategy {

	protected String[] stringTypes = new String[] {"VARCHAR","VARCHAR2","NVARCHAR2","CHAR"};
	protected String[] integerTypes = new String[] { "int", "smallint" };
	protected String[] boolTypes = new String[] { "BIT" };
	protected String[] longTypes = new String[] { "BIGINT" };
	protected String[] doubleTypes = new String[] { "DOUBLE" };
	protected String[] floatTypes = new String[] { "Float" };
	protected String[] dateTypes = new String[] { "DATE", "TIMESTAMP", "DATETIME" };
	protected String[] bigDecimalTypes = new String[] { "BigDecimal" };
	protected String[] byteTypes = new String[] { "tinyint" };
	
	@Override
	public String getColumnInfoSql() {
		StringBuilder sbf = new StringBuilder();
		sbf.append("select ");
		sbf.append("t.COLUMN_NAME AS name");
		sbf.append(",(CASE WHEN t.IS_NULLABLE = 'YES' THEN '1' ELSE '0' END) AS isNull");
		sbf.append(",(t.ORDINAL_POSITION * 10) AS sort");
		sbf.append(",t.DATA_TYPE as dataType");
		sbf.append(",isnull(g.[value], '') AS comments");
		sbf.append(",CASE WHEN k.COLUMN_NAME is NULL THEN 0 ELSE 1 END AS isPk");
		sbf.append(" FROM INFORMATION_SCHEMA.COLUMNS t ");
		sbf.append(" INNER JOIN sys.sysobjects o ON t.TABLE_NAME=o.name AND SCHEMA_NAME(o.uid)=t.TABLE_SCHEMA");
		sbf.append(" LEFT JOIN sys.extended_properties g ON o.id = g.major_id");
		sbf.append(" AND t.ORDINAL_POSITION = g.minor_id AND g.name='MS_Description'");
		sbf.append(" LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE k ON k.TABLE_NAME=o.name AND t.COLUMN_NAME=k.COLUMN_NAME ");
		sbf.append(" WHERE t.TABLE_SCHEMA = (SCHEMA_NAME()) AND t.TABLE_NAME=:tableName");
		sbf.append(" ORDER BY t.ORDINAL_POSITION");
		sbf.append("");
		sbf.append("");
		sbf.append("");
		return sbf.toString();
	}

	@Override
	public String getTableCommentSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
