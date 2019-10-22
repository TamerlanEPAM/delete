package kz.zhabassov.project.dao;

import kz.zhabassov.project.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;


public class PlayerDaoTest {
    private PlayerDao playerDao;
    private Player truePlayer;
    private Player newPlayer;
    private EmbeddedDatabase db;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:h2/V0_1__create_tables.sql")
                .build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);



        playerDao = new PlayerDao();
        playerDao.setNamedParameterJdbcTemplate(template);
        assertNotNull(playerDao);

        truePlayer = new Player();
        truePlayer.setPlayerId(1);
        truePlayer.setName("Nikita Kucherov");
//        truePlayer.setTeam("Tampa Bay Lightning");
        truePlayer.setSkillLevel("master");
        truePlayer.setPosition("forward");

        newPlayer = new Player();
        newPlayer.setName("Yegor Korshkow");
//        newPlayer.setTeam("Tampa Bay Lightning");
        newPlayer.setSkillLevel("junior");
        newPlayer.setPosition("forward");
    }


    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    @Order(1)
    public void insert() {
        Long isInserted = playerDao.insert(newPlayer);
        assertTrue(isInserted != 0);
        System.out.println(isInserted);
    }

    @Test
    @Order(2)
    public void delete() {
        Long isInserted = playerDao.insert(newPlayer);
        assertTrue(isInserted != 0);
//        int isDeleted = playerDao.delete((int)isInserted);
//        assertTrue(isDeleted != 0);

    }

    @Test
    @Order(3)
    public void update() {
        int isUpdatedFirst = playerDao.update(truePlayer);
//        assertTrue(isUpdatedFirst != 0);
        truePlayer.setSkillLevel("master");
        int isUpdatedSecond = playerDao.update(truePlayer);
//        assertTrue(isUpdatedSecond != 0);
    }

    @Test
    @Order(4)
    public void findById() {
        Player player = playerDao.findById(1);
        assertTrue(truePlayer.getName().equals(player.getName()));
    }


    @Test
    @Order(5)
    public void findByName() {
        Player player = playerDao.findByName("Nikita Kucherov");
        assertTrue(truePlayer.getName().equals(player.getName()));
    }

    @Test
    @Order(6)
    public void findAll() {
        List<Player> players = playerDao.findAll();
        assertTrue(players.size() > 0);
    }


}