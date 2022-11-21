package week7.learn.service;

import week7.learn.model.dto.request.UserDto;
import week7.learn.model.dto.response.ResponseData;

public interface UserService {
    ResponseData<Object> register(UserDto request);

    ResponseData<Object> login(UserDto request);

    ResponseData<Object> updateUser(UserDto request);
}
