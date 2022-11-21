package week7.learn.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.model.entity.User;

@Component
public class UserValidator {
    public void validateUserNotFound(Optional<User> userOpt) throws Exception {
        if (userOpt.isEmpty()) {
            throw new CustomNotFoundException("User not found. Please input correct user");
        }
    }

    public void validateUserInactive(User user) throws Exception {
        if (user.getIsDeleted().equals(true)) {
            throw new Exception("User is inactive");
        }
    }
}
