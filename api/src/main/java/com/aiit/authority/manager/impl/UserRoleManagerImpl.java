package com.aiit.authority.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.UserRoleManager;
import com.aiit.authority.manager.dto.ListUserRoleDTO;
import com.aiit.authority.repository.dao.UserRoleDao;
import com.aiit.authority.repository.entity.UserRoleDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRoleManagerImpl implements UserRoleManager {

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public Boolean insertRole(String username, String roleName, String systemId) {
        if (isValidUserRole(username, roleName, systemId)) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_USER_ROLE);
        } else if (getUserRole(username, systemId) != null
                && isValidUserRole(username, roleName, systemId) == false) {
            throw new DatabaseException(ResultCodeEnum.NO_MORE_ROLE);
        }
        UserRoleDO userRoleDO = new UserRoleDO() {
            {
                setUsername(username);
                setRoleName(roleName);
                setSystemId(systemId);
            }
        };
        try {
            return userRoleDao.insert(userRoleDO) >= 0;
        } catch (Exception e) {
            log.error("用户的角色信息插入失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    /**
     * 根据用户名查询所有角色信息
     */
    @Override
    public ListUserRoleDTO listUserRoleByUser(String username, Integer currentPage, Integer pageSize) {
        Page<UserRoleDO> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUsername, username);
        wrapper.lambda().orderByDesc(UserRoleDO::getUpdateTime);
        return convertToDTO(wrapper, page);
    }

    /**
     * 根据系统信息来查询所有用户信息 （用户系统作为唯一索引）
     */
    @Override
    public ListUserRoleDTO listUserRoleBySystem(String roleName, String systemId, Integer currentPage,
        Integer pageSize) {
        Page<UserRoleDO> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getRoleName, roleName).eq(UserRoleDO::getSystemId, systemId);
        wrapper.lambda().orderByDesc(UserRoleDO::getUpdateTime);
        return convertToDTO(wrapper, page);
    }

    public ListUserRoleDTO convertToDTO(QueryWrapper<UserRoleDO> wrapper, Page<UserRoleDO> page) {
        IPage<Map<String, Object>> iPage;
        List<UserRoleDO> userRoleDOList = null;
        try {
            iPage = userRoleDao.selectMapsPage(page, wrapper);
        } catch (Exception e) {
            log.error("根据用户名查询角色分页信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            userRoleDOList = JsonUtils.convert(iPage.getRecords(),
                    new TypeReference<List<UserRoleDO>>() { } );
        }

        PageInfo pageInfo = PageUtils.getPageInfo(page);
        ListUserRoleDTO listUserRoleDTO = new ListUserRoleDTO();
        listUserRoleDTO.setPageInfo(pageInfo);
        listUserRoleDTO.setList(userRoleDOList);

        return listUserRoleDTO;
    }

    @Override
    public Boolean deleteUserRole(String username, String roleName, String systemId) {
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getRoleName, roleName).eq(UserRoleDO::getUsername, username)
            .eq(UserRoleDO::getSystemId, systemId);
        try {
            return userRoleDao.delete(wrapper) >= 0;
        } catch (Exception e) {
            log.error("删除用户角色信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public UserRoleDO getUserRole(String username, String systemId) {
        UserRoleDO userRoleDO;
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUsername, username).eq(UserRoleDO::getSystemId, systemId);
        try {
            userRoleDO = userRoleDao.selectOne(wrapper);
        } catch (Exception e) {
            log.error("获取单个用户系统信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        return userRoleDO;
    }

    /**
     * 用户名加系统名，以及要更改的角色名，因为用户和系统是唯一索引，所以可以定位到指定的一条数据进行更改。
     */
    @Override
    public Boolean updateUserRole(String username, String roleName, String systemId) {
        UpdateWrapper<UserRoleDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUsername, username).eq(UserRoleDO::getSystemId, systemId);
        UserRoleDO entity = new UserRoleDO();
        entity.setRoleName(roleName);
        try {
            return userRoleDao.update(entity, wrapper) > 0;
        } catch (Exception e) {
            log.error("更新用户角色信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public Boolean isValidUserRole(String username, String roleName, String systemId) {
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUsername, username).eq(UserRoleDO::getRoleName, roleName)
            .eq(UserRoleDO::getSystemId, systemId);
        try {
            return userRoleDao.selectOne(wrapper) != null;
        } catch (Exception e) {
            log.error("验证用户信息失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

}
