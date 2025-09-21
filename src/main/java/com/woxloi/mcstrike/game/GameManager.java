package com.woxloi.mcstrike.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.woxloi.mcstrike.mcstrike;

public class GameManager {

    private final TeamManager teamManager = new TeamManager();
    private int roundNumber = 0;
    private boolean roundActive = false;
    private RoundTimer roundTimer;
    private BombManager bombManager;

    private final int ROUND_DURATION = 120; // ラウンド時間：120秒

    // 🔹 ラウンド勝利スコア
    private int attackScore = 0;
    private int defenseScore = 0;
    private final int MAX_ROUNDS = 5; // 先に5ラウンド勝利でゲーム終了

    public GameManager() {
        bombManager = new BombManager(this);
    }

    public void startNextRound() {
        roundNumber++;
        roundActive = true;

        // 攻守交代
        teamManager.swapTeams();

        // スポーン位置にテレポート
        teamManager.teleportTeams();

        mcstrike.BroadPrefixMessage(null, "ラウンド " + roundNumber + " 開始！");

        // ボスバータイマー開始
        roundTimer = new RoundTimer(mcstrike.getPlugin(mcstrike.class),
                "ラウンド " + roundNumber + " 攻撃 " + attackScore + " - 防御 " + defenseScore, ROUND_DURATION);

        Bukkit.getOnlinePlayers().forEach(roundTimer::addPlayer);
        roundTimer.start();

    }

    public void endRound(String winner) {
        roundActive = false;

        // 🔹 勝利チームにスコア加算
        if (winner.contains("攻撃側")) attackScore++;
        else if (winner.contains("防御側")) defenseScore++;

        mcstrike.BroadPrefixMessage(null, "ラウンド終了！勝利チーム: " + winner);
        mcstrike.BroadPrefixMessage(null, "スコア: 攻撃側 " + attackScore + " - 防御側 " + defenseScore);

        if (roundTimer != null) {
            roundTimer.cancel();
        }

        bombManager.cancelBomb();

        // 🔹 最大ラウンド勝利チェック
        if (attackScore >= MAX_ROUNDS) {
            mcstrike.BroadPrefixMessage(null, "ゲーム終了！攻撃側の勝利！");
            resetScores();
        } else if (defenseScore >= MAX_ROUNDS) {
            mcstrike.BroadPrefixMessage(null, "ゲーム終了！防御側の勝利！");
            resetScores();
        }
    }

    private void swapTeams() {
        // GameManager側のswapTeamsは削除してTeamManager側のswapTeamsを呼ぶ
        teamManager.swapTeams();
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

    private void resetScores() {
        attackScore = 0;
        defenseScore = 0;
        roundNumber = 0;
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
