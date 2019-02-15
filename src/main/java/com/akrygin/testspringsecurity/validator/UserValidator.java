package com.akrygin.testspringsecurity.validator;

import com.akrygin.testspringsecurity.model.User;
import com.akrygin.testspringsecurity.service.UserService;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class UserValidator implements Validator {
    private final UserService userService;
    private String passwordErrors;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, Errors errors) {
        final User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (!passwordIsValid(user.getPassword())) {
            errors.rejectValue("password", "Invalid.userForm.password", passwordErrors);
        }
    }

    private boolean passwordIsValid(String password) {
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 20),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new AlphabeticalSequenceRule(3,false)));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()){
            return true;
        } else {
            passwordErrors = String.join(", ", validator.getMessages(result));
            return false;
        }
    }
}
