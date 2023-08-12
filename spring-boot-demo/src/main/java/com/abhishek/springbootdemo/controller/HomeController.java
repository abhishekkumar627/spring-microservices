package com.abhishek.springbootdemo.controller;

import com.abhishek.springbootdemo.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "Hello Abhishek Kumar You are here again 1";
    }

    //@RequestMapping("/user")
    //@RequestMapping(method = RequestMethod.GET,path = "/user")
    @GetMapping("/user")
    public User user(){
        User user = new User();
        user.setId(1);
        user.setName("Abhishek Kumar");
        user.setEmailId("abhishek.kumar627@gmail.com");
        return user;
    }

    @GetMapping("/user/{id1}/{id2}")
    public String pathVariable(@PathVariable("id1") String name,@PathVariable("id2") String rollno){
        return "Path Variable 1: "+ name+ " and 2  : "+ rollno;
    }

    @GetMapping("/user/requestParam")
    public String requestParam(@RequestParam(defaultValue = "",required = false,name = "name") String name,@RequestParam("email") String email){
        return "Request Param Variable Name: "+ name+ "\n and Email : "+ email;
    }
}
