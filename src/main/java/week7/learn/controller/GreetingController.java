package week7.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "username", defaultValue = "World") String name) {
        return String.format("Hello %s! ", name);
    }

    @GetMapping("/hello2/{name}")
    public String hello2(@PathVariable("name") String name) {
        return String.format("Hello %s from path variable! ", name);
    }

    @GetMapping("/hello3")
    public String hello3(@RequestBody String name) {
        return String.format("Hello %s from request body! ", name);
    }
}
