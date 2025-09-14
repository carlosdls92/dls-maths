package es.com.dls.maths.dlsmaths.controller.game;

import es.com.dls.maths.dlsmaths.entity.GameConfig;
import es.com.dls.maths.dlsmaths.entity.GameResult;
import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.enums.GameConfigs;
import es.com.dls.maths.dlsmaths.enums.MultiplicationMode;
import es.com.dls.maths.dlsmaths.enums.OperationEnum;
import es.com.dls.maths.dlsmaths.enums.RoleEnum;
import es.com.dls.maths.dlsmaths.mapper.GameMapper;
import es.com.dls.maths.dlsmaths.model.GameSession;
import es.com.dls.maths.dlsmaths.model.Option;
import es.com.dls.maths.dlsmaths.model.QuestionAbstract;
import es.com.dls.maths.dlsmaths.repository.GameConfigRepository;
import es.com.dls.maths.dlsmaths.repository.GameResultRepository;
import es.com.dls.maths.dlsmaths.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/multiplication")
public class MultiplicationGameController {

    @Autowired
    private GameConfigRepository gameConfigRepository;
    @Autowired
    private GameResultRepository gameResultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameMapper gameMapper;

    @GetMapping("/select-mode")
    public String selectMode(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameConfig config = getActualConfig();
        List<Option> listModes = getModesFromConfig(config);

        model.addAttribute("modes", listModes);
        return "game/multiplication/select-mode";
    }

    @GetMapping("/start")
    public String startGame(@RequestParam("mode") String modeStr,
                            @RequestParam(value = "table", required = false) Integer table,
                            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión

        GameConfig config = getActualConfig();
        MultiplicationMode mode = MultiplicationMode.valueOf(modeStr);
        int tableValue = (mode == MultiplicationMode.SPECIFIC && table != null) ? table : 0;
        session.setAttribute("game", new GameSession(mode, tableValue, config));
        return "redirect:/multiplication/play";
    }

    @GetMapping("/play")
    public String play(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameSession game = (GameSession) session.getAttribute("game");
        if (game == null) return "redirect:/multiplication/select-mode";

        if (game.isFinished()) {
            return "redirect:/multiplication/summary";
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

        return "game/multiplication/game-play"; // tu plantilla con las opciones y el contador
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
            game.submitAnswer(a, b, answer, config.getTimeLimitSeconds()-time, OperationEnum.MULTIPLICATION, config);
        }
        return "redirect:/multiplication/play";
    }

    @GetMapping("/summary")
    public String summary(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login"; // Redirección si no hay sesión
        if(user.getRole().equals(RoleEnum.ADMIN)) return "redirect:/login";
        model.addAttribute("user", user);

        GameSession game = (GameSession) session.getAttribute("game");
        if (game == null) return "redirect:/multiplication/select-mode";

        BigDecimal actualPoints = Optional.of(user).map(User::getPoints).orElse(BigDecimal.ZERO);
        user.setPoints(actualPoints.add(game.getPoints()));
        userRepository.save(user);
        model.addAttribute("user", user);

        GameResult result = gameMapper.toEntity(game, user);

        gameResultRepository.save(result);

        model.addAttribute("percent", game.getPercent());
        model.addAttribute("answers", game.getAnswers());
        model.addAttribute("operation", OperationEnum.MULTIPLICATION.name());
        return "game/summary";
    }

    private GameConfig getActualConfig(){
        return gameConfigRepository.findByDescriptionIgnoreCase(GameConfigs.MULTIPLICATION.name())
//                gameConfigRepository.findById(1L)
                .orElse(GameConfig
                .builder()
                .timeLimitSeconds(5)
                .enableTimer(true)
                .rangeIni(2)
                .rangeFin(12)
                .build());
    }

    private List<Option> getModesFromConfig(GameConfig config){
        List<Option> listModes = new ArrayList<>(Arrays.stream(config.getRangeTables().split(","))
                .map(x-> Option
                    .builder()
                    .value(x)
                    .description("Tabla del "+x)
                    .build())
                .collect(Collectors.toList()));
        if(config.isEnableAllTables()){
            listModes.add(0, Option.builder().value("ALL").description("Todos ("+config.getRangeIni()+" - "+config.getRangeFin()+")").build());
        }
        return listModes;
    }

}