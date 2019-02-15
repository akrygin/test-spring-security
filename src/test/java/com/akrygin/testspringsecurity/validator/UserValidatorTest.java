package com.akrygin.testspringsecurity.validator;

import com.akrygin.testspringsecurity.model.User;
import com.akrygin.testspringsecurity.repository.UserRepository;
import com.akrygin.testspringsecurity.service.UserService;
import com.akrygin.testspringsecurity.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserValidatorTest {

    @Autowired
    private UserRepository userRepository;
    private UserValidator userValidator;
    private User emptyUser;
    private User validUser;
    private User userInvalidPassword;
    private User duplicateUser;

    @Before
    public void setUp(){
        userRepository.save(new User("akrygin", new BCryptPasswordEncoder().encode("Test12345_")));

        emptyUser = new User("", "");
        validUser = new User("akrygin2", "Test12345_");
        userInvalidPassword = new User("test", "test");
        duplicateUser = new User("akrygin", "Test12345_");

        UserService userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder());
        userValidator = new UserValidator(userService);
    }

    @Test
    public void testSupports(){
        assertTrue(userValidator.supports(User.class));
    }

    @Test
    public void testValidateCorrectCredentials(){
        Errors validObject = new BeanPropertyBindingResult(validUser, "validUser");
        userValidator.validate(validUser, validObject);
        assertFalse(validObject.hasErrors());
    }

    @Test
    public void testValidateEmptyUsernamePassword(){
        Errors errorsEmpty = new BeanPropertyBindingResult(emptyUser, "emptyUser");
        userValidator.validate(emptyUser, errorsEmpty);
        assertTrue(errorsEmpty.hasErrors());
        assertEquals(2, errorsEmpty.getAllErrors().stream()
                .filter(error -> "NotEmpty".equals(error.getCode()))
                .count());
    }

    @Test
    public void testValidatePasswordErrors(){
        Errors errorsPassword = new BeanPropertyBindingResult(validUser, "validUser");
        userValidator.validate(userInvalidPassword, errorsPassword);
        assertTrue(errorsPassword.hasErrors());
        assertEquals(1, errorsPassword.getErrorCount());
        errorsPassword.getFieldErrors()
                .forEach(error -> assertEquals("password", error.getField()));
        assertEquals(1, compareResultWithPossibleError(
                "Password must be at least 8 characters in length.",
                errorsPassword));
        assertEquals(1, compareResultWithPossibleError(
                "Password must contain at least 1 uppercase characters.",
                errorsPassword));
        assertEquals(1, compareResultWithPossibleError(
                "Password must contain at least 1 digit characters.",
                errorsPassword));
        assertEquals(1, compareResultWithPossibleError(
                "Password must contain at least 1 special characters.",
                errorsPassword));
    }

    @Test
    public void testValidateDuplicateUser(){
        Errors duplicateUserError = new BeanPropertyBindingResult(duplicateUser, "duplicateUser");
        userValidator.validate(duplicateUser, duplicateUserError);
        assertTrue(duplicateUserError.hasErrors());
        assertEquals(1, duplicateUserError.getErrorCount());
        assertEquals("username", Objects.requireNonNull(duplicateUserError.getFieldError()).getField());
    }

    private long compareResultWithPossibleError(String errorMessage, Errors errorsPassword) {
        return errorsPassword.getFieldErrors().stream()
                .filter(x -> Objects.requireNonNull(x.getDefaultMessage()).contains(errorMessage))
                .count();
    }
}
