package com.buutruong.ecommerce.features.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2/code")
public class OAuth2Controller {
    @GetMapping("/github")
    public String github() {
        System.out.println("github");
        return "TEST";
    }

    @GetMapping("/google")
    public String google() {
        System.out.println("google");
        return "TEST GOOGLE";
    }
}
