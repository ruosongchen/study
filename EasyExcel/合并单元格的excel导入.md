## Excel表格

下面是Excel表格，成果简介前可以作为主表数据存储(存在合并)，成果简介后的数据作为从表数据存储。

## 实现思路

- 通过EasyExcel读取Excel数据，用List<AwardsDetailField> list接收
  因为POI/EasyExcel对合并单元格的数据只读取一次，需要把为空的单元格也赋值，这是**核心技术难点**。

- 循环使用hibernate-validator对数据进行校验
  存在校验不通过的数据返回给前端,所有数据校验通过才写入数据库

- 封装然后写入数据库

  - 用Java8 对List< DetailFeiled> list根据多字段进行分组，得到Map<String,List> map
  - 循环map，封装主表和详细表插入list
  - 批量插入

- 导入时要求同一年数据可以分多次导入，但同一年内，成果名称不允许重复
  这个可以在数据库中根据**获奖类型+获奖年份+成果名称**建立联合约束

  ```sql
  ALTER TABLE t_awards ADD CONSTRAINT award_type_year_product UNIQUE (award_type,award_year,award_product);
  ```

## 相关代码

**下面贴出的是通过EasyExcel读取Excel数据的测试代码**

**pom依赖：**

```
<dependencies>
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>easyexcel</artifactId>
       <version>2.2.6</version>
   </dependency>
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>fastjson</artifactId>
       <version>1.2.76</version>
   </dependency>
   <!--lombok-->
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.20</version>
       <scope>provided</scope>
   </dependency>
   <!--junit测试-->
   <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>4.12</version>
       <scope>test</scope>
   </dependency>
   <!--slf4j简单实现-->
   <dependency>
       <groupId>org.slf4j</groupId>
       <artifactId>slf4j-simple</artifactId>
       <version>1.7.25</version>
       <scope>compile</scope>
   </dependency>
   <!--用于给泛型对象的属性设置值-->
   <dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.3</version>
</dependency>
</dependencies>
```

**Excel行号类:**

```
@Data
public class RowIndex {
    @ExcelIgnore
    private Integer lineNo;
}
```

**AwardsDetailField类(需继承RowIndex)：**

```
package com.importexcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Excel对应的Java实体类
 */
@Data
public class AwardsDetailField extends RowIndex {

    @ExcelProperty(value="奖项等级（一级分类）",index = 0)
    private String topClass;

    @ExcelProperty(value="奖项等级（二级分类）",index = 1)
    private String secondClass;

    @ExcelProperty(value="获奖年份",index = 2)
    private String awardYear;

    @ExcelProperty(value="获奖产品/成果名称",index = 3)
    private String awardProduct;

    @ExcelProperty(value="成果简介",index = 4)
    private String briefIntroduction;

    @ExcelProperty(value="获奖单位",index = 5)
    private String company;

    @ExcelProperty(value="获奖个人姓名",index = 6)
    private String name;

    @ExcelProperty(value="员工编号",index = 7)
    private String employeeNo;

    @ExcelProperty(value="获奖金额（元）",index = 8)
    private BigDecimal money;
}
```

**ImportExcelHelper类(导入Excel入口、对合并单元格处理)：**

```
package com.importexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class ImportExcelHelper<T> {

    private static final Logger LOGGER 
    		= LoggerFactory.getLogger(ImportExcelHelper.class);

    /**
     * 返回解析后的List
     *
     * @param: fileName 文件名
     * @param: clazz Excel对应属性名
     * @param: sheetNo 要解析的sheet
     * @param: headRowNumber 正文起始行
     * @return java.util.List<T> 解析后的List
     */
    public List<T> getList(String fileName, Class<T> clazz
    		, Integer sheetNo, Integer headRowNumber) {
        ImportExcelListener<T> listener = new ImportExcelListener<>(headRowNumber);
        try {
            EasyExcel.read(fileName, clazz, listener)
            	.extraRead(CellExtraTypeEnum.MERGE)
            	.sheet(sheetNo)
            	.headRowNumber(headRowNumber)
            	.doRead();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        List<CellExtra> extraMergeInfoList = listener.getExtraMergeInfoList();
        if (CollectionUtils.isEmpty(extraMergeInfoList)) {
            return listener.getData();
        }
        List<T> data = explainMergeData(listener.getData()
        		, extraMergeInfoList, headRowNumber);
        return data;
    }

    /**
     * 处理合并单元格
     *
     * @param data               解析数据
     * @param extraMergeInfoList 合并单元格信息
     * @param headRowNumber      起始行
     * @return 填充好的解析数据
     */
    private List<T> explainMergeData(List<T> data
    		, List<CellExtra> extraMergeInfoList, Integer headRowNumber) {
        //循环所有合并单元格信息
        extraMergeInfoList.forEach(cellExtra -> {
            int firstRowIndex = cellExtra.getFirstRowIndex() - headRowNumber;
            int lastRowIndex = cellExtra.getLastRowIndex() - headRowNumber;
            int firstColumnIndex = cellExtra.getFirstColumnIndex();
            int lastColumnIndex = cellExtra.getLastColumnIndex();
            //获取初始值
            Object initValue = getInitValueFromList(firstRowIndex
            		, firstColumnIndex, data);
            //设置值
            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
                    setInitValueToList(initValue, i, j, data);
                }
            }
        });
        return data;
    }

    /**
     * 设置合并单元格的值
     *
     * @param filedValue  值
     * @param rowIndex    行
     * @param columnIndex 列
     * @param data        解析数据
     */
    public void setInitValueToList(Object filedValue
    		, Integer rowIndex, Integer columnIndex, List<T> data) {
        T object = data.get(rowIndex);

        for (Field field : object.getClass().getDeclaredFields()) {
            //提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == columnIndex) {
                    try {
                        field.set(object, filedValue);
                        break;
                    } catch (IllegalAccessException e) {
                        LOGGER.error("设置合并单元格的值异常："+e.getMessage());
                    }
                }
            }
        }
    }


    /**
     * 获取合并单元格的初始值
     * rowIndex对应list的索引
     * columnIndex对应实体内的字段
     *
     * @param firstRowIndex    起始行
     * @param firstColumnIndex 起始列
     * @param data             列数据
     * @return 初始值
     */
    private Object getInitValueFromList(Integer firstRowIndex
    		, Integer firstColumnIndex, List<T> data) {
        Object filedValue = null;
        T object = data.get(firstRowIndex);
        for (Field field : object.getClass().getDeclaredFields()) {
            //提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == firstColumnIndex) {
                    try {
                        filedValue = field.get(object);
                        break;
                    } catch (IllegalAccessException e) {
                        LOGGER.error("设置合并单元格的初始值异常："+e.getMessage());
                    }
                }
            }
        }
        return filedValue;
    }
}
```

