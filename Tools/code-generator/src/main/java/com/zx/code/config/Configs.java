package com.zx.code.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zx.code.model.ColumnEntity;
/**
 * @Description
 * @ClassName Configs
 * @Author zhengxin
 * @datetime 2023/7/18 9:57
 * @version: 1.0
 */
@ConfigurationProperties(prefix = "message")
public class Configs {
	
	/**
	 * 数据类型(默认mysql）
	 */
	private String dbType;
	/**
	 * 实体类型类型（jpa，hibernate，普通）
	 */
	private String entityType;
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 表说明（表注释）
	 */
	private String tableComment;
	/**
	 * 实体类名称
	 */
	private String entityName;
	/**
	 * 模板实体名类
	 */
	private String templateEntityName;
	/**
	 * 模板模块文件夹名称
	 */
	private String tempModelName = "businesspath";
	/**
	 * 查询条件
	 */
	private String conditions;
	/**
	 * 包名
	 */
	private String packageName;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 是否联合主键
	 */
	private Boolean isUnionKey = false;
	/**
	 * 模板路径
	 */
	private String templatePath;
	/**
	 * 代码生成路径
	 */
	private String codePath;
	
	
	private String author;
	/**
	 * 字段列表信息
	 */
	private List<ColumnEntity> columEntities;
	
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getTableName() {
		return tableName;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTemplateEntityName() {
		return templateEntityName;
	}

	public void setTemplateEntityName(String templateEntityName) {
		this.templateEntityName = templateEntityName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Boolean getIsUnionKey() {
		return isUnionKey;
	}

	public void setIsUnionKey(Boolean isUnionKey) {
		this.isUnionKey = isUnionKey;
	}

	public List<ColumnEntity> getColumEntities() {
		return columEntities;
	}

	public void setColumEntities(List<ColumnEntity> columEntities) {
		this.columEntities = columEntities;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public String getTempModelName() {
		return tempModelName;
	}

	public void setTempModelName(String tempModelName) {
		this.tempModelName = tempModelName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
