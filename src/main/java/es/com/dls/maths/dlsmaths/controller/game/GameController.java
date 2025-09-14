package es.com.dls.maths.dlsmaths.controller.game;

import es.com.dls.maths.dlsmaths.entity.User;

import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/game")
public class GameController {

    @GetMapping("/select")
    public String selectGame(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        if(user.getAdditionEnabled() && user.getMultiplicationEnabled()){
            return "game/select-game";
        }

        if(user.getMultiplicationEnabled()){
            return "redirect:/multiplication/select-mode";
        }

        if(user.getAdditionEnabled()){
            return "redirect:/addition";
        }

        return "game/select-game";
    }

}