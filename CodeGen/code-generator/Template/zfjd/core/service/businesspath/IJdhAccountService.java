package com.tjdata.irmr.lawsupervision.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.exception.BusinessException;
import com.tjdata.ark.base.mybatis.service.IService;
import com.tjdata.irmr.lawsupervision.api.model.pojo.${ClassName};
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;

import java.util.List;

/**
 *
 *  @Description:${tableComment} 服务接口类
 *  @ClassName:I${ClassName}Service
 *  @author:${author}
 *  @datetime :${nowDate}
 */
public interface I${ClassName}Service extends IService<${ClassName}> {

    /**
     * 获取${tableComment}分页列表
     *
     * @param page
     * @return
     * @throws BusinessException
     */
    Page<${ClassName}> findPage(Page page) throws BusinessException;


    /**
     *  ${tableComment} 保存
     *
     * @param dto
     * @throws BusinessException
     */
    void doSave(${ClassName}Req dto) throws BusinessException;
}

