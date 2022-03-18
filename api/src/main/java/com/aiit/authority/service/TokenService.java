package com.aiit.authority.service;

import com.aiit.authority.service.dto.TokenInfoDTO;

public interface TokenService {

    TokenInfoDTO validateToken(String token);

    String decodeToken(String token);
}
