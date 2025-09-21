package com.woxloi.mcstrike.game;

import com.woxloi.mcstrike.mcstrike;
import org.bukkit.scheduler.BukkitRunnable;

public class BombManager {

    private final GameManager gameManager;
    private boolean bombPlanted = false;
    private boolean bombExploded = false;
    private boolean bombDefused = false;
    private int bombTimer = 30; // 爆弾カウントダウン秒

    private BukkitRunnable task;

    public BombManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void plantBomb() {
        if (bombPlanted) return;

        bombPlanted = true;
        bombExploded = false;
        bombDefused = false;

        // 爆弾タイマー開始
        task = new BukkitRunnable() {
            int timeLeft = bombTimer;

            @Override
            public void run() {
                if (!bombPlanted) {
                    cancel();
                    return;
                }

                if (timeLeft <= 0) {
                    bombExploded = true;
                    gameManager.checkRoundEnd();
                    cancel();
                    return;
                }

                timeLeft--;
            }
        };

        task.runTaskTimer(mcstrike.getPlugin(mcstrike.class), 0L, 20L);
    }

    public void defuseBomb() {
        if (!bombPlanted || bombDefused) return;

        bombDefused = true;
        bombPlanted = false;
        if (task != null) task.cancel();

        gameManager.checkRoundEnd();
    }

    public boolean isBombExploded() {
        return bombExploded;
    }

    public boolean isBombDefused() {
        return bombDefused;
    }

    public boolean isBombPlanted() {
        return bombPlanted;
    }
    
    public void cancelBomb() {
        bombPlanted = false;
        if (task != null) task.cancel();
    }
}
