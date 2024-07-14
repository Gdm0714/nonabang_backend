package inje.nonabang.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    @GetMapping("/protected")
    public String protectedEndpoint(@AuthenticationPrincipal OAuth2User user) {
        return "Hello, " + user.getAttribute("name") + "! This is a protected endpoint.";
    }
}

