package com.akrygin.testspringsecurity.service;

import com.akrygin.testspringsecurity.model.User;
import com.akrygin.testspringsecurity.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDetailsServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @Before
    public void setUp() {
        userRepository.save(new User("akrygin", new BCryptPasswordEncoder().encode("Test12345_")));
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testUserNotFoundByUsername() {
        String username = "someUser";
        userDetailsService.loadUserByUsername(username);
    }

    @Test
    public void testFindUserByUserName(){
        UserDetails userDetails = userDetailsService.loadUserByUsername("akrygin");
        assertEquals("akrygin", userDetails.getUsername());
    }
}
