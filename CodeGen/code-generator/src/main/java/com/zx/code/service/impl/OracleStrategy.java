package com.zx.code.service.impl;


import com.zx.code.model.ColumnEntity;
import com.zx.code.service.ConverStrategy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("ORACLE")
public class OracleStrategy extends BaseStrategy implements ConverStrategy {
	
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
		sbf.append(",(CASE WHEN t.NULLABLE = 'Y' THEN '1' ELSE '0' END) AS isNull");
		sbf.append(",(t.COLUMN_ID * 10) AS sort");
		sbf.append(",t.DATA_TYPE as dataType");
		sbf.append(",c.COMMENTS AS comments");
		sbf.append(",CASE WHEN au.constraint_type='P' THEN 0 ELSE 1 END AS isPk");
		//oracle的number类型比较特殊，需要判断小数点及数据长度
		sbf.append(",decode(t.DATA_TYPE,'DATE',t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',");
		sbf.append("'VARCHAR2', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',");
		sbf.append("'VARCHAR', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',");
		sbf.append("'NVARCHAR2', t.DATA_TYPE || '(' || t.DATA_LENGTH/2 || ')',");
		sbf.append("		'CHAR', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',");
		sbf.append("		'NUMBER',t.DATA_TYPE || (nvl2(t.DATA_PRECISION,nvl2(decode(t.DATA_SCALE,0,null,t.DATA_SCALE),");
		sbf.append("'(' || t.DATA_PRECISION || ',' || t.DATA_SCALE || ')', ");
		sbf.append("'(' || t.DATA_PRECISION || ')'),'(18)')),t.DATA_TYPE) AS columnType");
		
		sbf.append(" FROM user_tab_columns t ");
		sbf.append(" INNER JOIN user_col_comments c ON t.TABLE_NAME = c.table_name AND t.COLUMN_NAME = c.column_name ");
		sbf.append(" LEFT JOIN user_cons_columns cu ON t.TABLE_NAME = cu.table_name AND t.COLUMN_NAME = cu.column_name ");
		sbf.append(" INNER JOIN user_constraints au ON cu.constraint_name = au.constraint_name ");
		sbf.append(" WHERE 1=1 AND t.TABLE_NAME=:tableName");
		sbf.append(" AND au.table_name =:tableName");
		sbf.append(" ORDER BY t.COLUMN_ID");
		sbf.append("");
		sbf.append("");
		sbf.append("");
		return sbf.toString();
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
		// TODO Auto-generated method stub
		return null;
	}

}
