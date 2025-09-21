package com.woxloi.mcstrike.game;

import org.bukkit.Bukkit;
import com.woxloi.mcstrike.mcstrike;

public class GameManager {

    private final TeamManager teamManager = new TeamManager();
    private int roundNumber = 0;
    private boolean roundActive = false;
    private RoundTimer roundTimer;

    private final int ROUND_DURATION = 120; // ラウンド時間：120秒

    public void startNextRound() {
        roundNumber++;
        roundActive = true;

        swapTeams();

        mcstrike.BroadPrefixMessage(null, "ラウンド " + roundNumber + " 開始！");

        // ボスバータイマー開始
        roundTimer = new RoundTimer(mcstrike.getPlugin(mcstrike.class),
                "ラウンド " + roundNumber, ROUND_DURATION);

        // 全プレイヤーに追加
        Bukkit.getOnlinePlayers().forEach(roundTimer::addPlayer);
    }

    public void endRound(String winner) {
        roundActive = false;
        mcstrike.BroadPrefixMessage(null, "ラウンド終了！勝利チーム: " + winner);

        if (roundTimer != null) {
            roundTimer.cancel();
        }
    }

    private void swapTeams() {
        for (var p : teamManager.getAttackers()) teamManager.addPlayerToDefenders(p);
        for (var p : teamManager.getDefenders()) teamManager.addPlayerToAttackers(p);
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public boolean isRoundActive() {
        return roundActive;
    }
}
