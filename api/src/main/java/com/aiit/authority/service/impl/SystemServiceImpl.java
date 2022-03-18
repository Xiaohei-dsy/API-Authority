package com.aiit.authority.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aiit.authority.controller.request.AddSystemRequest;
import com.aiit.authority.controller.request.DeleteSystemRequest;
import com.aiit.authority.controller.request.ListSystemRequest;
import com.aiit.authority.controller.request.UpdateSystemRequest;
import com.aiit.authority.controller.response.*;
import com.aiit.authority.controller.response.vo.SystemInfoVO;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.manager.dto.ListSystemDTO;
import com.aiit.authority.repository.entity.SystemDO;
import com.aiit.authority.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

    @Resource
    private SystemManager systemManager;

    @Override
    public AddSystemResponse insertSystem(AddSystemRequest addSystemRequest) {
        AddSystemResponse addSystemResponse = new AddSystemResponse();
        boolean result = systemManager.insertSystem(addSystemRequest.getSystemId(), addSystemRequest.getDescription());
        if (result) {
            addSystemResponse.setMessage(ResultCodeEnum.ADD_SYSTEM_SUCCESS.getMessage());
        } else {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
        return addSystemResponse;
    }

    @Override
    public ListSystemPageResponse listSystem(ListSystemRequest listSystemRequest) {
        ListSystemPageResponse listSystemPageResponse = new ListSystemPageResponse();
        List<SystemInfoVO> systemInfoVOList = new ArrayList<>();

        // 提取DTO类信息
        ListSystemDTO listSystemDTO =
            systemManager.listSystem(listSystemRequest.getCurrentPage(), listSystemRequest.getPageSize());

        PageInfo pageInfo = listSystemDTO.getPageInfo();

        List<SystemDO> systemDOList = listSystemDTO.getList();

        // 设置系统信息的前端VO类
        if (systemDOList != null) {
            systemInfoVOList = systemDOList.stream().map(systemDO -> {
                SystemInfoVO systemInfoVO = new SystemInfoVO();
                systemInfoVO.setSystemId(systemDO.getSystemId());
                systemInfoVO.setDescription(systemDO.getDescription());
                return systemInfoVO;
            }).collect(Collectors.toList());
        }

        listSystemPageResponse.setSystemInfoList(systemInfoVOList);
        listSystemPageResponse.setPage(pageInfo);
        return listSystemPageResponse;
    }

    @Override
    public DeleteSystemResponse deleteSystem(DeleteSystemRequest deleteSystemRequest) {
        DeleteSystemResponse deleteSystemResponse = new DeleteSystemResponse();
        Boolean result = systemManager.deleteSystem(deleteSystemRequest.getSystemId());
        deleteSystemResponse.setSuccess(result);
        return deleteSystemResponse;
    }

    @Override
    public UpdateSystemResponse updateSystem(UpdateSystemRequest updateSystemRequest) {
        UpdateSystemResponse updateSystemResponse = new UpdateSystemResponse();
        Boolean result =
            systemManager.updateSystem(updateSystemRequest.getSystemId(), updateSystemRequest.getDescription());
        updateSystemResponse.setSuccess(result);
        return updateSystemResponse;
    }

}