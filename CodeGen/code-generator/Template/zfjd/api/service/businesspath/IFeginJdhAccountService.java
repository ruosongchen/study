package com.tjdata.irmr.lawsupervision.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.exception.BusinessException;
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;
import com.tjdata.irmr.lawsupervision.api.model.rsp.${ClassName}Rsp;

import java.util.List;

/**
 *
 *  @Description:${tableComment} feign服务接口
 *  @ClassName:IFegin${ClassName}Service
 *  @author:${author}
 *  @datetime :${nowDate}
 */
public interface IFegin${ClassName}Service {

    /**
     *  数据保存
     * @param dto 实体参数
     * @return: boolean
     * @Author: ${author}
     * @Date: ${nowDate}
     * @throws BusinessException
     */
    boolean doSave(${ClassName}Req dto) throws BusinessException;

    /**
     *  数据更新
     * @param dto 实体参数
     * @return: boolean
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    boolean updateById(${ClassName}Req dto);

    /**
     *  分页查询
     * @param page
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.tjdata.irmr.lawsupervision.api.model.rsp.ZfjdCaseEvaluationTaskTemplateRsp>
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    Page<${ClassName}Rsp> findPage(Page page);

    /**
     *  根据ID获取详情信息
     * @param id
     * @return: com.tjdata.irmr.lawsupervision.api.model.rsp.ZfjdCaseEvaluationTaskTemplateRsp
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    ${ClassName}Rsp getById(String id);

    /**
     *  批量删除
     * @param idList ID数组
     * @return: boolean
     * @Author: ${author}
     * @Date: ${nowDate}
     */
    boolean removeByIds(List<String> idList);
}
