package com.zx.code.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zx.code.config.Configs;
import com.zx.code.model.ColumnEntity;
import org.springframework.util.StringUtils;

public class CreateCodeUtils {
	/**
	 * 实体类属性
	 * 
	 * @param configs
	 * @return
	 */
	public static String getJaveEntityCode(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder("");
		// 是联合主键
		if (configs.getIsUnionKey()) {
			List<ColumnEntity> columnsPk = new ArrayList<ColumnEntity>();
			List<ColumnEntity> columnsNew = new ArrayList<ColumnEntity>();
			// 判断是否是联合主键，则剔除
			for (ColumnEntity column : columns) {
				if (column.getIsPk()) {
					columnsPk.add(column);
				} else {
					columnsNew.add(column);
				}
			}
			appendNotes(sbd, configs, new ColumnEntity());
			sbd.append(createPrivateField(configs.getEntityName() + "Key",
					CharUtil.toLowerCaseFirstOne(configs.getEntityName() + "Key"), "联合主键"));
			sbd.append("\r\n");

			for (ColumnEntity column : columnsNew) {
				// 添加注解
				appendNotes(sbd, configs, column);
				sbd.append(createPrivateField(column.getJavaName(), column.getJavaType(), column.getComments()));
				sbd.append("\r\n");
			}

			sbd.append("\r\n");
			sbd.append("\r\n");

			// 生成get和set方法
			sbd.append(createGetAndSetMethod(configs.getEntityName() + "Key",
					CharUtil.toLowerCaseFirstOne(configs.getEntityName() + "Key")));
			for (ColumnEntity column : columnsNew) {
				sbd.append(createGetAndSetMethod(column.getJavaName(), column.getJavaType()));
			}

			createUnionKeyFile(columnsPk, configs);
		} else {
			// 生成私有属性
			for (ColumnEntity column : columns) {
				appendNotes(sbd, configs, column);
				sbd.append(createPrivateField(column.getJavaName(), column.getJavaType(), column.getComments()));
				sbd.append("\r\n");
			}
			sbd.append("\r\n");
			sbd.append("\r\n");
			// 生成get和set方法
			for (ColumnEntity column : columns) {
				sbd.append(createGetAndSetMethod(column.getJavaName(), column.getJavaType()));
			}
		}
		return sbd.toString();
	}

	/**
	 * 实体类属性
	 * 
	 * @param configs
	 * @return
	 */
	public static String createGetAndSetCode(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder("");
		// 生成get和set方法
		for (ColumnEntity column : columns) {
			sbd.append(createGetAndSetMethod(column.getJavaName(), column.getJavaType()));
		}
		return sbd.toString();
	}

	private static void createUnionKeyFile(List<ColumnEntity> columnsPk, Configs configs) {
		// TODO Auto-generated method stub

	}

	public static String createPrivateField(String javaName, String javaType, String comments) {
		StringBuilder sbd = new StringBuilder("");
		sbd.append("	private " + javaType + " " + javaName + ";");
		sbd.append("//" + comments);
		sbd.append("\r\n");
		return sbd.toString();
	}

	/**
	 * 根据字段名获取私有属性名称
	 * 
	 * @param columnName
	 * @return
	 */
	public static String getPrivateName(String columnName) {
		return CharUtil.lineToHump(columnName);
	}

	/**
	 * 根据字段名获取私有属性名称
	 * 
	 * @param columnName
	 * @return
	 */
	public static String getEntityName(String tableName) {
		int index = tableName.indexOf("_");
		if (index > 0 && index == 1) {
			tableName = tableName.substring(2, tableName.length());
		}
		return CharUtil.toUpCaseFirstOne(CharUtil.lineToHump(tableName));
	}

	/**
	 * 生成get和set方法
	 * 
	 * @param javaName 属性名称
	 * @param javaType 属性类型
	 * @return
	 */
	public static String createGetAndSetMethod(String javaName, String javaType) {
		StringBuilder sbd = new StringBuilder("");
		String getAndSetName = CharUtil.toUpCaseFirstOne(javaName);
		// get
		sbd.append("	public " + javaType + " get" + getAndSetName + "() {");
		sbd.append("\r\n");
		sbd.append("		return " + javaName + ";");
		sbd.append("\r\n");
		sbd.append("	}");
		sbd.append("\r\n");
		sbd.append("\r\n");
		// set
		sbd.append("	public void set" + getAndSetName + "(" + javaType + " " + javaName + ") {");
		sbd.append("\r\n");
		sbd.append("		this." + javaName + " = " + javaName + ";");
		sbd.append("\r\n");
		sbd.append("	}");
		sbd.append("\r\n");
		sbd.append("\r\n");
		return sbd.toString();
	}

	/**
	 * 生成导入信息
	 * 
	 * @param javaType
	 * @return
	 */
	public static String createImportInfo(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder("");
		Set<String> importSet = new HashSet<String>();
		if (configs.getEntityType().contains("JPA")) {
			if (configs.getIsUnionKey()) {
				importSet.add("import javax.persistence.EmbeddedId;");
			} else {
				importSet.add("import javax.persistence.Id;");
			}
		}
		for (ColumnEntity column : columns) {
			importSet.add(createImportColumn(column.getJavaType()));
		}
		for (String string : importSet) {
			sbd.append(string);
			sbd.append("\r\n");
		}
		return sbd.toString();
	}

	/**
	 * 生成字段类型的导入信息
	 * 
	 * @param javaType
	 * @return
	 */
	public static String createImportColumn(String javaType) {
		if ("Date".equals(javaType)) {
			return "import java.util.Date;";
		} else if ("BigDecimal".equals(javaType)) {
			return "import java.math.BigDecimal;";
		} else if ("Blob".equals(javaType)) {
			return "import java.sql.Blob;";
		}
		return "";
	}

