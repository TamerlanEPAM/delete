package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Game;
import kz.zhabassov.project.entity.Penalty;
import kz.zhabassov.project.entity.PenaltyOfPlayerInGame;
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
public class PenaltyOfPlayerInGameDao implements InitializingBean {
    private static final String SQL_FIND_BY_PLAYER_ID = "SELECT player_penalty_game_id, player_id, player_name, penalty, game_id FROM \"player_penalty_game\" WHERE player_id = :player_id";
    private static final String SQL_FIND_BY_ID = "SELECT player_penalty_game_id, player_id, player_name, penalty, game_id FROM \"player_penalty_game\" WHERE player_penalty_game_id = :player_penalty_game_id";
    private static final String SQL_DELETE_QUERY = "DELETE FROM \"player_penalty_game\" WHERE player_penalty_game_id = :player_penalty_game_id";
    private static final String SQL_FIND_ALL = "SELECT player_penalty_game_id, player_id, player_name, penalty, game_id FROM \"player_penalty_game\"";
    private static final String SQL_UPDATE_QUERY = "UPDATE \"player_penalty_game\" SET penalty = :penalty WHERE player_penalty_game_id = :player_penalty_game_id";
    private static final String SQL_INSERT_QUERY = "INSERT INTO \"player_penalty_game\" (player_id, player_name, penalty, game_id)  VALUES (:player_id, :player_name, :penalty, :game_id)";
    private static final String SQL_INSERT_PENALTY_PLAYER_GAME_QUERY = "";
    private static final String COLUMN_PENALTY_PLAYER_GAME_ID = "penalty_player_game_id";
    private static final String COLUMN_PLAYER_ID = "player_id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_PENALTY = "penalty";
    private static final String COLUMN_GAME_ID = "game_id";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int insert(PenaltyOfPlayerInGame penaltyOfPlayerInGame) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_PLAYER_ID, penaltyOfPlayerInGame.getPlayerId())
                .addValue(COLUMN_PLAYER_NAME, penaltyOfPlayerInGame.getPlayerName())
                .addValue(COLUMN_PENALTY, penaltyOfPlayerInGame.getPenalty())
                .addValue(COLUMN_GAME_ID, penaltyOfPlayerInGame.getGame_id());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        int id = keyHolder.getKeys().get(COLUMN_PENALTY_PLAYER_GAME_ID) != null ? (Integer) keyHolder.getKeys().get(COLUMN_PENALTY_PLAYER_GAME_ID) : 0;
        if (status != 0) {
            log.info("PenaltyOfPlayerInGame data inserted with ID " + id);
        } else {
            log.warn("PenaltyOfPlayerInGame didn't insert of player, penalty and game ID " + penaltyOfPlayerInGame.getPlayerName() + ", " + penaltyOfPlayerInGame.getPenalty() + ", " + penaltyOfPlayerInGame.getGame_id());
        }
        return id;
    }

    public int update(PenaltyOfPlayerInGame penaltyOfPlayerInGame) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_PENALTY, penaltyOfPlayerInGame.getPenalty()).addValue(COLUMN_PENALTY_PLAYER_GAME_ID, penaltyOfPlayerInGame.getGame_id());
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, namedParameters);
        if (status != 0) {
            log.info("PenaltyOfPlayerInGame data updated for name " + penaltyOfPlayerInGame.getPenaltyOfPlayerInGameId() + " now penalty is " + penaltyOfPlayerInGame.getPenalty());
        } else {
            log.warn("No PenaltyOfPlayerInGame found with name " + penaltyOfPlayerInGame.getPenalty());
        }
        return status;
    }

    public int delete(PenaltyOfPlayerInGame penaltyOfPlayerInGameId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PENALTY_PLAYER_GAME_ID, penaltyOfPlayerInGameId);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, namedParameters);
        if (status != 0) {
            log.info("PenaltyOfPlayerInGame data deleted for ID " + penaltyOfPlayerInGameId);
        } else {
            log.warn("No PenaltyOfPlayerInGame found with ID " + penaltyOfPlayerInGameId);
        }
        return status;

    }

    public List<PenaltyOfPlayerInGame> findAll() {
        List<PenaltyOfPlayerInGame> penaltyOfPlayerInGames = namedParameterJdbcTemplate.query(SQL_FIND_ALL, new PenaltyOfPlayerInGameMapper());
        if (penaltyOfPlayerInGames != null) {
            log.info("Successfully find all penaltyOfPlayerInGames");
        } else {
            log.warn("Couldn't find any penaltyOfPlayerInGames");
        }
        return penaltyOfPlayerInGames;
    }

    public PenaltyOfPlayerInGame findById(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PENALTY_PLAYER_GAME_ID, id);
        return namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, namedParameters, new PenaltyOfPlayerInGameMapper());

    }

    public PenaltyOfPlayerInGame findByPlayerId(int playerId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PLAYER_ID, playerId);
        PenaltyOfPlayerInGame penaltyOfPlayerInGame = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_PLAYER_ID, namedParameters, new PenaltyOfPlayerInGameMapper());
        if (penaltyOfPlayerInGame != null) {
            log.info("Successfully find penaltyOfPlayerInGame with player ID " + playerId);
        } else {
            log.warn("Couldn't find penaltyOfPlayerInGame with player ID " + playerId);
        }
        return penaltyOfPlayerInGame;
    }

    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            log.error("Must set jdbcTemplate on PenaltyOfPlayerINGameDao");
            throw new BeanCreationException("Must set jdbcTemplate on PenaltyOfPlayerINGameDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("jdbcTemplate was initialized in PenaltyOfPlayerINGameDao");

    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        log.info("namedParameterJdbcTemplate was initialized in PenaltyOfPlayerINGameDao");
    }

    public int insert(Penalty penalty, Player player, Game game) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_PENALTY, penalty.getPenalty())
                .addValue(COLUMN_GAME_ID, game.getGameId())
                .addValue(COLUMN_PLAYER_NAME, player.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_PENALTY_PLAYER_GAME_QUERY, param, keyHolder);
        if (status != 0) {
            log.info("Penalty of player in game inserted " + penalty.getPenalty()+" "+player.getName()+" "+game.getGameId());
        } else {
            log.warn("Penalty of player in game didn't insert " +  penalty.getPenalty()+" "+player.getName()+" "+game.getGameId());
        }
        return status;

    }

    private static final class PenaltyOfPlayerInGameMapper implements RowMapper<PenaltyOfPlayerInGame> {

        @Override
        public PenaltyOfPlayerInGame mapRow(ResultSet resultSet, int i) throws SQLException {
            PenaltyOfPlayerInGame penaltyOfPlayerInGame = new PenaltyOfPlayerInGame();
            penaltyOfPlayerInGame.setPenaltyOfPlayerInGameId(resultSet.getInt(COLUMN_PENALTY_PLAYER_GAME_ID));
            penaltyOfPlayerInGame.setPlayerId(resultSet.getInt(COLUMN_PLAYER_ID));
            penaltyOfPlayerInGame.setPlayerName(resultSet.getString(COLUMN_PLAYER_NAME));
            penaltyOfPlayerInGame.setPenalty(resultSet.getString(COLUMN_PENALTY));
            penaltyOfPlayerInGame.setGame_id(resultSet.getInt(COLUMN_GAME_ID));
            return penaltyOfPlayerInGame;
        }
    }
}
