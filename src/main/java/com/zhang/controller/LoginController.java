package com.zhang.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 登录controller
 *
 */
@Controller
public class LoginController {

    /**
     * 登录
     * @return
     */
    @RequestMapping("login")
    public String login(){
        System.out.println("登录");
        return "redirect:main.html";
    }

    /**
     * 页面跳转
     * @return
     */
//    @Secured("ROLE_abc")
    //PreAuthorize允许角色以ROLE_开头，也可以不以ROLE_开头，但是配置类不允许以ROLE_开头
    @PreAuthorize("hasRole('ROLE_abc')")
    @RequestMapping("toMain")
    public String toMain(){
        return "redirect:main.html";
    }

    /**
     * 页面跳转
     * @return
     */
    @RequestMapping("toError")
    public String toError(){
        return "redirect:error.html";
    }

    /**
     * 页面跳转
     * @return
     */
    @GetMapping("demo")
    public String demo(){
        return "demo";
    }

    /**
     * 页面跳转
     * @return
     */
    @GetMapping("/showLogin")
    public String showLogin(){
        return "login";
    }

}
