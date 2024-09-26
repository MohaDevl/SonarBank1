package com.BSS.Bank.System.controller;

import com.BSS.Bank.System.dto.RegisterDTO;
import com.BSS.Bank.System.model.User;
import com.BSS.Bank.System.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class UserController {

    public static User currentUser;
    private final UserService userService;

    // Autowiring UserService to handle user-related data operations
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Handler to display the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User());
        model.addAttribute("registerDTO", new RegisterDTO());
        return "registrationForm";  // The name of the HTML file for the registration form
    }

    // Handler to process the registration form
    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute RegisterDTO registerDTO, Model model) {
        try {
            userService.saveUser(registerDTO);  // Save the user using the UserService
            log.info("User registered successfully: {}", registerDTO);
            return "redirect:/home";     // Redirect to the home page after successful registration
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "registrationForm";  // Stay on the registration form and show the error
        }
    }

    @GetMapping("/home")
    public String home(Model model) {
        // You can add attributes to your model here if needed
        return "home";  // This should be the name of your home page template (home.html)
    }

    // Handler to display the login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (currentUser != null) {
            // If the user is already logged in, redirect to the dashboard
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "loginForm";  // The name of the HTML file for the login form
    }

    // Handler to process the login form
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute User user, Model model) {
        try {
            boolean isAuthenticated = userService.authenticateUser(user.getUsername(), user.getPassword());
            if (isAuthenticated) {
                currentUser = userService.getUserByUsername(user.getUsername());
                log.info("User logged in successfully: {}", currentUser);
                return "redirect:/dashboard";  // Redirect to the home page after successful login
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "loginForm";  // Stay on the login form and show the error
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "loginForm";  // Stay on the login form and show the error
        }

    }

    @GetMapping("/logout")
    public String logout() {
        currentUser = null;
        return "redirect:/";  // This should be the name of your dashboard template (dashboard.html)
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // You can add attributes to your model here if needed
        if (currentUser == null) {
            return "redirect:/login";
        } else {
            currentUser = userService.getUserByUsername(currentUser.getUsername());
            model.addAttribute("user", currentUser);
        }

        return "dashboard";  // This should be the name of your dashboard template (dashboard.html)
    }

}