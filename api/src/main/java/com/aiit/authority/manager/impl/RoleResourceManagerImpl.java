package com.aiit.authority.manager.impl;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.ResourceManager;
import com.aiit.authority.manager.RoleResourceManager;
import com.aiit.authority.manager.dto.ListRoleResourceDTO;
import com.aiit.authority.repository.dao.RoleResourceDao;
import com.aiit.authority.repository.entity.RoleResourceDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@Component
public class RoleResourceManagerImpl implements RoleResourceManager {

    @Value("${role.resource.limit}")
    private Integer limit;

    @Resource
    private RoleResourceDao roleResourceDao;

    @Resource
    private ResourceManager resourceManager;

    @Override
    public Boolean insertRoleResource(String roleName, String systemId, String resourceId) {
        if (resourceManager.getResource(resourceId, systemId) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_RESOURCE);
        }
        if (getRoleResource(roleName, systemId, resourceId) != null) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_ROLE_RESOURCE);
        }

        int cnt = countResource(roleName, systemId);

        if (cnt > limit) {
            log.error("角色绑定的资源为{},超过上限{}", cnt, limit);
            throw new DatabaseException(ResultCodeEnum.OVER_NUM_RESOURCE);
        }
        RoleResourceDO roleResourceDO = new RoleResourceDO() {
            {
                setRoleName(roleName);
                setSystemId(systemId);
                setResourceId(resourceId);
            }
        };
        try {
            return roleResourceDao.insert(roleResourceDO) > 0;
        } catch (Exception e) {
            log.error("角色的资源信息插入失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public RoleResourceDO getRoleResource(String roleName, String systemId, String resourceId) {
        RoleResourceDO roleResourceDO;
        QueryWrapper<RoleResourceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResourceDO::getRoleName, roleName).
                eq(RoleResourceDO::getSystemId, systemId)
                .eq(RoleResourceDO::getResourceId, resourceId);
        try {
            roleResourceDO = roleResourceDao.selectOne(queryWrapper);
        } catch (Exception e) {
            log.error("查询单个角色资源失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        return roleResourceDO;
    }

    @Override
    public ListRoleResourceDTO listRoleResource(String roleName, String systemId,
                                                Integer currentPage, Integer pageSize) {
        Page<RoleResourceDO> page = new Page<>(currentPage, pageSize);
        QueryWrapper<RoleResourceDO> queryWrapper = new QueryWrapper<>();

        if (Objects.nonNull(roleName)) {
            queryWrapper.lambda().eq(RoleResourceDO::getRoleName, roleName);
        }

        if (Objects.nonNull(systemId)) {
            queryWrapper.lambda().eq(RoleResourceDO::getSystemId, systemId);
        }

        queryWrapper.lambda().orderByDesc(RoleResourceDO::getUpdateTime);
        IPage<Map<String, Object>> iPage;
        try {
            iPage = roleResourceDao.selectMapsPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("根据用户名查询角色分页信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        List<RoleResourceDO> roleResourceDOList = CollectionUtils.isEmpty(iPage.getRecords()) ? new ArrayList<>()
                : JsonUtils.convert(iPage.getRecords(), new TypeReference<List<RoleResourceDO>>() {
        });
        PageInfo pageInfo = PageUtils.getPageInfo(page);
        return new ListRoleResourceDTO(pageInfo, roleResourceDOList);
    }

    @Override
    public Boolean deleteRoleResource(String roleName, String systemId, String resourceId) {
        QueryWrapper<RoleResourceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResourceDO::getRoleName, roleName).eq(RoleResourceDO::getSystemId, systemId)
                .eq(RoleResourceDO::getResourceId, resourceId);
        try {
            return roleResourceDao.delete(queryWrapper) >= 0;
        } catch (Exception e) {
            log.error("删除用户资源失败");
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public Integer countResource(String roleName, String systemId) {
        QueryWrapper<RoleResourceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResourceDO::getRoleName, roleName)
                .eq(RoleResourceDO::getSystemId, systemId);
        try {
            return roleResourceDao.selectCount(queryWrapper);
        } catch (Exception e) {
            log.error("获取角色资源数失败");
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

}
