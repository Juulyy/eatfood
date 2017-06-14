package com.eat.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/ui")
public class AuthenticationControllerUI {

    @GetMapping(value = "/login", produces = "application/json")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/logout", produces = "application/json")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/ui/login";
    }

}