package com.aiit.authority.manager;


import com.aiit.authority.manager.dto.ListLogsDTO;
import com.aiit.authority.repository.entity.LogDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

public interface LogManager {

    Boolean insertLog(String record, String operator, Integer operation);

    ListLogsDTO listLogs(Integer currentPage, Integer pageSize, QueryWrapper<LogDO> wrapper);

    List<LogDO> listLogs(QueryWrapper wrapper);
}
