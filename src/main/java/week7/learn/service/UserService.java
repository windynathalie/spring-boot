package week7.learn.service;

import week7.learn.model.dto.request.LoginDto;
import week7.learn.model.dto.request.RegisterDto;
import week7.learn.model.dto.response.ResponseData;

public interface UserService {
    ResponseData<Object> register(RegisterDto request) throws Exception;

    ResponseData<Object> login(LoginDto request) throws Exception;

    ResponseData<Object> updateUser(RegisterDto request) throws Exception;
}
