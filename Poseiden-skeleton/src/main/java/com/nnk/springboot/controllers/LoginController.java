package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller handling authentication pages and simple user listing for secure area.
 * Provides endpoints for login page, secure user listing and a generic error page.
 */
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Display the custom login page.
     * This endpoint returns the view name for the login form used by Spring Security.
     *
     * @return ModelAndView configured with the login view
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     * Display a secure page showing all users. This endpoint is intended to be
     * protected by Spring Security and demonstrates access to repository data
     * in a secured context.
     *
     * @return ModelAndView configured with the user list view and model attribute "users"
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Display a generic error page when access is denied or an authorization
     * failure occurs. The view is populated with a human-readable error message.
     *
     * @return ModelAndView configured with the 403 error view and an error message
     */
    @GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}
