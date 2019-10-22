package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Game;
import kz.zhabassov.project.entity.Penalty;
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
public class PenaltyDao implements InitializingBean {
    private static final String SQL_FIND_BY_PENALTY = "SELECT penalty.penalty, description FROM \"penalty\" WHERE penalty.penalty = :penalty";
    private static final String SQL_DELETE_QUERY = "DELETE FROM \"penalty\" WHERE penalty = :penalty";
    private static final String SQL_FIND_ALL = "SELECT penalty.penalty, description FROM \"penalty\"";
    private static final String SQL_UPDATE_QUERY = "UPDATE \"penalty\" SET description = :description WHERE penalty = :penalty";
    private static final String SQL_INSERT_QUERY = "INSERT INTO \"penalty\" (penalty, description)  VALUES (:penalty, :description)";
    private static final String COLUMN_PENALTY = "penalty";
    private static final String COLUMN_DESCRIPTION = "description";


    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int insert(Penalty penalty) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(COLUMN_PENALTY, penalty.getPenalty())
                .addValue(COLUMN_DESCRIPTION, penalty.getDescription());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        if (status != 0) {
            log.info("Penalty data inserted with name " + penalty.getPenalty());
        } else {
            log.warn("Penalty didn't insert with name " + penalty.getPenalty());
        }
        return status;
    }

    public int update(Penalty penalty) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_DESCRIPTION, penalty.getDescription()).addValue(COLUMN_PENALTY, penalty.getPenalty());
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Penalty data updated for name " + penalty.getPenalty() + " now description is " + penalty.getDescription());
        } else {
            log.warn("No Penalty found with name " + penalty.getPenalty());
        }
        return status;
    }

    public int delete(String penalty) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PENALTY, penalty);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, namedParameters);
        if (status != 0) {
            log.info("Penalty data deleted for name " + penalty);
        } else {
            log.warn("No Penalty found with name " + penalty);
        }
        return status;

    }

    public List<Penalty> findAll() {
        List<Penalty> penalties = namedParameterJdbcTemplate.query(SQL_FIND_ALL, new PenaltyMapper());
        if (penalties != null) {
            log.info("Successfully find all penalties");
        } else {
            log.warn("Couldn't find any penalties");
        }
        return penalties;
    }


    public Penalty findByPenalty(Penalty penaltyName) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(COLUMN_PENALTY, penaltyName);
        Penalty penalty = namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_PENALTY, namedParameters, new PenaltyMapper());
        if (penalty != null) {
            log.info("Successfully find penalty with penalty name " + penaltyName);
        } else {
            log.warn("Couldn't find penalty with penalty name " + penaltyName);
        }
        return penalty;
    }

    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            log.error("Must set jdbcTemplate on PenaltyDao");
            throw new BeanCreationException("Must set jdbcTemplate on PenaltyDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("jdbcTemplate was initialized in PenaltyDao");
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        log.info("namedParameterJdbcTemplate was initialized in PenaltyDao");
    }



    private static final class PenaltyMapper implements RowMapper<Penalty> {

        @Override
        public Penalty mapRow(ResultSet resultSet, int i) throws SQLException {
            Penalty penalty = new Penalty();
            penalty.setPenalty(resultSet.getString(COLUMN_PENALTY));
            penalty.setDescription(resultSet.getString(COLUMN_DESCRIPTION));
            return penalty;
        }
    }
}
