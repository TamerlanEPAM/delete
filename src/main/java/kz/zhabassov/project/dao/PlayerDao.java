package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Player;
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
public class PlayerDao implements InitializingBean {
    private static final String SQL_INSERT_QUERY = "INSERT INTO player (name, position, skill_level, team)  VALUES (:name, :position, :skill_level, :team)";
    private static final String SQL_UPDATE_QUERY = "UPDATE player SET skill_level = :skill_level WHERE player_id = :player_id";
    private static final String SQL_DELETE_QUERY = "DELETE FROM player WHERE player_id = :player_id";
    private static final String SQL_FIND_ALL = "SELECT player_id, name, position, skill_level, team FROM player";
    private static final String SQL_FIND_BY_ID = "SELECT player_id, name, position, skill_level, team FROM player WHERE player_id = :player_id";
    private static final String SQL_FIND_BY_NAME = "SELECT player_id, name, position, skill_level, team FROM player WHERE name = :name";
    private static final String SQL_FIND_FIRST_FIVE_PLAYER_WITH_MAX_PENALTIES = "SELECT player.player_id, player.name, COUNT(player_penalty_game.penalty) as penalty_amount FROM player RIGHT JOIN player_penalty_game ON player.player_id =player_penalty_game.player_id GROUP BY player.player_id ORDER BY penalty_amount DESC LIMIT 5;";
    private static final String COLUMN_PLAYER_ID = "player_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_SKILL_LEVEL = "skill_level";
    private static final String COLUMN_TEAM = "team";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Long insert(Player player) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_NAME, player.getName())
                .addValue(COLUMN_POSITION, player.getPosition())
                .addValue(COLUMN_SKILL_LEVEL, player.getSkillLevel())
                .addValue(COLUMN_TEAM, player.getTeam());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        Long id = keyHolder.getKeys().get(COLUMN_PLAYER_ID) != null ? (Long) keyHolder.getKeys().get(COLUMN_PLAYER_ID) : null;
        if (status != 0) {
            log.info("Player data inserted with ID " + id);
        } else {
            log.warn("Player didn't insert with name " + player.getName());
        }
        return id;
    }

    public int update(Player player) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_SKILL_LEVEL, player.getSkillLevel()).addValue(COLUMN_PLAYER_ID, player.getPlayerId());
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Player data updated for ID " + player.getPlayerId());
        } else {
            log.warn("No Player found with ID " + player.getPlayerId());
        }
        return status;
    }

    public int delete(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PLAYER_ID, id);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Player data deleted for ID " + id);
        } else {
            log.warn("No Player found with ID " + id);
        }
        return status;
    }

    public List<Player> findAll() {
        List<Player> players = namedParameterJdbcTemplate.query(SQL_FIND_ALL, new PlayerMapper());
        if (players != null) {
            log.info("Successfully find all players");
        } else {
            log.warn("Couldn't find any players");
        }
        return players;
    }

    public List<Player> findFiveWithMaxPenalties() {
        List<Player> players = namedParameterJdbcTemplate.query(SQL_FIND_FIRST_FIVE_PLAYER_WITH_MAX_PENALTIES, new PlayerMapper());
        if (players != null) {
            log.info("Successfully found five players with max penalties");
        } else {
            log.warn("Couldn't find any players");
        }
        return players;
    }

    public Player findById(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PLAYER_ID, id);
        Player player = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, namedParameters, new PlayerMapper());
        if (player != null) {
            log.info("Successfully find player with ID " + id);
        } else {
            log.warn("Couldn't find player with ID " + id);
        }
        return player;
    }

    public Player findByName(String name) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_NAME, name);
        Player player = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_NAME, namedParameters, new PlayerMapper());
        if (player != null) {
            log.info("Successfully find player with name" + name);
        } else {
            log.warn("Couldn't find player with name" + name);
        }
        return player;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            log.error("Must set jdbcTemplate on PlayerDao");
            throw new BeanCreationException("Must set jdbcTemplate on PlayerDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("jdbcTemplate was initialized in PlayerDao");
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        log.info("namedParameterJdbcTemplate was initialized in PlayerDao");

    }

    private static final class PlayerMapper implements RowMapper<Player> {

        @Override
        public Player mapRow(ResultSet resultSet, int i) throws SQLException {
            Player player = new Player();
            player.setPlayerId(resultSet.getInt(COLUMN_PLAYER_ID));
            player.setName(resultSet.getString(COLUMN_NAME));
            player.setPosition(resultSet.getString(COLUMN_POSITION));
            player.setSkillLevel(resultSet.getString(COLUMN_SKILL_LEVEL));
            player.setTeam(resultSet.getString(COLUMN_TEAM));
            return player;
        }
    }

}
