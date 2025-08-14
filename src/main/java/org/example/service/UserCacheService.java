package org.example.service;


import org.example.dto.UserCredentials;

import java.util.List;

public interface UserCacheService {
    void cacheUser(UserCredentials request);
    boolean authenticate(UserCredentials request);
    List<String> getAllUsernames();
}
