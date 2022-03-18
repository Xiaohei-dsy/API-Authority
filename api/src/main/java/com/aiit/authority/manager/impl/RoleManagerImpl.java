package com.aiit.authority.manager.impl;

import javax.annotation.Resource;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.manager.dto.ListRoleDTO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.RoleManager;
import com.aiit.authority.repository.dao.RoleDao;
import com.aiit.authority.repository.entity.RoleDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RoleManagerImpl implements RoleManager {

    @Resource
    private RoleDao roleDao;

    @Override
    public Boolean insertRole(String roleName, String systemId, String description) {
        RoleDO roleDO = new RoleDO() {
            {
                setRoleName(roleName);
                setSystemId(systemId);
                setDescription(description);
            }
        };
        if (getRole(roleName, systemId) != null) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_ROLE_SYSTEM);
        }
        try {
            return roleDao.insert(roleDO) > 0;
        } catch (Exception e) {
            log.error("角色插入失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public RoleDO getRole(String roleName, String systemId) {
        RoleDO roleDO;
        QueryWrapper<RoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleDO::getRoleName, roleName).eq(RoleDO::getSystemId, systemId);
        try {
            roleDO = roleDao.selectOne(wrapper);
        } catch (Exception e) {
            log.error("获取单个角色信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        return roleDO;
    }

    @Override
    public Boolean deleteRole(String roleName, String systemId) {
        QueryWrapper<RoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleDO::getRoleName, roleName).eq(RoleDO::getSystemId, systemId);
        try {
            return roleDao.delete(wrapper) > 0;
        } catch (Exception e) {
            log.error("删除角色失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public Boolean updateRole(String roleName, String systemId, String description) {
        UpdateWrapper<RoleDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(RoleDO::getRoleName, roleName).eq(RoleDO::getSystemId, systemId);
        RoleDO entity = new RoleDO();
        entity.setDescription(description);
        try {
            return roleDao.update(entity, wrapper) > 0;
        } catch (Exception e) {
            log.error("更新角色失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public ListRoleDTO listRole(Integer currentPage, Integer pageSize) {
        Page<RoleDO> page = new Page<>(currentPage, pageSize);
        List<RoleDO> roleDOList = null;
        IPage<Map<String, Object>> iPage;

        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        try {
            iPage = roleDao.selectMapsPage(page, queryWrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            roleDOList = JsonUtils.convert(iPage.getRecords(),
                    new TypeReference<List<RoleDO>>() { } );
        }

        PageInfo pageInfo = PageUtils.getPageInfo(page);
        ListRoleDTO listRoleDTO = new ListRoleDTO();
        listRoleDTO.setPageInfo(pageInfo);
        listRoleDTO.setRoleList(roleDOList);

        return listRoleDTO;
    }

}
