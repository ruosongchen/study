package com.zx.code.service;

import com.zx.code.model.ColumnEntity;

/**
 * 转换策略
 * @author admin
 *
 */
public interface ConverStrategy {
	
	/**
	 * 获取表字段信息
	 * @param columnType
	 * @return
	 */
	public String getColumnInfoSql();

	/**
	 * 数据库类型映射java类型
	 * @param columnEntity
	 * @return
	 */
	public String converToJavaType(ColumnEntity columnEntity);
	
	/**
	 * 获取表注释sql
	 */
	public String getTableCommentSql();

}
