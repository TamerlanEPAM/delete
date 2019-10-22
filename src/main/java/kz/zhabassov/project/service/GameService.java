package kz.zhabassov.project.service;

import kz.zhabassov.project.dao.GameDao;
import kz.zhabassov.project.entity.Game;
import kz.zhabassov.project.entity.Team;

import java.util.List;

public class GameService {

    private GameDao gameDao;
    //get first 5 with host team and score, guest team and score
    public List<Game>  showLastFiveGames(){
        return gameDao.findLastFive();
    }

    //get all
    public List<Game> showAllGames(){
        return gameDao.findAll();
    }

    /**
     * fghfgh
     * @param game
     * @return
     */
    public int updateScore(Game game){
        return gameDao.update(game);
    }

    //add game
    public int addGame(Game game){
        return  gameDao.insert(game);
    }

    public List<Game> showFirstFive(){
        return gameDao.findFirstFiveWithMaxScore();
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
