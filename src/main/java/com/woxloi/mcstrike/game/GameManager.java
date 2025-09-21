package com.woxloi.mcstrike.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.woxloi.mcstrike.mcstrike;

public class GameManager {

    private final TeamManager teamManager = new TeamManager();
    private int roundNumber = 0;
    private boolean roundActive = false;
    private RoundTimer roundTimer;
    private BombManager bombManager; // 新規追加

    private final int ROUND_DURATION = 120; // ラウンド時間：120秒

    public GameManager() {
        bombManager = new BombManager(this);
    }

    public void startNextRound() {
        roundNumber++;
        roundActive = true;

        swapTeams();

        mcstrike.BroadPrefixMessage(null, "ラウンド " + roundNumber + " 開始！");

        roundTimer = new RoundTimer(mcstrike.getPlugin(mcstrike.class),
                "ラウンド " + roundNumber, ROUND_DURATION);

        Bukkit.getOnlinePlayers().forEach(roundTimer::addPlayer);
    }

    public void endRound(String winner) {
        roundActive = false;
        mcstrike.BroadPrefixMessage(null, "ラウンド終了！勝利チーム: " + winner);

        if (roundTimer != null) {
            roundTimer.cancel();
        }

        // 爆弾タイマーも停止
        bombManager.cancelBomb();
    }

    private void swapTeams() {
        for (Player p : teamManager.getAttackers()) teamManager.addPlayerToDefenders(p);
        for (Player p : teamManager.getDefenders()) teamManager.addPlayerToAttackers(p);
    }

    // 勝利条件チェック（全滅 or 爆弾）
    public void checkRoundEnd() {
        if (!roundActive) return;

        if (teamManager.getAttackers().isEmpty()) {
            endRound("防御側（攻撃側全滅）");
        } else if (teamManager.getDefenders().isEmpty()) {
            endRound("攻撃側（防御側全滅）");
        } else if (bombManager.isBombExploded()) {
            endRound("攻撃側（爆弾爆発）");
        } else if (bombManager.isBombDefused()) {
            endRound("防御側（爆弾解除）");
        }
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public BombManager getBombManager() {
        return bombManager;
    }
}
