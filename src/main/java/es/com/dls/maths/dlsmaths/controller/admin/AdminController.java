package es.com.dls.maths.dlsmaths.controller.admin;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.repository.GameConfigRepository;
import es.com.dls.maths.dlsmaths.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GameConfigRepository configRepo;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showHome(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        return "admin/home";
    }

    @GetMapping("/config")
    public String configForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameConfig config = configRepo.findById(1L).orElse(new GameConfig());
        model.addAttribute("config", config);
        return "admin/config";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(!user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        List<User> users = userRepository.findAll(); // Incluye GameResults por usuario
        model.addAttribute("users", users);
        return "admin/statistics";
    }

    @PostMapping("/config")
    public String saveConfig(@ModelAttribute GameConfig config) {
        config.setId(1L);
        configRepo.save(config);
        return "redirect:/admin/config";
    }
}
