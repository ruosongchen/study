package com.zx.code.model;

public class ColumnEntity {

	private String name; // 数据库列名 例：demo_field
	private String comments; // 字段描述
	private String dataType; // 数据库字段类型(例：varchar、text、int）
	private String columnType;// 字段类型（例：varchar(20)，int（8））
	private String javaType; // JAVA类型（例：String，Long，Integer,Date）
	private String javaName; // 字段属性名（实体类属性名称）例：demoField
	private Boolean isPk; // 是否主键（1：主键）
	private Boolean isNull; // 是否可为空（1：可为空；0：不为空）
	private Boolean isEdit; // 是否编辑字段（1：编辑字段）
	private Boolean isQuery = false; // 是否查询字段（1：查询字段）
	private String queryType; // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）
	private String showType; // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
	private String dictType; // 字典类型
	private Integer sort; // 排序（升序）
	private String defaultValue; // 默认值

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJavaName() {
		return javaName;
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}

	public Boolean getIsPk() {
		return isPk;
	}

	public void setIsPk(Boolean isPk) {
		this.isPk = isPk;
	}

	public Boolean getIsNull() {
		return isNull;
	}

	public void setIsNull(Boolean isNull) {
		this.isNull = isNull;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Boolean getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(Boolean isQuery) {
		this.isQuery = isQuery;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
