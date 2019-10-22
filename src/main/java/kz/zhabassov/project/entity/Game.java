package kz.zhabassov.project.entity;

import lombok.Data;

import java.util.Date;

public class Game extends Entity{
    private Long gameId;
    private Date date;
    private Long hostScore;
    private Long guestScore;
    private String hostTeam;
    private String guestTeam;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        properties.put(PropertyGame.GAME_ID,gameId);
        this.gameId = gameId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        properties.put(PropertyGame.DATE, date);
        this.date = date;
    }

    public Long getHostScore() {
        return hostScore;
    }

    public void setHostScore(Long hostScore) {
        properties.put(PropertyGame.HOST_SCORE, hostScore);
        this.hostScore = hostScore;
    }

    public Long getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(Long guestScore) {
        properties.put(PropertyGame.GUEST_SCORE, guestScore);
        this.guestScore = guestScore;
    }

    public String getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(String hostTeam) {
        properties.put(PropertyGame.HOST_TEAM, hostTeam);
        this.hostTeam = hostTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        properties.put(PropertyGame.GUEST_TEAM, guestTeam);
        this.guestTeam = guestTeam;
    }

    public enum PropertyGame implements Property {
        GAME_ID(Long.class,"entity_id"),
        DATE(Date.class, "date"),
        HOST_SCORE(Long.class, "host_score"),
        GUEST_SCORE(Long.class, "guest_score"),
        HOST_TEAM(String.class, "host_team"),
        GUEST_TEAM(String.class, "guest_team")
        ;
        private Class propertyType;
        private String column;
        PropertyGame(Class propertyType, String column) {
            this.propertyType = propertyType;
            this.column = column;
        }

        public Class getPropertyType() {
            return propertyType;
        }

        public String getColumn() {
            return column;
        }

    }
}
