package es.com.dls.maths.dlsmaths.controller.admin;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.repository.GameConfigRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/configs")
@RequiredArgsConstructor
public class ConfigAdminController {

    private final GameConfigRepository gameConfigRepository;

    @GetMapping
    public String listConfigs(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("configs", gameConfigRepository.findAll());
        return "admin/configs";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("config", gameConfigRepository.findById(id).orElseThrow(RuntimeException::new));
        return "admin/config-form";
    }

    @GetMapping("/new")
    public String newUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        model.addAttribute("config", new GameConfig());
        return "admin/config-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute GameConfig config) {
        gameConfigRepository.save(config);
        return "redirect:/admin/configs";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        gameConfigRepository.deleteById(id);
        return "redirect:/admin/configs";
    }
}
