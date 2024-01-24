package com.tjdata.irmr.lawsupervision.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.bean.RspBase;
import com.tjdata.ark.base.common.exception.BusinessException;
import com.tjdata.irmr.common.utils.ResponseUtils;
import com.tjdata.irmr.lawsupervision.api.feign.I${ClassName}Fegin;
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;
import com.tjdata.irmr.lawsupervision.api.model.rsp.${ClassName}Rsp;
import com.tjdata.irmr.lawsupervision.api.service.IFegin${ClassName}Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *  @Description:${tableComment} feign服务接口实现类
 *  @ClassName:Fegin${ClassName}ServiceImpl
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@Service
public class Fegin${ClassName}ServiceImpl implements IFegin${ClassName}Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Fegin${ClassName}ServiceImpl.class);

    @Resource
    I${ClassName}Fegin ${className}Fegin;


    @Override
    public boolean doSave(${ClassName}Req dto) throws BusinessException {
        RspBase<Boolean> rspBase = ${className}Fegin.add(dto);
        ResponseUtils.checkResponse(rspBase, "数据保存失败！");
        return rspBase.getData();
    }

    @Override
    public boolean updateById(${ClassName}Req dto) {
        RspBase<Boolean> rspBase = ${className}Fegin.edit(dto);
        ResponseUtils.checkResponse(rspBase, "数据更新失败！");
        return rspBase.getData();
    }

    @Override
    public Page<${ClassName}Rsp> findPage(Page page) {
        RspBase<Page<${ClassName}Rsp>> rspBase = ${className}Fegin.findPage(page.getSize(),page.getCurrent());
        ResponseUtils.checkResponse(rspBase, "分页数据失败！");
        return rspBase.getData();
    }

    @Override
    public ${ClassName}Rsp getById(String id) {
        RspBase<${ClassName}Rsp> rspBase = ${className}Fegin.findById(id);
        ResponseUtils.checkResponse(rspBase, "获取详情失败！");
        return rspBase.getData();
    }

    @Override
    public boolean removeByIds(List<String> idList) {
        RspBase<Boolean> rspBase = ${className}Fegin.delete(idList);
        ResponseUtils.checkResponse(rspBase, "删除数据失败！");
        return rspBase.getData();
    }
}
