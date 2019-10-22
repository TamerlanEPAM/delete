package kz.zhabassov.project.entity;

import lombok.Data;

public class Player extends Entity{
    private Long playerId;
    private String name;
    private String position;
    private String skillLevel;
    private String team;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        properties.put(PropertyPlayer.ID, playerId);
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        properties.put(PropertyPlayer.NAME, name);
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        properties.put(PropertyPlayer.POSITION, position);
        this.position = position;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        properties.put(PropertyPlayer.SKILL_LEVEL, skillLevel);
        this.skillLevel = skillLevel;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        properties.put(PropertyPlayer.TEAM, team);
        this.team = team;
    }

    public enum PropertyPlayer implements Property {
        ID("player_id", Long.class),
        NAME("name", String.class),
        POSITION("position", String.class),
        SKILL_LEVEL("skill_level", String.class),
        TEAM("team", String.class)
        ;

        private String column;
        private Class propertyType;

        PropertyPlayer(String column, Class propertyType) {
            this.column = column;
            this.propertyType = propertyType;
        }

        public String getColumn() {
            return column;
        }

        public Class getPropertyType() {
            return propertyType;
        }
    }
}


