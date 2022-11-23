package week7.learn.validator;

import java.util.Optional;

import org.springframework.stereotype.Service;

import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.exception.customException.CustomUnauthorizedException;
import week7.learn.model.entity.User;

@Service
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

    public void validateUserFound(Optional<User> userOpt) throws Exception {
        if (userOpt.isPresent()) {
            throw new CustomNotFoundException("User is found. Please login!");
        }
    }

    public void validatePassword(String passwordDB, String requestDB) throws Exception {
        if (!passwordDB.equals(requestDB)) {
            throw new CustomUnauthorizedException("Unauthorized! Password does not match!");
        }
    }
}
