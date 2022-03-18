package com.aiit.authority.service;

import com.aiit.authority.controller.request.AddResourceRequest;
import com.aiit.authority.controller.request.ListResourceRequest;
import com.aiit.authority.controller.response.AddResourceResponse;
import com.aiit.authority.controller.response.ListResourceResponse;

public interface ResourceService {

    AddResourceResponse insertResource(AddResourceRequest request);

    ListResourceResponse listResources(ListResourceRequest request);
}
