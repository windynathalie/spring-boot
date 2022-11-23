package week7.learn.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import week7.learn.model.dto.request.LoginDto;
import week7.learn.model.dto.request.RegisterDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private ResponseData<Object> responseData;

    @PostMapping
    public ResponseEntity<Object> signUp(@RequestBody @Valid RegisterDto request) throws Exception {
        responseData = userService.register(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto request) throws Exception {
        responseData = userService.login(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid RegisterDto request) throws Exception {
        responseData = userService.updateUser(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
}
