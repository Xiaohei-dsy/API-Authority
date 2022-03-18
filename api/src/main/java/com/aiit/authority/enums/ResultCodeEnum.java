package com.aiit.authority.enums;

import lombok.Getter;

/**
 * 枚举信息：错误code/message
 */
@Getter
public enum ResultCodeEnum {

    // 成功代码
    SUCCESS("00000", "执行成功"),
    ACTIVATE_USER_SUCCESS("00000", "审批通过，启用用户成功"),
    USER_REGISTER_SUCCESS("00000", "注册用户成功"),
    ADMIN_REGISTER_SUCCESS("00000", "注册管理员成功"),
    DELETE_USER_SUCCESS("00000", "删除用户成功"),
    DISABLE_USER_SUCCESS("00000", "用户禁用成功"),
    USER_LOGIN_SUCCESS("00000", "用户登录成功"),
    ADMIN_LOGIN_SUCCESS("00000", "管理员登录成功"),
    USER_LOGOUT_SUCCESS("00000", "用户登出成功"),
    ADMIN_LOGOUT_SUCCESS("00000", "管理员登出成功"),
    ADD_SYSTEM_SUCCESS("00000", "添加系统成功"),

    // 失败代码
    CLIENT_ERROR("A0001", "用户端错误"),
    EMPTY_PARAMETER("A0410","必填字段为空"),
    NO_TOKEN_IN_REQUEST("A0224", "请求头未包含token"),
    IDENTITY_VERIFICATION_FAILED("A0220", "身份校验失败"),
    DUPLICATE_USER("A0111 ", "用户已存在"),
    NO_SUCH_USER("A0201", "用户不存在"),
    NO_SUCH_SYSTEM("A0201", "系统不存在"),
    NO_SUCH_RESOURCE("A0201", "资源不存在"),
    NO_SUCH_ROLE_SYSTEM("A0201", "该角色/系统组合不存在"),
    NO_SUCH_USER_ROLE("A0201","该用户此角色信息不存在"),
    NO_MORE_ROLE("A0111 ", "该用户此系统已存在角色，无法再添加。"),
    WRONG_PASSWORD("A0210", "用户密码错误"),
    BANNED_USER("A0303", "用户已被禁用"),
    PENDING_USER("A0302", "用户授权进行中"),
    WRONG_USER_ROLE("A0300", "用户种类错误"),
    OPERATION_TO_SELF("A0300", "无法对自己操作"),
    REDIS_POOL_FAILED("C0200", "连接池错误"),
    REDIS_DELETE_FAILED("C0130","redis删除用户token失败"),
    REDIS_SET_FAILED("C0130","redis添加用户token失败"),
    REDIS_GET_FAILED("C0130","redis获取用户token失败"),
    REDIS_GET_LEFT_TIME_FAILED("C0130","redis获取token剩余时间失败"),
    UNKNOWN_DATABASE_ERROR("C0300", "未知数据库异常"),
    SAVE_LOG_FAILED("C0300","log插入数据库失败"),
    DUPLICATE_SYSTEM("A0351", "系统已存在"),
    DUPLICATE_ROLE_SYSTEM("A0351","该角色/系统组合已存在"),
    DUPLICATE_RESOURCE("A0351", "资源已存在"),
    DUPLICATE_USER_ROLE("A0351","该用户此角色信息已存在"),
    DUPLICATE_ROLE_RESOURCE("A0351","该角色此资源已存在"),
    OVER_NUM_RESOURCE("A0351","角色绑定的资源超过上限"),
    ILLEGAL_PARAM("A0421", "参数校验失败"),
    SERVER_ERROR("B0001", "系统错误"),
    THIRD_PARTY_ERROR("C0001", "第三方服务出错"),
    ;

    private String code;
    private String message;

    private ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
