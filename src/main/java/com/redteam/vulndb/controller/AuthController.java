package com.redteam.vulndb.controller;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.repository.OperatorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for authentication endpoints.
 */
@Controller
public class AuthController {

    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(OperatorRepository operatorRepository, PasswordEncoder passwordEncoder) {
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String redirectToProjects() {
        return "redirect:/projects";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        if (operatorRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Bu kullanıcı adı zaten alınmış!");
            return "register";
        }

        Operator operator = new Operator();
        operator.setUsername(username);
        operator.setPassword(passwordEncoder.encode(password));
        operatorRepository.save(operator);

        return "redirect:/login?registered=true";
    }
}
