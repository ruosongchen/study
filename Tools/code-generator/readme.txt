	//1、该项目为个人开发的代码生成器，辅助开发人员快速开发
	//2、数据模板采用freemark语法，例：
	//获取包名：${packageName}
	//list遍历：<#list columns as vo><#if vo.isPk==true >${vo.comments}</#if></#list>
	//3、最主要的数据存储在columns，ClassName，className，tableName，moduleName，packageName中，模板基本用这些数据填充就够了
	//4、封装了一些基础的代码，以便快速配置模板
	
	
	注：特殊符号转义 
	1、${}的转义：例：要输出'${obj.name}'，===>${r'${obj.name}'}，那个“r”是关键点
	2、自定义宏的结束符号不能出来的解决办法：比如我们自定义了个宏叫做：<@cfw.column></@cfw.column>，如果要显示源码，结束的那个符号"</@cfw.column>”是显示不出来的，这个时候可以采用字符转义的方式，比如：
	${'&lt;/@cfw.column''}，这样显示的结果就是：</@cfw.column>

	
	private static Map<String, Object> model = null;
	
	/**
	 * 获取数据模型
	 * 
	 * @param configs
	 * @param genTable
	 * @return
	 */
	private static Map<String, Object> createDataModel(Configs configs) {
		if (model != null) {
			return model;
		}
		model = new HashMap<String, Object>();

		/*************最主要的数据start***************/
		model.put("packageName", configs.getPackageName());//包名
		model.put("moduleName", configs.getModuleName());//模块名称
		model.put("className", CharUtil.toLowerCaseFirstOne(configs.getEntityName()));//类名小写开头
		model.put("ClassName", CharUtil.toUpCaseFirstOne(configs.getEntityName()));//类名大写开头
		model.put("tableName", configs.getTableName());//表名
		model.put("columns", configs.getColumEntities());//表字段实体列表（字段实体结构见实体示例）
		/*************最主要的数据end***************/
		//实体类
		model.put("entityCodeImport", CreateCodeUtils.createImportInfo(configs));//实体类导入
		model.put("entityCode", CreateCodeUtils.getJaveEntityCode(configs));//实体类属性及get，set方法
		//查询条件
		model.put("conditions", CreateCodeUtils.createConditons(configs));//查询条件实体列表（字段实体结构见实体示例）
		model.put("c1", CreateCodeUtils.createConditons1(configs));//(例：String con1,Integer con2,)
		model.put("c2", CreateCodeUtils.createConditons2(configs));//(例：String con1,Integer con2)与cons1比少个“,”
		model.put("c3", CreateCodeUtils.createConditons3(configs));//(例： con1, con2,)
		model.put("c4", CreateCodeUtils.createConditons4(configs));//(例：con1, con2)与cons3比少个“,”
		//主键
		model.put("idKey", CreateCodeUtils.getIdKey(configs));//主键（例：id1+id2）
		//sql语句
		model.put("insertSql", CreateCodeUtils.createInsertSql(configs));//插入语句
		model.put("updateSql", CreateCodeUtils.createUpdateSql(configs));//更新语句
		model.put("selectSql", CreateCodeUtils.createSelectSql(configs));//查询语句
		
		model.put("dbType", configs.getDbType());//数据库类型

		return model;
	}
	
	
            	
	实体属性列表如下：
	private String name; // 数据库列名 例：demo_field
	private String comments; // 字段描述
	private String dataType; // 数据库字段类型(例：varchar、text、int）
	private String columnType;//字段类型（例：varchar(20)，int（8））
	private String javaType; // JAVA类型（例：String，Long，Integer）
	private String javaName; // 字段属性名（实体类属性名称）例：demoField
	private Boolean isPk; // 是否主键（true：主键）
	private Boolean isNull; // 是否可为空（true：可为空；false：不为空）
	private Boolean isEdit; // 是否编辑字段（true：编辑字段）
	private Boolean isQuery = false; // 是否查询字段（true：查询字段）
	private String queryType; // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）
	private String showType; // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
	private String dictType; // 字典类型
	private Integer sort; // 排序（升序）
	private String defaultValue; // 默认值
	
	