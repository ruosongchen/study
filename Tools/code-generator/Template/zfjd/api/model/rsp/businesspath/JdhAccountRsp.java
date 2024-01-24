package com.tjdata.irmr.lawsupervision.api.model.rsp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *  @Description:${tableComment} 返回实体
 *  @ClassName:${ClassName}Rsp
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@ApiModel(value = "${ClassName}Rsp对象", description = "${tableComment} 返回实体")
public class ${ClassName}Rsp implements Serializable {
	private static final long serialVersionUID = 1L;
    
	<#list columns as column>
    @ApiModelProperty(value = "${column.comments}")
    private ${column.javaType} ${column.javaName};
    </#list>

${getAndSetCode}
	
}
