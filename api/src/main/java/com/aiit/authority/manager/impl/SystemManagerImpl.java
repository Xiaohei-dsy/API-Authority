package com.aiit.authority.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.manager.dto.ListSystemDTO;
import com.aiit.authority.repository.dao.SystemDao;
import com.aiit.authority.repository.entity.SystemDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemManagerImpl implements SystemManager {

    @Resource
    private SystemDao systemDao;

    @Override
    public SystemDO getSystem(String systemId) {
        SystemDO systemDO;
        QueryWrapper<SystemDO> wrapper = new QueryWrapper();
        wrapper.eq("system_id", systemId);
        try {
            systemDO = systemDao.selectOne(wrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        return systemDO;
    }

    @Override
    public Boolean insertSystem(String systemId, String description) {

        SystemDO systemDO = new SystemDO();
        systemDO.setSystemId(systemId);
        systemDO.setDescription(description);

        Map<String, Object> map = new HashMap<>();
        map.put("system_id", systemId);

        if (getSystem(systemId) != null) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_SYSTEM);
        }

        try {
            systemDao.insert(systemDO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public ListSystemDTO listSystem(Integer currentPage, Integer pageSize) {
        Page<SystemDO> page = new Page<>(currentPage, pageSize);
        List<SystemDO> systemDOList = null;
        IPage<Map<String, Object>> iPage;

        // 构造查询条件，如以更新时间的逆序作为标准。
        QueryWrapper<SystemDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");

        try {
            iPage = systemDao.selectMapsPage(page, queryWrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        // 将查询数据转化为系统实体类的List,service层再构造VO类。
        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            systemDOList = JsonUtils.convert(iPage.getRecords(),
                    new TypeReference<List<SystemDO>>() { });
        }
        PageInfo pageInfo = PageUtils.getPageInfo(page);

        ListSystemDTO listSystemDTO = new ListSystemDTO();
        listSystemDTO.setList(systemDOList);
        listSystemDTO.setPageInfo(pageInfo);

        return listSystemDTO;
    }

    @Override
    public Boolean deleteSystem(String systemId) {
        QueryWrapper<SystemDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SystemDO::getSystemId,systemId);
        try {
            return systemDao.delete(wrapper) >= 0;
        } catch (Exception e) {
            log.error("删除系统失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    public Boolean updateSystem(String systemId,String description) {
        UpdateWrapper<SystemDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(SystemDO::getSystemId,systemId);
        SystemDO entity = new SystemDO();
        entity.setDescription(description);
        try {
            return systemDao.update(entity, wrapper) > 0;
        } catch (Exception e) {
            log.error("更新系统失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }
}
