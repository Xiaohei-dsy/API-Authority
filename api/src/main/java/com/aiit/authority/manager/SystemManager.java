package com.aiit.authority.manager;

import com.aiit.authority.manager.dto.ListSystemDTO;
import com.aiit.authority.repository.entity.SystemDO;

public interface SystemManager {

    Boolean insertSystem(String systemId, String description);

    SystemDO getSystem(String systemId);

    ListSystemDTO listSystem(Integer currentPage, Integer pageSize);

    Boolean deleteSystem(String systemId);

    Boolean updateSystem(String systemId,String description);

}
