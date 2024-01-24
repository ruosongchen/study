package com.zx.code.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zx.code.model.ColumnEntity;
import com.zx.code.service.ConverStrategy;

@Service("DAMENG")
public class DMStrategy extends BaseStrategy implements ConverStrategy {
	
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
		String sql = "select a.table_name as tableName, b.DATA_TYPE as dataType,concat(b.DATA_TYPE,'(',b.DATA_LENGTH,')') as columnType,a.comments,a.column_name as name,b.DATA_LENGTH,decode(b.NULLABLE,'Y',1,0) as isNull,(select 1 from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and au.table_name =:tableName and a.column_name=cu.column_name) isPk From user_col_comments a, user_tab_columns b where a.table_name = b.TABLE_NAME and a.column_name = b.COLUMN_NAME and a.table_name=:tableName";
		return sql;
	}
	
	/**
	 * 根据字段类型获取相应的java类型
	 * 
	 * @param dataType
	 * @return
	 */
	@Override
	public String converToJavaType(ColumnEntity columnEntity) {
		String dataType = columnEntity.getDataType();
		if ("NUMBER".equalsIgnoreCase(dataType)) {
			// 如果是浮点型
			String[] ss = StringUtils.split(substringBetween(columnEntity.getColumnType(), "(", ")"), ",");
			if (ss != null && ss.length == 2 && Integer.parseInt(ss[1]) > 0) {
				return "Double";
			} else if (ss != null && ss.length == 1 && Integer.parseInt(ss[0]) <= 10) {// 如果是整形
				return "Integer";
			} else {// 长整形
				return "Long";
			}
		} else {
			return super.converToJavaType(columnEntity);
		}
	}

	private String substringBetween(final String str, final String open, final String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != -1) {
			final int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	@Override
	public String getTableCommentSql() {
		String sql ="select table_name as tableName,comments from USER_TAB_COMMENTS where table_name=:tableName";
		return sql;
	}

}