	/**
	 * 添加注解
	 * 
	 * @param sbd
	 * @param configs
	 * @param column
	 */
	public static void appendNotes(StringBuilder sbd, Configs configs, ColumnEntity column) {
		if (column == null) {
			column = new ColumnEntity();
		}
		String columnName = column.getName();
		if (configs.getEntityType().contains("JPA")) {
			// 改造字段名称
			if (column.getIsPk() && !configs.getIsUnionKey()) {
				sbd.append("@Id");
				sbd.append("\r\n");
			}
			if (configs.getIsUnionKey() && !StringUtils.hasText(columnName)) {
				// 改造字段名称
				sbd.append("@EmbeddedId");
				sbd.append("\r\n");
				sbd.append("\r\n");
			}
			sbd.append("	@Column(name = \"" + columnName + "\")");
			sbd.append("\r\n");
		}
	}

	public static void main(String[] args) {

	}

	public static String createInsertSql(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder();
		sbd.append("insert into ").append(configs.getTableName()).append("(");
		int i = 0;
		for (ColumnEntity column : columns) {
			if (i > 0) {
				sbd.append(",");
			}
			sbd.append(column.getName());
			i++;
		}
		sbd.append(") values (");
		int j = 0;
		for (ColumnEntity column : columns) {
			if (j > 0) {
				sbd.append(",");
			}
			sbd.append(getPara(column.getJavaName()));
			j++;
		}
		sbd.append(")");
		return sbd.toString();
	}

	public static String createUpdateSql(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder();
		sbd.append("update ").append(configs.getTableName()).append(" set ");
		int i = 0;
		for (ColumnEntity column : columns) {
			if (i > 0) {
				sbd.append(",");
			}
			sbd.append(column.getName() + "=" + getPara(column.getJavaName()));
			i++;
		}
		sbd.append(" where 1!=1 ");
		return sbd.toString();
	}

	public static String createSelectSql(Configs configs) {
		List<ColumnEntity> columns = configs.getColumEntities();
		StringBuilder sbd = new StringBuilder();
		sbd.append("select ");
		int i = 0;
		for (ColumnEntity column : columns) {
			if (i > 0) {
				sbd.append(",");
			}
			sbd.append("t.");
			sbd.append(column.getName() + " as " + column.getJavaName());
			i++;
		}
		sbd.append(" from ").append(configs.getTableName());
		sbd.append(" t ");
		sbd.append("where 1=1 ");
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				sbd.append(" and t." + column.getName() + "=" + getPara(column.getJavaName()));
			}
		}
		return sbd.toString();
	}

	private static String getPara(String javaName) {
		return ":" + javaName;
	}

	public static List<ColumnEntity> createConditons(Configs configs) {
		List<ColumnEntity> results = new ArrayList<ColumnEntity>();
		List<ColumnEntity> columns = configs.getColumEntities();
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				results.add(column);
			}
		}
		return results;
	}

	public static String getIdKey(Configs configs) {
		String idKey = "";
		List<ColumnEntity> columns = configs.getColumEntities();
		int i = 0;
		for (ColumnEntity column : columns) {
			if (column.getIsPk()) {
				if (i > 0) {
					idKey += "+";
				}
				idKey += column.getJavaName();
				i++;
			}
		}
		return idKey;
	}

	public static List<ColumnEntity> getIdKeys(Configs configs) {
		List<ColumnEntity> reList = new ArrayList<ColumnEntity>();
		List<ColumnEntity> columns = configs.getColumEntities();
		for (ColumnEntity column : columns) {
			if (column.getIsPk()) {
				reList.add(column);
			}
		}
		return reList;
	}

	/**
	 * 
	 * @param configs
	 * @return (例：String con1,Integer con2,)
	 */
	public static String createConditons1(Configs configs) {
		StringBuilder sbd = new StringBuilder("");
		List<ColumnEntity> columns = configs.getColumEntities();
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				sbd.append(column.getJavaType());
				sbd.append(" " + column.getJavaName());
				sbd.append(",");
			}
		}
		return sbd.toString();
	}

	/**
	 * 
	 * @param configs
	 * @return (例：String con1,Integer con2)与cons1比少个“,”
	 */
	public static String createConditons2(Configs configs) {
		StringBuilder sbd = new StringBuilder("");
		List<ColumnEntity> columns = configs.getColumEntities();
		int count = 0;
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				if (count > 0) {
					sbd.append(",");
				}
				sbd.append(column.getJavaType());
				sbd.append(" " + column.getJavaName());
				count++;
			}
		}
		return sbd.toString();
	}

	/**
	 * 
	 * @param configs
	 * @return (例： con1, con2,)
	 */
	public static String createConditons3(Configs configs) {
		StringBuilder sbd = new StringBuilder("");
		List<ColumnEntity> columns = configs.getColumEntities();
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				sbd.append(" " + column.getJavaName());
				sbd.append(",");
			}
		}
		return sbd.toString();
	}

	/**
	 * 
	 * @param configs
	 * @return (例：con1, con2)与cons3比少个“,”
	 */
	public static String createConditons4(Configs configs) {
		StringBuilder sbd = new StringBuilder("");
		List<ColumnEntity> columns = configs.getColumEntities();
		int count = 0;
		for (ColumnEntity column : columns) {
			if (column.getIsQuery()) {
				if (count > 0) {
					sbd.append(",");
				}
				sbd.append(" " + column.getJavaName());
				count++;
			}
		}
		return sbd.toString();
	}

}
