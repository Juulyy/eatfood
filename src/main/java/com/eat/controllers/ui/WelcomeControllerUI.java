package com.eat.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/ui")
public class WelcomeControllerUI {

    @RequestMapping(value = {"", "/", "/welcome"}, method = RequestMethod.GET)
    public String showPlaces() {
        return "welcome";
    }

}
