package kz.zhabassov.project.daoA;

import kz.zhabassov.project.entity.Entity;
import kz.zhabassov.project.entity.Property;
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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Dao implements InitializingBean {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int insert(Entity entity, String SQL_INSERT_QUERY, String COLUMN_ID) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
            map.put(pair.getKey().getColumn(), pair.getValue());
        }
        SqlParameterSource param = new MapSqlParameterSource().addValues(map);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param, keyHolder);
        int id = keyHolder.getKeys().get(COLUMN_ID) != null ? (Integer) keyHolder.getKeys().get(COLUMN_ID) : 0;
        if (status != 0) {
            log.info("Entity data inserted"+entity);
        } else {
            log.warn("Entity didn't insert"+entity);
        }
        return id;
    }

    public int insert(Entity entity, String SQL_INSERT_QUERY) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
            map.put(pair.getKey().getColumn(), pair.getValue());
        }
        SqlParameterSource param = new MapSqlParameterSource().addValues(map);
        int status = namedParameterJdbcTemplate.update(SQL_INSERT_QUERY, param);
        if (status != 0) {
            log.info("Entity data inserted"+entity);
        } else {
            log.warn("Entity didn't insert"+entity);
        }
        return status;
    }

    public int update(Entity entity, String SQL_UPDATE_QUERY) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
            map.put(pair.getKey().getColumn(), pair.getValue());
        }
        SqlParameterSource param = new MapSqlParameterSource().addValues(map);
        int status = namedParameterJdbcTemplate.update(SQL_UPDATE_QUERY, param);
        if (status != 0) {
            log.info("Entity data updated"+entity);
        } else {
            log.warn("No Entity founded"+entity);
        }
        return status;
    }

    public int delete(Entity entity, String SQL_DELETE_QUERY) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
            map.put(pair.getKey().getColumn(), pair.getValue());
        }
        SqlParameterSource param = new MapSqlParameterSource().addValues(map);
        int status = namedParameterJdbcTemplate.update(SQL_DELETE_QUERY, param);
        if (status != 0) {
            log.info("Entity data deleted"+entity);
        } else {
            log.warn("No Entity found"+entity);
        }
        return status;
    }

    public List<Entity> findAll(String SQL_FIND_ALL_QUERY) {
        List<Entity> entities = namedParameterJdbcTemplate.query(SQL_FIND_ALL_QUERY, new EntityMapper());
        if (entities != null) {
            log.info("Successfully find all entities"+entities.size()+"items");
        } else {
            log.warn("Couldn't find any entities");
        }
        return entities;

    }

    public Entity findByProperty(Entity entity, String SQL_FIND_QUERY) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
            map.put(pair.getKey().getColumn(), pair.getValue());
        }
        SqlParameterSource param = new MapSqlParameterSource().addValues(map);
        Entity foundEntity = namedParameterJdbcTemplate.queryForObject(SQL_FIND_QUERY, param, new EntityMapper());
        if (foundEntity != null) {
            log.info("Successfully find entity " + entity);
        } else {
            log.warn("Couldn't find entity " + entity);
        }
        return foundEntity;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            log.error("Must set jdbcTemplate on EntityDao");
            throw new BeanCreationException("Must set jdbcTemplate on EntityDao");
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("jdbcTemplate was initialized in EntityDao");

    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        log.info("namedParameterJdbcTemplate was initialized in EntityDao");
    }

    private static final class EntityMapper implements RowMapper<Entity> {

        @Override
        public Entity mapRow(ResultSet resultSet, int i) throws SQLException {
            Entity entity = new Entity();

            for (Map.Entry<Property, Object> pair : entity.getProperties().entrySet()) {
                if (pair.getKey().getPropertyType() instanceof Integer) {
                    pair.setValue(resultSet.getInt(pair.getKey().getColumn()));
                } else if (pair.getKey().getPropertyType() instanceof Date) {
                    pair.setValue(resultSet.getDate(pair.getKey().getColumn()));
                } else if (pair.getKey().getPropertyType() instanceof String) {
                    pair.setValue(resultSet.getString(pair.getKey().getColumn()));
                } else {

                    try {
                        throw new IOException("Type doesn't exist");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            return entity;
        }
    }
}
