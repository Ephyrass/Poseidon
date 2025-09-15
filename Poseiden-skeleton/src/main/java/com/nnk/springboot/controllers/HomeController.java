package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for application home endpoints.
 * Provides the home view mapping used as the default landing page after authentication.
 */
@Controller
public class HomeController {
    /**
     * Return the home view.
     *
     * @return the name of the home view template
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }
}
