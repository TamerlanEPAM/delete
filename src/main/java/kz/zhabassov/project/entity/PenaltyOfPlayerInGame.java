package kz.zhabassov.project.entity;

import lombok.Data;

@Data
public class PenaltyOfPlayerInGame {
    private Long penaltyOfPlayerInGameId;
    private Long playerId;
    private String playerName;
    private String penalty;
    private Long game_id;

}
