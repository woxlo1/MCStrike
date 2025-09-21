package com.woxloi.mcstrike.game;

import org.bukkit.entity.Player;
import com.woxloi.mcstrike.mcstrike;

public class GameManager {

    private final TeamManager teamManager = new TeamManager();
    private int roundNumber = 0;
    private boolean roundActive = false;

    public void startNextRound() {
        roundNumber++;
        roundActive = true;

        // 攻守交代
        swapTeams();

        // ラウンド開始メッセージ
        mcstrike.BroadPrefixMessage(null, "ラウンド " + roundNumber + " 開始！");
    }

    public void endRound(String winner) {
        roundActive = false;
        mcstrike.BroadPrefixMessage(null, "ラウンド終了！勝利チーム: " + winner);
    }

    private void swapTeams() {
        // シンプルな攻守交代
        for (Player p : teamManager.getAttackers()) {
            teamManager.addPlayerToDefenders(p);
        }
        for (Player p : teamManager.getDefenders()) {
            teamManager.addPlayerToAttackers(p);
        }
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public boolean isRoundActive() {
        return roundActive;
    }
}
