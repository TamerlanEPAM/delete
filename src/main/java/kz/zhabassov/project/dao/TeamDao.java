package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class TeamDao implements InitializingBean {
    private static final String SQL_DELETE_QUERY = "DELETE FROM team WHERE team.team = :team";
    private static final String SQL_FIND_ALL = "SELECT team, city, coach, captain FROM \"team\"";
    private static final String SQL_UPDATE_QUERY = "UPDATE team SET captain = :captain WHERE team.team = :team";
    private static final String SQL_INSERT_QUERY = "INSERT INTO team (team, city, coach, captain)  VALUES (:team, :city, :coach, :captain)";
    private static final String SQL_FIND_BY_TEAM = "SELECT team.team, city, coach, captain FROM team WHERE team.team = :team";
    private static final String SQL_FIND_BY_CITY = "SELECT team.team, city, coach, captain FROM team WHERE team.city = :city";
    private static final String COLUMN_TEAM = "team";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_COACH = "coach";
    private static final String COLUMN_CAPTAIN = "captain";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int insert(Team team) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_TEAM, team.getTeam())
                .addValue(COLUMN_CITY, team.getCity())
                .addValue(COLUMN_COACH, team.getCoach())
                .addValue(COLUMN_CAPTAIN, team.getCaptain());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        if (status != 0) {
//            log.info("Team data inserted with name " + team.getTeam());
        } else {
//            log.warn("Team didn't insert with name " + team.getTeam());
        }
        return status;
    }

    public int update(Team team) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_CAPTAIN, team.getCaptain()).addValue(COLUMN_TEAM, team.getTeam());
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, namedParameters);
        if (status != 0) {
//            log.info("Team data updated for name " + team.getTeam() + " new captain is " + team.getCaptain());
        } else {
//            log.warn("No Team found with name " + team.getTeam());
        }
        return status;

    }

    public int delete(Team team) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_TEAM, team);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, namedParameters);
        if (status != 0) {
//            log.info("Team data deleted for ID " + team);
        } else {
//            log.warn("No Team found with ID " + team);
        }
        return status;
    }

    public List<Team> findAll() {
        List<Team> teams = namedParameterJdbcTemplate.query(SQL_FIND_ALL, new TeamMapper());
        if (teams != null) {
//            log.info("Successfully find all teams");
        } else {
//            log.warn("Couldn't find any teams");
        }
        return teams;

    }

    public Team findByCity(String city) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_CITY, city);
        Team team = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_CITY, namedParameters, new TeamMapper());
        if (team != null) {
//            log.info("Successfully find team with city " + city);
        } else {
//            log.warn("Couldn't find team with city " + city);
        }
        return team;
    }

    public Team findByTeam(String teamName) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_TEAM, teamName);
        Team team = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_TEAM, namedParameters, new TeamMapper());
        if (team != null) {
//            log.info("Successfully find team with name " + teamName);
        } else {
//            log.warn("Couldn't find team with name " + teamName);
        }
        return team;
    }

    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
//            log.error("Must set jdbcTemplate on TeamDao");
            throw new BeanCreationException("Must set jdbcTemplate on TeamDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
//        log.info("jdbcTemplate was initialized in TeamDao");
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
//        log.info("namedParameterJdbcTemplate was initialized in TeamDao");
    }

    private static final class TeamMapper implements RowMapper<Team> {

        @Override
        public Team mapRow(ResultSet resultSet, int i) throws SQLException {
            Team team = new Team();
            team.setTeam(resultSet.getString(COLUMN_TEAM));
            team.setCity(resultSet.getString(COLUMN_CITY));
            team.setCoach(resultSet.getString(COLUMN_COACH));
            team.setCaptain(resultSet.getInt(COLUMN_CAPTAIN));
            return team;
        }
    }
}
