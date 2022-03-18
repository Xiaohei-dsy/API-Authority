package com.aiit.authority.manager.impl;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.LogManager;
import com.aiit.authority.manager.dto.ListLogsDTO;
import com.aiit.authority.repository.dao.LogDao;
import com.aiit.authority.repository.entity.LogDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogManagerImpl implements LogManager {

    @Resource
    LogDao logDao;

    @Override
    public ListLogsDTO listLogs(Integer currentPage, Integer pageSize, QueryWrapper<LogDO> wrapper) {

        List<LogDO> logDOList = null;
        Page<LogDO> page = new Page<>(currentPage, pageSize);
        IPage<Map<String, Object>> iPage;

        // 查询所有日志，获取IPage对象
        try {
            iPage = logDao.selectMapsPage(page, wrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        // 将查询数据转化为系统实体类的List,service层再构造VO类。
        if (!CollectionUtils.isEmpty(iPage.getRecords())) {
            logDOList = JsonUtils.convert(iPage.getRecords(),
                    new TypeReference<List<LogDO>>() {
                    });
        }

        PageInfo pageInfo = PageUtils.getPageInfo(page);

        ListLogsDTO listLogsDTO = new ListLogsDTO();
        listLogsDTO.setList(logDOList);
        listLogsDTO.setPageInfo(pageInfo);

        return listLogsDTO;
    }

    @Override
    public List<LogDO> listLogs(QueryWrapper wrapper) {

        // 查询所有log，获取DO列表
        List<LogDO> logDOList = null;
        try {
            logDao.selectList(wrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        return logDOList;
    }

    @Override
    public Boolean insertLog(String record, String operator, Integer operation) {

        // 注入字段
        LogDO entity = new LogDO();
        entity.setOperator(operator);
        entity.setOperation(operation);
        entity.setRecord(record);

        // 执行插入
        try {
            logDao.insert(entity);
        }
        // 遇到异常时返回false
        catch (Exception e) {
            return false;
        }

        // 插入成功返回true
        return true;
    }
}
