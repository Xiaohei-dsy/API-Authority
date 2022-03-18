package com.aiit.authority.service;

import com.aiit.authority.controller.request.AddSystemRequest;
import com.aiit.authority.controller.request.DeleteSystemRequest;
import com.aiit.authority.controller.request.ListSystemRequest;
import com.aiit.authority.controller.request.UpdateSystemRequest;
import com.aiit.authority.controller.response.AddSystemResponse;
import com.aiit.authority.controller.response.DeleteSystemResponse;
import com.aiit.authority.controller.response.ListSystemPageResponse;
import com.aiit.authority.controller.response.UpdateSystemResponse;

public interface SystemService {

    AddSystemResponse insertSystem(AddSystemRequest addSystemRequest);

    ListSystemPageResponse listSystem(ListSystemRequest listSystemRequest);

    DeleteSystemResponse deleteSystem(DeleteSystemRequest deleteSystemRequest);

    UpdateSystemResponse updateSystem(UpdateSystemRequest updateSystemRequest);

}
