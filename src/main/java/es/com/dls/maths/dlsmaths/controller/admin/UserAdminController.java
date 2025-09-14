package es.com.dls.maths.dlsmaths.controller.admin;

import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("userForm", userRepository.findById(id).orElseThrow(RuntimeException::new));
        return "admin/user-form";
    }

    @GetMapping("/new")
    public String newUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("userForm", new User());
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
