package io.juakali.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Maps URLs to Thymeleaf view names under src/main/resources/templates.
 */
@Controller
public class PageController {

  @GetMapping("/")
  String home() {
    return "home";
  }

  @GetMapping("/public")
  String publicPage() {
    return "public";
  }

  @GetMapping("/profile")
  String profile() {
    return "profile";
  }

  @GetMapping("/admin")
  String admin() {
    return "admin";
  }

  @GetMapping("/login")
  String login() {
    return "login";
  }
}
