package kz.zhabassov.project.service;

import kz.zhabassov.project.dao.PlayerDao;
import kz.zhabassov.project.entity.Player;

import java.util.List;

public class PlayerService {
    private PlayerDao playerDao;
    //get all
    public List<Player> showAllPlayers(){
        return playerDao.findAll();
    }

    //get five first with max penalties
    public List<Player> showFiveWithMaxPenalties(){
        return playerDao.findFiveWithMaxPenalties();
    }

    //add name team, position, skill level
    public Long addPlayer(Player player){
        return playerDao.insert(player);
    }

    //update name, team, position, skill level
    public int updatePlayer(Player player){
        return playerDao.update(player);
    }

    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
}
