package week7.learn.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import week7.learn.model.dto.request.LoginDto;
import week7.learn.model.dto.request.RegisterDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.model.entity.DetailUser;
import week7.learn.model.entity.User;
import week7.learn.repository.DetailUserRepository;
import week7.learn.repository.UserRepository;
import week7.learn.validator.UserValidator;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DetailUserRepository detailUserRepository;

    private ResponseData<Object> responseData;
    private User user;
    private DetailUser detailUser;
    private Map<Object, Object> data;

    @Autowired
    private UserValidator userValidator;

    @Override
    public ResponseData<Object> register(RegisterDto request) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        userValidator.validateUserFound(userOpt);

        user = new User(request.getEmail(), request.getPassword());
        userRepository.save(user);

        detailUser = new DetailUser(request.getFirstName(), request.getLastName(), request.getPhoneNumber());
        detailUser.setUser(user);
        detailUser.setUserEmail(user);
        detailUserRepository.save(detailUser);

        data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("firstName", detailUser.getFirstName());
        data.put("lastName", detailUser.getLastName());
        data.put("phoneNumber", detailUser.getPhoneNumber());

        responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Success registerd", data);

        return responseData;
    }

    @Override
    public ResponseData<Object> login(LoginDto request) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        userValidator.validateUserNotFound(userOpt);

        user = userOpt.get();

        userValidator.validatePassword(request.getPassword(), user.getPassword());

        data = new HashMap<>();
        data.put("email", user.getEmail());

        responseData = new ResponseData<Object>(HttpStatus.OK.value(), "Success Login", data);

        return responseData;
    }

    @Override
    public ResponseData<Object> updateUser(RegisterDto request) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        userValidator.validateUserNotFound(userOpt);

        user = userOpt.get();

        Optional<DetailUser> detailUserOpt = detailUserRepository.findByUser(user);

        if (detailUserOpt.isPresent()) {
            detailUser = detailUserOpt.get();
            detailUser.setFirstName(request.getFirstName());
            detailUser.setLastName(request.getLastName());
            detailUser.setPhoneNumber(request.getPhoneNumber());
            detailUser.setUserEmail(user);
            detailUserRepository.save(detailUser);
        } else {
            detailUser = new DetailUser(request.getFirstName(), request.getLastName(),
                    request.getPhoneNumber());
            detailUser.setUser(user);
            detailUser.setUserEmail(user);
            detailUserRepository.save(detailUser);
        }

        data = new HashMap<>();
        data.put("firstName", detailUser.getFirstName());
        data.put("lastName", detailUser.getLastName());
        data.put("phoneNumber", detailUser.getPhoneNumber());

        responseData = new ResponseData<Object>(HttpStatus.OK.value(), "Success Update Data", data);

        return responseData;
    }

}
