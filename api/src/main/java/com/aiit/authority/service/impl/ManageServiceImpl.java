package com.aiit.authority.service.impl;

import com.aiit.authority.controller.request.ActivateUserRequest;
import com.aiit.authority.controller.request.AdminLoginRequest;
import com.aiit.authority.controller.request.AdminLogoutRequest;
import com.aiit.authority.controller.request.AdminRegisterRequest;
import com.aiit.authority.controller.request.AdminRegisterUserRequest;
import com.aiit.authority.controller.request.DeleteUserRequest;
import com.aiit.authority.controller.request.DisableUserRequest;
import com.aiit.authority.controller.request.ListAllUsersRequest;
import com.aiit.authority.controller.request.ListPendingUsersRequest;
import com.aiit.authority.controller.request.ListReviewedUsersRequest;
import com.aiit.authority.controller.request.QueryUserExistRequest;
import com.aiit.authority.controller.request.UserLoginRequest;
import com.aiit.authority.controller.request.UserLogoutRequest;
import com.aiit.authority.controller.request.UserRegisterRequest;
import com.aiit.authority.controller.response.ActivateUserResponse;
import com.aiit.authority.controller.response.AdminLoginResponse;
import com.aiit.authority.controller.response.AdminLogoutResponse;
import com.aiit.authority.controller.response.AdminRegisterResponse;
import com.aiit.authority.controller.response.AdminRegisterUserResponse;
import com.aiit.authority.controller.response.DeleteUserResponse;
import com.aiit.authority.controller.response.DisableUserResponse;
import com.aiit.authority.controller.response.ListAllUsersResponse;
import com.aiit.authority.controller.response.ListApprovedUsersResponse;
import com.aiit.authority.controller.response.ListPendingUsersResponse;
import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.controller.response.QueryUserExistResponse;
import com.aiit.authority.controller.response.UserLoginResponse;
import com.aiit.authority.controller.response.UserLogoutResponse;
import com.aiit.authority.controller.response.UserRegisterResponse;
import com.aiit.authority.controller.response.vo.UserInfoVO;
import com.aiit.authority.enums.RedisPreFixEnum;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.UserManager;
import com.aiit.authority.manager.dto.ListUsersDTO;
import com.aiit.authority.repository.entity.UserDO;
import com.aiit.authority.repository.redis.RedisUtils;
import com.aiit.authority.service.ManageService;
import com.aiit.authority.utils.EncryptionUtils;
import com.aiit.authority.utils.JwtUtils;
import com.aiit.authority.utils.LogUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ManageServiceImpl implements ManageService {

    @Resource
    private UserManager userManager;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private LogUtils logUtils;


    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {

        // 密码加密
        String cipher = EncryptionUtils.getCipher(request.getPassword());

        // 数据库插入普通用户
        boolean success = userManager.insertUser(request.getUsername(),
                cipher,
                request.getRemarkName(),
                UserPositionEnum.NORMAL_USER,
                UserStatusEnum.WAITING_APPROVAL);


        if (success) {
            // 插入日志
            logUtils.logRegisterSelf("普通用户", request.getUsername());
            return new UserRegisterResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

    }

    @Override
    public AdminRegisterResponse registerAdmin(AdminRegisterRequest request) {

        // 密码加密
        String cipher = EncryptionUtils.getCipher(request.getPassword());

        // 数据库插入管理员
        boolean success = userManager.insertUser(request.getUsername(),
                cipher,
                request.getRemarkName(),
                UserPositionEnum.ADMIN,
                // TODO：管理员初始status为1，待确认
                UserStatusEnum.ACTIVE_USER);

        if (success) {
            // 插入日志
            logUtils.logRegisterSelf("管理员", request.getUsername());
            return new AdminRegisterResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public ListAllUsersResponse listAllUsers(ListAllUsersRequest request) {

        // 传入分页相关信息和查询条件
        ListUsersDTO listUsersDTO = userManager.listUsers(request.getCurrentPage(), request.getPageSize(), null);

        // 从DTO中获取到PageInfo和DO的list
        PageInfo pageInfo = listUsersDTO.getPageInfo();
        List<UserDO> userDOList = listUsersDTO.getList();

        // 由DO列表生成VO列表
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        if (userDOList != null) {
            userInfoVOList = userDOList.stream().map(userDO -> {
                UserInfoVO userInfoVO = new UserInfoVO();
                userInfoVO.setUsername(userDO.getUsername());
                userInfoVO.setIsManager(userDO.getIsManager());
                userInfoVO.setStatus(userDO.getStatus());
                userInfoVO.setRemarkName(userDO.getRemarkName());
                return userInfoVO;
            }).collect(Collectors.toList());
        }

        // 构建响应体
        ListAllUsersResponse response = new ListAllUsersResponse();
        response.setUserInfoList(userInfoVOList);
        response.setPage(pageInfo);
        return response;
    }

    @Override
    public ListPendingUsersResponse listPendingUsers(ListPendingUsersRequest request) {

        // 规定查询条件：普通用户，未被审批
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", UserStatusEnum.WAITING_APPROVAL.getValue());
        wrapper.eq("is_manager", UserPositionEnum.NORMAL_USER.getValue());

        // 传入分页相关信息和查询条件
        ListUsersDTO listUsersDTO = userManager.listUsers(request.getCurrentPage(), request.getPageSize(), wrapper);

        // 从DTO中获取到PageInfo和DO的list
        PageInfo pageInfo = listUsersDTO.getPageInfo();
        List<UserDO> userDOList = listUsersDTO.getList();

        // 由DO列表生成VO列表
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        if (userDOList != null) {
            userInfoVOList = userDOList.stream().map(userDO -> {
                UserInfoVO userInfoVO = new UserInfoVO();
                userInfoVO.setUsername(userDO.getUsername());
                userInfoVO.setIsManager(userDO.getIsManager());
                userInfoVO.setStatus(userDO.getStatus());
                userInfoVO.setRemarkName(userDO.getRemarkName());
                return userInfoVO;
            }).collect(Collectors.toList());
        }

        // 构建响应体
        ListPendingUsersResponse response = new ListPendingUsersResponse();
        response.setUserInfoList(userInfoVOList);
        response.setPage(pageInfo);
        return response;
    }

    @Override
    public ListApprovedUsersResponse listReviewedUsers(ListReviewedUsersRequest request) {

        // 规定查询条件: 普通用户，已被审批（可能为通过审批或被禁用）
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.ne("status", UserStatusEnum.WAITING_APPROVAL.getValue());
        wrapper.eq("is_manager", UserPositionEnum.NORMAL_USER.getValue());

        // 传入分页相关信息和查询条件
        ListUsersDTO listUsersDTO = userManager.listUsers(request.getCurrentPage(), request.getPageSize(), wrapper);

        // 从DTO中获取到PageInfo和DO的list
        PageInfo pageInfo = listUsersDTO.getPageInfo();
        List<UserDO> userDOList = listUsersDTO.getList();

        // 由DO列表生成VO列表
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        if (userDOList != null) {
            userInfoVOList = userDOList.stream().map(userDO -> {
                UserInfoVO userInfoVO = new UserInfoVO();
                userInfoVO.setUsername(userDO.getUsername());
                userInfoVO.setIsManager(userDO.getIsManager());
                userInfoVO.setStatus(userDO.getStatus());
                userInfoVO.setRemarkName(userDO.getRemarkName());
                return userInfoVO;
            }).collect(Collectors.toList());
        }

        // 构建响应体
        ListApprovedUsersResponse response = new ListApprovedUsersResponse();
        response.setUserInfoList(userInfoVOList);
        response.setPage(pageInfo);
        return response;
    }

    @Override
    public ActivateUserResponse activateUser(ActivateUserRequest request) {

        // 尝试启用用户
        boolean success = userManager.updateUser(request.getUsername(), UserStatusEnum.ACTIVE_USER);

        if (success) {
            // 插入日志
            logUtils.logActivateUser(request.getUsername());
            return new ActivateUserResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public AdminRegisterUserResponse registerUserByAdmin(AdminRegisterUserRequest request) {

        // 密码加密
        String cipher = EncryptionUtils.getCipher(request.getPassword());

        // 数据库插入普通用户
        boolean success = userManager.insertUser(request.getUsername(),
                cipher,
                request.getRemarkName(),
                UserPositionEnum.NORMAL_USER,
                UserStatusEnum.ACTIVE_USER);

        if (success) {
            // 插入日志
            logUtils.logRegisterOther(request.getUsername());
            return new AdminRegisterUserResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public DeleteUserResponse deleteUser(DeleteUserRequest request) {


        // 尝试删除用户
        boolean success = userManager.deleteUser(request.getUsername());

        if (success) {
            // 插入日志
            logUtils.logDeleteUser(request.getUsername());
            return new DeleteUserResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public DisableUserResponse disableUser(DisableUserRequest request) {


        // 尝试禁用用户
        boolean success = userManager.updateUser(request.getUsername(), UserStatusEnum.BANNED_USER);

        if (success) {
            // 插入日志
            logUtils.logDisableUser(request.getUsername());
            return new DisableUserResponse(true);
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public UserLoginResponse userLogin(UserLoginRequest request) {

        UserDO user = userManager.getUser(request.getUsername());

        // 用前端传过来的的pwd生成cipher，与数据库存储的cipher比对，如果一致说明明文pwd正确
        String cipher = EncryptionUtils.getCipher(request.getPassword());

        // 用户未注册
        if (user == null) {
            throw new AuthenticateException(ResultCodeEnum.NO_SUCH_USER);
        }
        // 输入的密码错误
        else if (!user.getPassword().equals(cipher)) {
            throw new AuthenticateException(ResultCodeEnum.WRONG_PASSWORD);
        }
        // 用户被禁用
        else if (user.getStatus().equals(UserStatusEnum.BANNED_USER.getValue())) {
            throw new AuthenticateException(ResultCodeEnum.BANNED_USER);
        }
        // 用户审批中
        else if (user.getStatus().equals(UserStatusEnum.WAITING_APPROVAL.getValue())) {
            throw new AuthenticateException(ResultCodeEnum.PENDING_USER);
        }

        // 生成token
        String token = JwtUtils.createToken(request.getUsername());

        // 存入缓存
        String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + request.getUsername();
        redisUtils.set(redisTokenKey, token);

        // 通过响应体回传给前端
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);

        log.info("用户{}登陆成功",request.getUsername());

        return response;
    }

    @Override
    public AdminLoginResponse adminLogin(AdminLoginRequest request) {

        UserDO user = userManager.getUser(request.getUsername());

        // 用前端传过来的的pwd生成cipher，与数据库存储的cipher比对，如果一致说明明文pwd正确
        String cipher = EncryptionUtils.getCipher(request.getPassword());

        // 用户未注册
        if (user == null) {
            throw new AuthenticateException(ResultCodeEnum.NO_SUCH_USER);
        }
        // 输入的密码错误
        else if (!user.getPassword().equals(cipher)) {
            throw new AuthenticateException(ResultCodeEnum.WRONG_PASSWORD);
        }
        // 用户种类错误（普通用户以管理员登录）
        else if (!user.getIsManager().equals(UserPositionEnum.ADMIN.getValue())) {
            throw new AuthenticateException(ResultCodeEnum.WRONG_USER_ROLE);
        }

        // 生成token
        String token = JwtUtils.createToken(request.getUsername());

        // 存入缓存
        String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + request.getUsername();
        redisUtils.set(redisTokenKey, token);

        // 通过响应体回传给前端
        AdminLoginResponse response = new AdminLoginResponse();
        response.setToken(token);

        log.info("管理员{}登陆成功",request.getUsername());

        return response;
    }

    @Override
    public UserLogoutResponse userLogout(UserLogoutRequest request) {

        // 解析token包含的信息(payload部分采用base64加密，无需密钥即可解密)
        String jwtUsername = JwtUtils.decodeToken(request.getToken());
        // 验证jwt是否合法(是否过期，payload是否被篡改)，如果非法会抛出对应异常，由全局handler处理
        JwtUtils.verifyToken(request.getToken(), jwtUsername);

        String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + jwtUsername;
        // 登录过期会自动删除redis内的token，无需手动删除；如在token有效期内登出，需要手动删除redis内token
        if (redisUtils.get(redisTokenKey) != null) {
            // 登出时删除token
            redisUtils.delete(redisTokenKey);
        }
        log.info("用户{}登出",jwtUsername);
        return new UserLogoutResponse(true);
    }

    @Override
    public AdminLogoutResponse adminLogout(AdminLogoutRequest request) {

        // 解析token包含的信息(payload部分采用base64加密，无需密钥即可解密)
        String jwtUsername = JwtUtils.decodeToken(request.getToken());
        // 验证jwt是否合法(是否过期，payload是否被篡改)，如果非法会抛出对应异常，由全局handler处理
        JwtUtils.verifyToken(request.getToken(), jwtUsername);

        String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + jwtUsername;
        // 登录过期会自动删除redis内的token，无需手动删除；如在token有效期内登出，需要手动删除redis内token
        if (redisUtils.get(redisTokenKey) != null) {
            // 登出时删除token
            redisUtils.delete(redisTokenKey);
        }
        log.info("管理员{}登出",jwtUsername);
        return new AdminLogoutResponse(true);
    }

    @Override
    public QueryUserExistResponse queryIfUserExist(QueryUserExistRequest request) {

        // 查询用户是否存在
        QueryUserExistResponse response = new QueryUserExistResponse();
        response.setExist(userManager.getUser(request.getUsername()) != null);
        return response;

    }

}