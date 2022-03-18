package com.aiit.authority.service.impl;

import com.aiit.authority.controller.request.ListAllLogsRequest;
import com.aiit.authority.controller.response.ListAllLogsResponse;
import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.controller.response.vo.LogInfoVO;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.LogManager;
import com.aiit.authority.manager.dto.ListLogsDTO;
import com.aiit.authority.repository.entity.LogDO;
import com.aiit.authority.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogManager logManager;

    /**
     * 分页查询日志信息
     *
     * @param request
     * @return
     */
    @Override
    public ListAllLogsResponse listAllLogs(ListAllLogsRequest request) {


        /**
         * 构造查询条件:
         * operator, operation, date三个参数为null时代表前端没有传对应字段，在wrapper里不参与条件查询
         * operator, date两个参数为空串时也认为前端没有传对应字段，在wrapper里不参与条件查询
         * date必须符合2021-12-22格式
         */

        QueryWrapper<LogDO> wrapper = new QueryWrapper<>();
        // 为空字段（意为默认）或为0时降序
        if (request.getTimeOrder() == null || request.getTimeOrder() == 0) {
            wrapper.lambda().orderByDesc(LogDO::getUpdateTime);
        }
        // 非空字段或为1时升序
        else if (request.getTimeOrder() != null && request.getTimeOrder() == 1) {
            wrapper.lambda().orderByAsc(LogDO::getUpdateTime);
        }
        // 操作者：非空字段且非空串时参与查询
        if (request.getOperator() != null && !request.getOperator().isEmpty()) {
            wrapper.lambda().eq(LogDO::getOperator, request.getOperator());
        }
        // 操作种类：非空字段时参与查询，范围检验在request内
        if (request.getOperation() != null) {
            wrapper.lambda().eq(LogDO::getOperation, request.getOperation());
        }


        // 传入分页相关信息和查询条件
        ListLogsDTO listLogsDTO = logManager.listLogs(request.getCurrentPage(), request.getPageSize(), wrapper);

        // 从DTO中获取到PageInfo和DO的list
        PageInfo pageInfo = listLogsDTO.getPageInfo();
        List<LogDO> logDOList = listLogsDTO.getList();

        // 由DO列表生成VO列表
        List<LogInfoVO> logInfoVOList = new ArrayList<>();
        if (logDOList != null) {
            logInfoVOList = logDOList.stream().map(logDO -> {
                LogInfoVO logInfoVO = new LogInfoVO();
                logInfoVO.setOperation(logDO.getOperation());
                logInfoVO.setOperator(logDO.getOperator());
                logInfoVO.setRecord(logDO.getRecord());
                logInfoVO.setOperationTime(logDO.getCreateTime().toLocaleString());
                return logInfoVO;
            }).collect(Collectors.toList());
        }

        // 构建响应体
        ListAllLogsResponse response = new ListAllLogsResponse();
        response.setLogInfoList(logInfoVOList);
        response.setPage(pageInfo);
        return response;
    }


    /**
     * 插入新日志
     *
     * @param record
     * @param operator
     * @param operation
     */
    @Override
    public void record(String record, String operator, int operation) {

        Boolean success = logManager.insertLog(record, operator, operation);
        // 处理异常
        if (!success) {
            throw new DatabaseException(ResultCodeEnum.SAVE_LOG_FAILED);
        }
    }


}
