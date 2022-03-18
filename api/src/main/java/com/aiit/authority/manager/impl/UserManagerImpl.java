package com.aiit.authority.manager.impl;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.UserManager;
import com.aiit.authority.manager.dto.ListUsersDTO;
import com.aiit.authority.repository.dao.UserDao;
import com.aiit.authority.repository.entity.UserDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserManagerImpl implements UserManager {

    @Resource
    UserDao userDao;

    @Override
    public Boolean insertUser(String username, String password, String remarkName,
                              UserPositionEnum role, UserStatusEnum status) {

        // 创建entity
        UserDO entity = new UserDO();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setRemarkName(remarkName);
        entity.setIsManager(role.getValue());
        entity.setStatus(status.getValue());

        QueryWrapper<UserDO> wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        // 如果用户已存在，需要让前端感知到
        if (userDao.selectOne(wrapper) != null) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_USER);
        }

        // 执行插入
        try {
            userDao.insert(entity);
        }
        // 遇到异常时返回false
        catch (Exception e) {
            return false;
        }

        // 插入成功返回true
        return true;
    }

    @Override
    public ListUsersDTO listUsers(Integer currentPage, Integer pageSize, QueryWrapper<UserDO> wrapper) {

        Page<UserDO> page = new Page<>(currentPage, pageSize);
        List<UserDO> userDOList = null;
        IPage<Map<String, Object>> iPage;

        // 构造查询条件，如以更新时间的逆序作为标准。
        if (wrapper != null) {
            wrapper.lambda().orderByDesc(UserDO::getUpdateTime);
        }

        // 查询所有用户，获取IPage对象
        try {
            iPage = userDao.selectMapsPage(page, wrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        // 将查询数据转化为系统实体类的List,service层再构造VO类。
        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            userDOList = JsonUtils.convert(iPage.getRecords(),
                    new TypeReference<List<UserDO>>() {
                    });
        }

        PageInfo pageInfo = PageUtils.getPageInfo(page);

        ListUsersDTO listUsersDTO = new ListUsersDTO();
        listUsersDTO.setList(userDOList);
        listUsersDTO.setPageInfo(pageInfo);

        return listUsersDTO;
    }

    @Override
    public List<UserDO> listUsers(QueryWrapper wrapper) {

        // 查询所有用户，获取DO列表
        List<UserDO> userDOList = null;
        try {
            userDOList = userDao.selectList(wrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        return userDOList;
    }

    @Override
    public Boolean updateUser(String username, UserStatusEnum status) {

        // 以username为条件构建wrapper
        UpdateWrapper<UserDO> wrapper = new UpdateWrapper();
        wrapper.eq("username", username);

        // 如果用户不存在，需要让前端感知到
        if (userDao.selectOne(wrapper) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_USER);
        }

        // 指定status为待更新字段
        UserDO entity = new UserDO();
        entity.setStatus(status.getValue());

        // 执行更新操作
        try {
            userDao.update(entity, wrapper);
        }
        // 遇到异常时返回false
        catch (Exception e) {
            return false;
        }

        // 更新成功返回true
        return true;
    }


    @Override
    public Boolean deleteUser(String username) {

        // 构建删除条件wrapper
        QueryWrapper<UserDO> wrapper = new QueryWrapper();
        wrapper.eq("username", username);

        // 如果用户不存在，需要让前端感知到
        if (userDao.selectOne(wrapper) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_USER);
        }

        // 删除指定用户
        try {
            userDao.delete(wrapper);
        }
        // 遇到异常时返回false
        catch (Exception e) {
            return false;
        }

        // 插入成功返回true
        return true;
    }

    @Override
    public UserDO getUser(String username) {

        // 从DB获取用户对象
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userDao.selectOne(wrapper);

    }

}
