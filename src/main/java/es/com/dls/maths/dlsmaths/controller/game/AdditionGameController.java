package es.com.dls.maths.dlsmaths.controller.game;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.entity.GameResult;
import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.GameConfigs;
import es.com.dls.maths.dlsmaths.enums.OperationEnum;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.mapper.GameMapper;
import es.com.dls.maths.dlsmaths.model.GameSession;
import es.com.dls.maths.dlsmaths.model.Question;
import es.com.dls.maths.dlsmaths.model.QuestionAbstract;
import es.com.dls.maths.dlsmaths.repository.GameConfigRepository;
import es.com.dls.maths.dlsmaths.repository.GameResultRepository;
import es.com.dls.maths.dlsmaths.repository.UserRepository;
import es.com.dls.maths.dlsmaths.util.AdditionQuestionGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/addition")
public class AdditionGameController {

    @Autowired
    private GameConfigRepository gameConfigRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameMapper gameMapper;

    @GetMapping
    public String showAdditionGame(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        List<Question> questions =
                AdditionQuestionGenerator.generate(10, user.getAdditionMaxDigits());

        model.addAttribute("questions", questions);

        return "game/addition/addition-play";
    }

    @GetMapping("/start")
    public String startGame(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión

        GameConfig config = getActualConfig();
        session.setAttribute("game", new GameSession(user.getAdditionMaxDigits(), config));
        return "redirect:/addition/play";
    }

    @GetMapping("/play")
    public String play(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameSession game = (GameSession) session.getAttribute("game");
        if (game == null) return "redirect:/addition";

        if (game.isFinished()) {
            return "redirect:/addition/summary";
        }

        GameConfig config = getActualConfig();
        int currentQuestionNumber = game.getCurrentQuestionNumber();
        QuestionAbstract question = game.getNextQuestion(currentQuestionNumber);

//        List<Integer> answers = getAnswersFromQuestion(question, config);

        model.addAttribute("question", question);
        model.addAttribute("current", currentQuestionNumber);
        model.addAttribute("total", game.getTotalQuestions());

        model.addAttribute("timeLimit", config.getTimeLimitSeconds());
        model.addAttribute("enableTimer", config.isEnableTimer());
//        model.addAttribute("answers", answers);

        return "game/addition/addition-play"; // tu plantilla con las opciones y el contador
    }

    @PostMapping("/submit")
    public String submit(@RequestParam("a") int a,
                         @RequestParam("b") int b,
                         @RequestParam("answer") int answer,
                         @RequestParam("time") int time,
                         HttpSession session) {
        GameConfig config = getActualConfig();
        GameSession game = (GameSession) session.getAttribute("game");
        if (game != null) {
            game.submitAnswer(a, b, answer, config.getTimeLimitSeconds()-time, OperationEnum.ADDITION, config);
        }
        return "redirect:/addition/play";
    }

    @GetMapping("/summary")
    public String summary(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameSession game = (GameSession) session.getAttribute("game");
        if (game == null) return "redirect:/addition/select-mode";

        BigDecimal actualPoints = Optional.of(user).map(User::getPoints).orElse(BigDecimal.ZERO);
        user.setPoints(actualPoints.add(game.getPoints()));
        userRepository.save(user);
        model.addAttribute("user", user);

        GameResult result = gameMapper.toEntity(game, user);
        gameResultRepository.save(result);

        model.addAttribute("percent", game.getPercent());
        model.addAttribute("answers", game.getAnswers());
        model.addAttribute("operation", OperationEnum.ADDITION.name());
        return "game/summary";
    }

    private GameConfig getActualConfig(){
        return gameConfigRepository.findByDescriptionIgnoreCase(GameConfigs.ADDITION.name())
                .orElse(GameConfig
                        .builder()
                        .timeLimitSeconds(5)
                        .enableTimer(true)
                        .rangeIni(2)
                        .rangeFin(12)
                        .build());
    }

}
