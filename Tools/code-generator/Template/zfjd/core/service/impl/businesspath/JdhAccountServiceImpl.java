package com.tjdata.irmr.lawsupervision.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjdata.ark.base.common.config.RedisConfig;
import com.tjdata.ark.base.common.exception.BusinessException;
import com.tjdata.ark.base.common.util.BeanUtil;
import com.tjdata.ark.base.mybatis.service.ServiceImpl;
import com.tjdata.ark.base.sdk.bean.saas.SaasUser;
import com.tjdata.irmr.lawsupervision.api.model.pojo.${ClassName};
import com.tjdata.irmr.lawsupervision.api.model.req.${ClassName}Req;
import com.tjdata.irmr.lawsupervision.core.dao.${ClassName}Mapper;
import com.tjdata.irmr.lawsupervision.core.service.I${ClassName}Service;
import com.tjdata.irmr.web.context.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 *
 *  @Description:${tableComment} 服务实现类
 *  @ClassName:${ClassName}ServiceImpl
 *  @author:${author}
 *  @datetime :${nowDate}
 */
@Service
@CacheConfig(cacheNames = ${ClassName}ServiceImpl.CACHE_NAME, keyGenerator = RedisConfig.KEY_GENERATOR)
public class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}> implements I${ClassName}Service {

    /**
     * Spring cache的缓存名称
     */
    public static final String CACHE_NAME = "${ClassName}ServiceImpl";

    @Autowired
    private ${ClassName}Mapper ${className}Mapper;

    /**
     *  分页查询
     * @param page
     * @return:
     * @Author: ${author}
     * @Date: ${nowDate}
     * @throws BusinessException
     */
    @Override
    public Page<${ClassName}> findPage(Page page) throws BusinessException {
        return ${className}Mapper.selectPage(page, null);
    }


    /**
     *  新增
     * @param dto
     * @Author: ${author}
     * @Date: ${nowDate}
     * @throws BusinessException
     */
    @Override
    public void doSave(${ClassName}Req dto) throws BusinessException {
        //校验参数
        validDoSave(dto);
        // SaasUser userInfo = WebContext.getInstance().getUserInfo();
        ${ClassName} entity = BeanUtil.convertBean2Bean(dto, ${ClassName}.class);
        WebContext.getInstance().setCreate(entity);
        super.save(entity);
    }

    /**
     *  校验doSave参数
     * @param dto
     * @Author: ${author}
     * @Date: ${nowDate}
     * @throws BusinessException
     */
    private void validDoSave(${ClassName}Req dto) throws BusinessException {

    }
}


