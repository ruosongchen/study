package com.tjdata.irmr.lawsupervision.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.annotation.LogIntercept;
import com.tjdata.ark.base.common.bean.RspBase;
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;
import com.tjdata.irmr.lawsupervision.api.model.rsp.${ClassName}Rsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *  @Description:${tableComment} feign
 *  @ClassName:I${ClassName}Fegin
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@FeignClient(value = "${r'${feign.lawsupervision.name}'}", path = "/${className}", contextId = "${ClassName}Req")
public interface I${ClassName}Fegin {

    /**
     *  新增信息
     * @param dto
     * @return: com.tjdata.ark.base.common.bean.RspBase<java.lang.Boolean>
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    @LogIntercept(value = "新增信息")
    @ApiOperation("新增")
    @PostMapping(value = "/add")
    public RspBase<Boolean> add(@RequestBody ${ClassName}Req dto);

    /**
     *  编辑信息
     * @param dto
     * @return: com.tjdata.ark.base.common.bean.RspBase<java.lang.Boolean>
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    @LogIntercept(value = "编辑信息")
    @ApiOperation("编辑")
    @PostMapping(value = "/edit")
    public RspBase<Boolean> edit(@RequestBody ${ClassName}Req dto);

    /**
     *  分页查询
     * @param size 分页大小
     * @param current 当前页
     * @return: com.tjdata.ark.base.common.bean.RspBase<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.tjdata.irmr.lawsupervision.api.model.rsp.${ClassName}Rsp>>
     * @Author: zhengxin
     * @Date: 2023/7/19 10:56
     */
    @LogIntercept(value = "分页查询")
    @ApiOperation("分页查询")
    @GetMapping(value = "/findPage")
    public RspBase<Page<${ClassName}Rsp>> findPage(@ApiParam(required = true, name = "size", value = "分页大小") @RequestParam(value = "size") Long size,
                                                     @ApiParam(required = true, name = "current", value = "当前页") @RequestParam(value = "current") Long current);

    /**
     *  查看详情
     * @param id
     * @return: com.tjdata.ark.base.common.bean.RspBase<com.tjdata.irmr.lawsupervision.api.model.rsp.${ClassName}Rsp>
     * @Author: zhengxin
     * @Date: 2023/7/19 10:56
     */
    @LogIntercept(value = "查看详情")
    @ApiOperation("查看详情")
    @GetMapping(value = "/findById")
    public RspBase<${ClassName}Rsp> findById(@ApiParam(required = true, name = "id", value = "id") @RequestParam(value = "id") String id);

    /**
     *  删除信息
     * @param idList 要删除的id列表
     * @return: com.tjdata.ark.base.common.bean.RspBase<java.lang.Boolean>
     * @Author: zhengxin
     * @Date: 2023/7/19 10:57
     */
    @LogIntercept(value = "删除信息")
    @ApiOperation("批量删除")
    @ApiImplicitParam(name = "idList", value = "要删除的id列表", dataType = "数组")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RspBase<Boolean> delete(@RequestBody List<String> idList);

}
