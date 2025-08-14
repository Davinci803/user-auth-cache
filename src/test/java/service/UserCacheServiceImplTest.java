package service;

import org.example.Dto.UserCredentials;
import org.example.service.UserCacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserCacheServiceImplTest {

    private ConcurrentHashMap<String, String> cache;
    private BCryptPasswordEncoder encoder;
    private UserCacheServiceImpl authService;

    @BeforeEach
    void setUp() {
        cache = new ConcurrentHashMap<>();
        encoder = Mockito.spy(new BCryptPasswordEncoder());
        authService = new UserCacheServiceImpl(cache, encoder);
    }

    @Test
    void testCacheUserStoresHashedPassword() {
        UserCredentials request = new UserCredentials();
        request.setUsername("user1");
        request.setPassword("password123");

        authService.cacheUser(request);

        assertTrue(cache.containsKey("user1"));
        assertNotEquals("password123", cache.get("user1"));
    }

    @Test
    void testAuthenticateCorrectPassword() {
        UserCredentials request = new UserCredentials();
        request.setUsername("user1");
        request.setPassword("password123");
        authService.cacheUser(request);

        assertTrue(authService.authenticate(request));
    }

    @Test
    void testAuthenticateWrongPassword() {
        UserCredentials request = new UserCredentials();
        request.setUsername("user1");
        request.setPassword("password123");
        authService.cacheUser(request);

        UserCredentials wrongRequest = new UserCredentials();
        wrongRequest.setUsername("user1");
        wrongRequest.setPassword("wrong");

        assertFalse(authService.authenticate(wrongRequest));
    }

    @Test
    void testAuthenticateNonExistentUser() {
        UserCredentials request = new UserCredentials();
        request.setUsername("nonexistent");
        request.setPassword("password");

        assertFalse(authService.authenticate(request));
    }

    @Test
    void testGetAllUsernames() {
        UserCredentials req1 = new UserCredentials();
        req1.setUsername("user1");
        req1.setPassword("pass1");
        authService.cacheUser(req1);

        UserCredentials req2 = new UserCredentials();
        req2.setUsername("user2");
        req2.setPassword("pass2");
        authService.cacheUser(req2);

        List<String> usernames = authService.getAllUsernames();

        assertTrue(usernames.contains("user1"));
        assertTrue(usernames.contains("user2"));
    }
}