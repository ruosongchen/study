package com.tjdata.irmr.lawsupervision.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.annotation.LogIntercept;
import com.tjdata.ark.base.common.bean.RspBase;
import com.tjdata.ark.base.common.exception.BusinessException;
import com.tjdata.ark.base.common.util.BeanUtil;
import com.tjdata.irmr.lawsupervision.api.model.pojo.${ClassName};
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;
import com.tjdata.irmr.lawsupervision.core.service.I${ClassName}Service;
import com.tjdata.irmr.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 *  @Description:${tableComment} 前端控制器
 *  @ClassName:${ClassName}Controller
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@RestController
@RequestMapping("/${className}")
@Api(tags = "${tableComment} 接口")
@Validated
public class ${ClassName}Controller extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private I${ClassName}Service ${className}Service;

    @LogIntercept(value = "新增信息")
    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public RspBase<Boolean> add(@RequestBody ${ClassName}Req dto) {
        try {
            ${className}Service.doSave(dto);
        } catch (BusinessException e) {
            return RspBase.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RspBase.fail(e.getMessage());
        }
        return RspBase.success();
    }

    @LogIntercept(value = "编辑信息")
    @ApiOperation("编辑")
    @PostMapping(value = "/edit")
    public RspBase<Boolean> edit(@RequestBody ${ClassName}Req dto) {
        ${ClassName} entity = BeanUtil.convertBean2Bean(dto, ${ClassName}.class);
        // 编辑
        //TODO 设置修改人信息及更新时间，完成后删除此行
        return RspBase.success(${className}Service.updateById(entity));
    }

    @LogIntercept(value = "分页查询")
    @ApiOperation("分页查询")
    @GetMapping(value = "/findPage")
    public RspBase<List<${ClassName}>> findPage(@ApiParam(required = true, name = "size", value = "分页大小") @RequestParam(value = "size") Long size,
                                                     @ApiParam(required = true, name = "current", value = "当前页") @RequestParam(value = "current") Long current) throws BusinessException {
        Page page = new Page(current, size);
        page = (Page) ${className}Service.page(page);
        return RspBase.success(page);
    }

    @LogIntercept(value = "查看详情")
    @ApiOperation("查看详情")
    @GetMapping(value = "/findById")
    public RspBase<${ClassName}> findById(@ApiParam(required = true, name = "id", value = "id") @RequestParam(value = "id") String id) {
        return RspBase.success(${className}Service.getById(id));
    }

    @LogIntercept(value = "删除信息")
    @ApiOperation("批量删除")
    @ApiImplicitParam(name = "idList", value = "要删除的id列表", dataType = "数组")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RspBase<Boolean> delete(@RequestBody List<String> idList) {
        return RspBase.success(${className}Service.removeByIds(idList));
    }


}

