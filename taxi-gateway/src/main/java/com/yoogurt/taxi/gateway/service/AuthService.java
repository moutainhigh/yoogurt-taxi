package com.yoogurt.taxi.gateway.service;

public interface AuthService {

    String getAuthToken(String userId, String grantCode, String username, Integer userType);

    String refreshToken(String token);

    void clearCachedAuthorizationInfo(Object... userIds);
}
