package com.akrygin.testspringsecurity.service;

import com.akrygin.testspringsecurity.model.User;
import com.akrygin.testspringsecurity.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecurityServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    private SecurityService securityService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Before
    public void setUp() {
        userRepository.save(new User("akrygin", new BCryptPasswordEncoder().encode("Test12345_")));
        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository);
        securityService = new SecurityServiceImpl(authenticationManager, userDetailsService);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testAutoLoginFailed(){
        securityService.autoLogin("akrygin2", "Test12345_");
    }

    @Test
    public void testAutoLoginSuccess() {
        securityService.autoLogin("akrygin", "Test12345_");
    }
}
