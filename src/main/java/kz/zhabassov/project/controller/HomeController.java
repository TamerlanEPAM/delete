package kz.zhabassov.project.controller;

import kz.zhabassov.project.service.GameService;
import kz.zhabassov.project.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeController {
    private GameService gameService;
    private PlayerService playerService;

    @RequestMapping({"/","/home"})
    public String showHomePage(Map<String, Object> model){
        model.put("games", gameService.showFirstFive());
        model.put("players", playerService.showFiveWithMaxPenalties());
        return "home";
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
