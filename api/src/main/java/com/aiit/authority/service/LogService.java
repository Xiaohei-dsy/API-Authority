package com.aiit.authority.service;

import com.aiit.authority.controller.request.ListAllLogsRequest;
import com.aiit.authority.controller.response.ListAllLogsResponse;

public interface LogService {

    ListAllLogsResponse listAllLogs(ListAllLogsRequest request);

    void record(String record, String operator, int operation);

}
