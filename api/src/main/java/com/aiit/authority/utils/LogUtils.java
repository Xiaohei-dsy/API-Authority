package com.aiit.authority.utils;

import com.aiit.authority.enums.OperationTypeEnum;
import com.aiit.authority.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class LogUtils {

    @Resource
    private LogService logService;


    public void logRegisterSelf(String userType, String operator) {
        String message = String.format("%s [%s] 注册成功", userType, operator);
        logService.record(message, operator, OperationTypeEnum.INSERT.getType());
        log.info(message);
    }

    public void logRegisterOther(String operatee) {
        String operator = ThreadLocalUtil.get("username");
        String message = String.format("管理员 [%s] 创建新用户 [%s]", operator, operatee);
        logService.record(message, operator, OperationTypeEnum.INSERT.getType());
        log.info(message);

    }

    public void logActivateUser(String operatee) {
        String operator = ThreadLocalUtil.get("username");
        String message = String.format("管理员 [%s] 启用用户 [%s]", operator, operatee);
        logService.record(message, operator, OperationTypeEnum.UPDATE.getType());
        log.info(message);
    }

    public void logDisableUser(String operatee) {
        String operator = ThreadLocalUtil.get("username");
        String message = String.format("管理员 [%s] 禁用用户 [%s]", operator, operatee);
        logService.record(message, operator, OperationTypeEnum.UPDATE.getType());
        log.info(message);
    }

    public void logDeleteUser(String operatee) {
        String operator = ThreadLocalUtil.get("username");
        String message = String.format("管理员 [%s] 删除用户 [%s]", operator, operatee);
        logService.record(message, operator, OperationTypeEnum.DELETE.getType());
        log.info(message);
    }

}
