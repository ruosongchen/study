package com.tjdata.irmr.lawsupervision.api.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *  @Description:${tableComment} 数据库实体
 *  @ClassName:${ClassName}
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@ApiModel(value = "${ClassName}对象", description = "${tableComment} 数据库实体")
@TableName("${tableName}")
public class ${ClassName} implements Serializable {
    
	<#list columns as column>
    /**
    * ${column.comments}
    */
    <#if column.isPk =true >
	@TableId(value = "${column.name}", type = IdType.NONE)
    </#if>
    <#if column.isPk =false >
	@TableField("${column.name}")
    </#if>
    @ApiModelProperty(value = "${column.comments}")
    private ${column.javaType} ${column.javaName};
    </#list>

${getAndSetCode}
	
}
