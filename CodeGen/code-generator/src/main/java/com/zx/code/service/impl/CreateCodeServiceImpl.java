package com.zx.code.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zx.code.config.Configs;
import com.zx.code.dao.JdbcColumnInfo;
import com.zx.code.model.ColumnEntity;
import com.zx.code.model.DbType;
import com.zx.code.service.ConverStrategy;
import com.zx.code.service.CreateCodeService;
import com.zx.code.util.CreateCodeUtils;
import com.zx.code.util.FreeMarkUtils;

@Service
public class CreateCodeServiceImpl implements CreateCodeService {

	@Autowired
	private Configs configs;

	@Autowired
	private Map<String, ConverStrategy> strategys;

	@Autowired
	private JdbcColumnInfo jdbcColumnInfo;

	@Override
	public void createCode() {
		System.out.println("生成模板开始。。。。。。。。。。。。。。。。。。");
		initConfig();
		FreeMarkUtils.templateToFile(configs);
		System.out.println("生成模板结束。。。。。。。。。。。。。。。。。。");
	}

	/**
	 * 初始化配置
	 */
	private void initConfig() {
		String dbType = !StringUtils.hasText(configs.getDbType()) ? DbType.MYSQL.name() : configs.getDbType();
		String tableName = configs.getTableName();
		if (!StringUtils.hasText(configs.getEntityName())) {
			configs.setEntityName(CreateCodeUtils.getEntityName(tableName));
		}
		// 1、初始化字段信息
		String sql = strategys.get(dbType).getColumnInfoSql();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", configs.getTableName());

		if (StringUtils.isEmpty(configs.getTableComment())) {
			// 1、初始化表注释
			String tableCommentSql = strategys.get(dbType).getTableCommentSql();
			if (StringUtils.hasText(tableCommentSql)) {
				configs.setTableComment(jdbcColumnInfo.getTableComment(tableCommentSql, params));
			}
		}

		List<ColumnEntity> columEntities = jdbcColumnInfo.findList(sql, params);
		int countPk = 0;
		for (ColumnEntity columnEntity : columEntities) {
			if(!StringUtils.hasText(columnEntity.getComments())) {
				columnEntity.setComments("");
			}
			// 设置字段的java类型
			columnEntity.setJavaType(strategys.get(dbType).converToJavaType(columnEntity));
			// 设置属性名称
			columnEntity.setJavaName(CreateCodeUtils.getPrivateName(columnEntity.getName()));
			if (StringUtils.hasText(configs.getConditions())) {
				if (configs.getConditions().contains(columnEntity.getName())) {
					columnEntity.setIsQuery(true);
				}
			}
			if (columnEntity.getIsPk() != null && columnEntity.getIsPk()) {
				countPk++;
			} else {
				columnEntity.setIsPk(false);
			}
		}
		if (countPk >= 2) {
			// 联合主键
			configs.setIsUnionKey(true);
		}
		configs.setColumEntities(columEntities);
	}

}
