package es.com.dls.maths.dlsmaths.controller;


import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String init(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam Long userId,
                        @RequestParam(required = false) String password,
                        Model model,
                        HttpSession session) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        session.setAttribute("user", user);

        if (RoleEnum.CHILD.equals(user.getRole())) {
            return "redirect:/game/select";
        }

        if (RoleEnum.ADMIN.equals(user.getRole())) {
            if (user.getPassword().equals(password)) {
                return "redirect:/admin";
            } else {
                model.addAttribute("users", userRepo.findAll());
                model.addAttribute("error", "Contraseña incorrecta");
                model.addAttribute("selectedUser", user.getId());
                return "auth/login";
            }
        }

        return "redirect:/login";
    }
}