**Excel模板的读取监听类：**

```
package com.importexcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel模板的读取监听类
 *
 * @author wangwei
 */
public class ImportExcelListener<T> extends AnalysisEventListener<T> {
    private static final Logger LOGGER 
    		= LoggerFactory.getLogger(ImportExcelListener.class);
    /**
     * 解析的数据
     */
    List<T> list = new ArrayList<>();

    /**
     * 正文起始行
     */
    private Integer headRowNumber;
    /**
     * 合并单元格
     */
    private List<CellExtra> extraMergeInfoList = new ArrayList<>();

    public ImportExcelListener(Integer headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data  one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        // 如果一行Excel数据均为空值，则不装载该行数据
        if(isLineNullValue(data)){
            return;
        }
        LOGGER.info("解析到一条数据: {}", gson.toJson(data));
	    // 获取Excle行号(从0开始)
	    ReadRowHolder readRowHolder = context.readRowHolder();
	    Integer rowIndex = readRowHolder.getRowIndex();
	    try {
	        BeanUtils.setProperty(data, "lineNo", rowIndex+1);
	    } catch (IllegalAccessException e) {
	        LOGGER.error("ImportExcelListener.invoke 设置行号异常: ", e);
	    } catch (InvocationTargetException e) {
	        LOGGER.error("ImportExcelListener.invoke 设置行号异常: ", e);
	    }
	    list.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 返回解析出来的List
     */
    public List<T> getData() {
        return list;
    }

    /**
     * 读取额外信息：合并单元格
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        LOGGER.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
        switch (extra.getType()) {
            case MERGE: {
                LOGGER.info(
                        "额外信息是合并单元格,而且覆盖了一个区间
                        ,在firstRowIndex:{},firstColumnIndex;{}
                        ,lastRowIndex:{},lastColumnIndex:{}"
                        ,extra.getFirstRowIndex()
                        ,extra.getFirstColumnIndex()
                        ,extra.getLastRowIndex()
                        ,extra.getLastColumnIndex());
                if (extra.getRowIndex() >= headRowNumber) {
                    extraMergeInfoList.add(extra);
                }
                break;
            }
            default:
        }
    }

    /**
     * 返回解析出来的合并单元格List
     */
    public List<CellExtra> getExtraMergeInfoList() {
        return extraMergeInfoList;
    }

    /**
	 * 判断整行单元格数据是否均为空 true是 false否
	 */
	private boolean isLineNullValue(T data) {
		if (data instanceof String) {
			return Objects.isNull(data);
		}
		try {
			List<Field> fields = Arrays.stream(data.getClass().getDeclaredFields())
					.filter(f -> f.isAnnotationPresent(ExcelProperty.class))
					.collect(Collectors.toList());
			List<Boolean> lineNullList = new ArrayList<>(fields.size());
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(data);
				if (Objects.isNull(value)) {
					lineNullList.add(Boolean.TRUE);
				} else {
					lineNullList.add(Boolean.FALSE);
				}
			}
			return lineNullList.stream().allMatch(Boolean.TRUE::equals);
		} catch (Exception e) {
			LOGGER.error("读取数据行[{}]解析失败: {}", data, e.getMessage());
		}
		return true;
	}
}
```

**测试类：**

```java
package com.importexcel;
import com.alibaba.fastjson.JSON;
import java.util.List;

/**
 * 测试类
 * @createTime 2021/08/06
 */
public class ImportTest {


    public static void main(String[] args) {
        String PATH = "E:\\Downloads\\";
        String fileName = PATH + "各奖励网站导入模版.xlsx";
        ImportExcelHelper<AwardsDetailField> helper = new ImportExcelHelper<>();
        List<AwardsDetailField> list 
        		= helper.getList(fileName,AwardsDetailField.class,0,1);
        System.out.println(JSON.toJSONString(list));
    }
}
```

运行后打印的JSON字符串,用JSON工具解析发现完全是想要的效果，成功搞定！

参考链接：[EasyExcel导入存在合并单元格的Excel (csdn.net)](read://https_blog.csdn.net/?url=https%3A%2F%2Fblog.csdn.net%2Fa1275302036%2Farticle%2Fdetails%2F119545551)

----

问题1：Excel解析时报错：Convert excel format exception.You can try specifying the ‘excelType‘ yourself

原因：因为在通过file获取的流类型，在EasyExcel.read读的时候，采用的同一个流，会导致excel类型出错。

问题2：Handler dispatch failed; nested exception is java.lang.NoSuchMethodError: org.apache.poi.ss.usermodel.Cell.getCellType()Lorg/apache/poi/ss/usermodel/CellType

原因：版本冲突
