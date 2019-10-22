package kz.zhabassov.project.service;

import kz.zhabassov.project.dao.TeamDao;
import kz.zhabassov.project.entity.Team;

import java.util.List;

public class TeamService {
    private TeamDao teamDao;
    //get all, name, games, goals, coach, captain
    public List<Team> showAll(){
        return teamDao.findAll();
    }

    //get first five, name, games, goals

    //update name, coach, captain
    public int update(Team team){
        return teamDao.update(team);
    }
    //insert name, games, goals, coach, captain
    public int insert(Team team){
        return teamDao.insert(team);
    }
}
