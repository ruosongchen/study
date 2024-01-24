package com.tjdata.irmr.lawsupervision.api.model.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *  @Description:${tableComment} 请求实体
 *  @ClassName:${ClassName}Req
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@ApiModel(value = "${ClassName}Req对象", description = "${tableComment} 请求实体")
public class ${ClassName}Req implements Serializable {
    
	<#list columns as column>
    @ApiModelProperty(value = "${column.comments}")
    private ${column.javaType} ${column.javaName};
    </#list>

${getAndSetCode}
	
}
