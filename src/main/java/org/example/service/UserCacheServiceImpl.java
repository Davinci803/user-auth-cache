package org.example.service;

import org.example.Dto.UserCredentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserCacheServiceImpl implements UserCacheService {

    private final ConcurrentHashMap<String, String> userCache;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserCacheServiceImpl(ConcurrentHashMap<String, String> userCache, BCryptPasswordEncoder passwordEncoder) {
        this.userCache = userCache;
        this.passwordEncoder = passwordEncoder;

        userCache.put("admin", passwordEncoder.encode("admin123"));
    }

    @Override
    public void cacheUser(UserCredentials request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        userCache.put(request.getUsername(), hashedPassword);
    }

    @Override
    public boolean authenticate(UserCredentials request) {
        String storedHash = userCache.get(request.getUsername());
        return storedHash != null && passwordEncoder.matches(request.getPassword(), storedHash);
    }

    @Override
    public List<String> getAllUsernames() {
        return new ArrayList<>(userCache.keySet());
    }
}