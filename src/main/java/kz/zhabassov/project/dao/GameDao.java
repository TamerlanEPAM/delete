package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Game;
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
import java.util.Date;
import java.util.List;

@Slf4j
public class GameDao implements InitializingBean {
    private static final String SQL_FIND_BY_DATE = "SELECT game_id, date, host_score, guest_score, host_team, guest_team FROM \"game\" WHERE date = :date;";
    private static final String SQL_FIND_BY_ID = "SELECT game_id,date,host_score,guest_score, host_team, guest_team FROM \"game\" WHERE game_id = :game_id;";
    private static final String SQL_DELETE_QUERY = "DELETE FROM \"game\" WHERE game_id = :game_id;";
    private static final String SQL_FIND_ALL = "SELECT game_id, host_team, host_score, guest_team, guest_score, date FROM \"game\" ORDER BY date DESC;";
    private static final String SQL_UPDATE_QUERY = "UPDATE \"game\" SET  host_score = :host_score, guest_score = :guest_score WHERE game_id = :game_id;";
    private static final String SQL_INSERT_QUERY = "INSERT INTO \"game\" (date, host_score, guest_score, host_team, guest_team)  VALUES (:date, :host_score, :guest_score, :host_team, :guest_team);";
    private static final String SQL_FIND_LAST_FIVE_QUERY = "SELECT game_id, host_team, host_score, guest_team, guest_score, date FROM \"game\" ORDER BY date DESC LIMIT 5;";
    private static final String SQL_FIND_FIRST_FIVE_WITH_MAX_SCORE = "SELECT team, SUM(game) as game, SUM(score) as score FROM (SELECT host_team as team, COUNT(game_id) as game, SUM(host_score) as score FROM \"game\" GROUP BY host_team UNION ALL SELECT guest_team as team , COUNT(game_id) as game, SUM(guest_score) as score FROM \"game\" GROUP BY guest_team ) t GROUP BY team ORDER BY score DESC LIMIT 5;";
    private static final String COLUMN_GAME_ID = "game_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HOST_SCORE = "host_score";
    private static final String COLUMN_GUEST_SCORE = "guest_score";
    private static final String COLUMN_HOST_TEAM = "host_team";
    private static final String COLUMN_GUEST_TEAM = "guest_team";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int insert(Game game) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_DATE, game.getDate())
                .addValue(COLUMN_HOST_SCORE, game.getHostScore())
                .addValue(COLUMN_GUEST_SCORE, game.getGuestScore())
                .addValue(COLUMN_HOST_TEAM, game.getHostTeam())
                .addValue(COLUMN_GUEST_TEAM, game.getGuestTeam());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        int id = keyHolder.getKeys().get(COLUMN_GAME_ID) != null ? (Integer) keyHolder.getKeys().get(COLUMN_GAME_ID) : 0;
        if (status != 0) {
            log.info("Game data inserted with ID, date and host and guest teams " + id + game.getDate() + "," + game.getHostTeam() + ":" + game.getGuestTeam());
        } else {
            log.warn("Game didn't insert with date and host and guest teams " + game.getDate() + "," + game.getHostTeam() + ":" + game.getGuestTeam());
        }
        return id;
    }

    public int update(Game game) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_HOST_SCORE, game.getGuestScore()).addValue(COLUMN_GUEST_SCORE, game.getGuestScore()).addValue(COLUMN_GAME_ID, game.getGameId());
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Game data updated for ID " + game.getGameId() + " now score (host:guest) is " + game.getHostScore() + ":" + game.getGuestScore());
        } else {
            log.warn("No Game found with ID " + game.getGameId());
        }
        return status;
    }

    public int delete(int game_id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_GAME_ID, game_id);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Game data deleted for ID " + game_id);
        } else {
            log.warn("No Game found with ID " + game_id);
        }
        return status;
    }

    public List<Game> findAll() {
        List<Game> games = namedParameterJdbcTemplate.query(SQL_FIND_ALL, new GameMapper());
        if (games != null) {
            log.info("Successfully find all games");
        } else {
            log.warn("Couldn't find any games");
        }
        return games;
    }

    public List<Game> findLastFive() {
        List<Game> games = namedParameterJdbcTemplate.query(SQL_FIND_LAST_FIVE_QUERY, new GameMapper());
        if (games != null) {
            log.info("Successfully found last five games");
        } else {
            log.warn("Couldn't find any games");
        }
        return games;
    }

    public List<Game> findFirstFiveWithMaxScore(){
        List<Game> games = namedParameterJdbcTemplate.query(SQL_FIND_FIRST_FIVE_WITH_MAX_SCORE, new GameMapper());
        if (games != null) {
            log.info("Successfully found first five with max score");
        } else {
            log.warn("Couldn't find any games");
        }
        return games;
    }

    public Game findById(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_GAME_ID, id);
        Game game = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, namedParameters, new GameMapper());
        if (game != null) {
            log.info("Successfully find game with ID " + id);
        } else {
            log.warn("Couldn't find game with ID " + id);
        }
        return game;
    }

    public Game findByDate(Date date) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_DATE, date);
        Game game = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_DATE, namedParameters, new GameMapper());
        if (game != null) {
            log.info("Successfully find game with date " + date);
        } else {
            log.warn("Couldn't find game with date " + date);
        }
        return game;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            log.error("Must set jdbcTemplate on GameDao");
            throw new BeanCreationException("Must set jdbcTemplate on GameDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("jdbcTemplate was initialized in GameDao");

    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        log.info("namedParameterJdbcTemplate was initialized in GameDao");
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet resultSet, int i) throws SQLException {
            Game game = new Game();
            game.setGameId(resultSet.getInt(COLUMN_GAME_ID));
            game.setDate(resultSet.getDate(COLUMN_DATE));
            game.setHostScore(resultSet.getInt(COLUMN_HOST_SCORE));
            game.setGuestScore(resultSet.getInt(COLUMN_GUEST_SCORE));
            game.setHostTeam(resultSet.getString(COLUMN_HOST_TEAM));
            game.setGuestTeam(resultSet.getString(COLUMN_GUEST_TEAM));
            return game;
        }
    }
}
