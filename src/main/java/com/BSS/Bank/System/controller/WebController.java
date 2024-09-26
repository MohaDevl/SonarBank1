package com.BSS.Bank.System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage(Model model) {
        if (UserController.currentUser != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("message", "Welcome to the Banking System!");
        return "home";
    }
}