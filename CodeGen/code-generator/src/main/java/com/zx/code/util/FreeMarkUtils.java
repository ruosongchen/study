package com.zx.code.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zx.code.config.Configs;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkUtils {
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

		/************* 最主要的数据start ***************/
		model.put("packageName", configs.getPackageName());// 包名
		model.put("moduleName", configs.getModuleName());// 模块名称
		model.put("className", CharUtil.toLowerCaseFirstOne(configs.getEntityName()));// 类名小写开头
		model.put("ClassName", CharUtil.toUpCaseFirstOne(configs.getEntityName()));// 类名大写开头
		model.put("tableName", configs.getTableName());// 表名
		model.put("columns", configs.getColumEntities());// 表字段实体列表（字段实体结构见实体示例）
		/************* 最主要的数据end ***************/
		// 实体类
		model.put("entityCodeImport", CreateCodeUtils.createImportInfo(configs));// 实体类导入
		model.put("entityCode", CreateCodeUtils.getJaveEntityCode(configs));// 实体类属性及get，set方法
		model.put("getAndSetCode", CreateCodeUtils.createGetAndSetCode(configs));// 实体类属性及get，set方法
		// 查询条件
		model.put("conditions", CreateCodeUtils.createConditons(configs));// 查询条件实体列表（字段实体结构见实体示例）
		model.put("c1", CreateCodeUtils.createConditons1(configs));// (例：String con1,Integer con2,)
		model.put("c2", CreateCodeUtils.createConditons2(configs));// (例：String con1,Integer con2)与cons1比少个“,”
		model.put("c3", CreateCodeUtils.createConditons3(configs));// (例： con1, con2,)
		model.put("c4", CreateCodeUtils.createConditons4(configs));// (例：con1, con2)与cons3比少个“,”
		// 主键
		model.put("idKey", CreateCodeUtils.getIdKey(configs));// 主键（例：id1+id2）
		// sql语句
		model.put("insertSql", CreateCodeUtils.createInsertSql(configs));// 插入语句
		model.put("updateSql", CreateCodeUtils.createUpdateSql(configs));// 更新语句
		model.put("selectSql", CreateCodeUtils.createSelectSql(configs));// 查询语句

		model.put("dbType", configs.getDbType());// 数据库类型

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		model.put("nowDate", sf.format(new Date()));// 当前时间

		model.put("tableComment", configs.getTableComment() == null ? "" : configs.getTableComment());
		
		model.put("author", configs.getAuthor());

		return model;
	}

	public static boolean templateToFile(Configs configs, String tempPath, String tempFileName, File javaFile) {
		Map<String, Object> model = createDataModel(configs);
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
		Writer out = null;
		try {
			// step2 获取模版路径
			configuration.setDirectoryForTemplateLoading(new File(tempPath));
			// step3 创建数据模型model
			// step4 加载模版文件
			Template template = configuration.getTemplate(tempFileName);
			// step5 生成数据
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile), "UTF-8"));
			// step6 输出文件
			template.process(model, out);
			Tools.info("^^^^^^^^" + (javaFile.getName()) + " 创建成功 !");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.flush();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}

	public static void templateToFile(Configs configs) {
		String currentPath = System.getProperty("user.dir");
		// 模板路径
		String tempPath = configs.getTemplatePath();
		if (!StringUtils.hasText(tempPath)) {
			tempPath = currentPath + File.separator + "Template";
		}
		// 目标代码路径
		String codePath = configs.getCodePath();
		if (!StringUtils.hasText(codePath)) {
			codePath = currentPath + File.separator + "codePath";
		}

		File templeteFolder = new File(tempPath);
		File fileCopy = new File(codePath);
		// 如果不存在，创建文件夹
		if (!fileCopy.exists()) {
			fileCopy.mkdir();
		}
		// 复制并替换模板
		copyAllFiles(templeteFolder, fileCopy, configs);
	}

	public static void copyAllFiles(File files, File fileCopy, Configs configs) {
		// 判断是否是文件
		// 如果不存在，创建文件夹
		if (!fileCopy.exists()) {
			fileCopy.mkdir();
		}
		// 将文件夹下的文件存入文件数组
		String[] fs = files.list();
		for (String f : fs) {
			// 创建文件夹下的子目录
			File srcFile = new File(files, f);
			f = changeFileName(f, configs);
			File destFile = new File(fileCopy, f);
			if (srcFile.isDirectory()) {
				// 将文件进行下一层循环
				copyAllFiles(srcFile, destFile, configs);
			} else {
				templateToFile(configs, srcFile.getParent(), srcFile.getName(), destFile);
			}
		}

	}

	/**
	 * 转变文件及文件夹名称
	 * 
	 * @param f
	 * @param configs
	 * @return
	 */
	private static String changeFileName(String f, Configs configs) {
		if (StringUtils.hasText(configs.getTempModelName()) && f.equals(configs.getTempModelName())) {
			f = f.replaceAll(configs.getTempModelName(), configs.getModuleName());
		}
		if (f.contains(configs.getTemplateEntityName())) {
			f = f.replaceFirst(configs.getTemplateEntityName(), configs.getEntityName());
		}
		if (f.contains(CharUtil.toLowerCaseFirstOne(configs.getTemplateEntityName()))) {
			f = f.replaceFirst(CharUtil.toLowerCaseFirstOne(configs.getTemplateEntityName()),
					CharUtil.toLowerCaseFirstOne(configs.getEntityName()));
		}
		return f;
	}
}
