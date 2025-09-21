package com.woxloi.mcstrike.game;

import com.woxloi.mcstrike.mcstrike;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundTimer {

    private final mcstrike plugin;
    private final BossBar bossBar;
    private BukkitRunnable task;

    public RoundTimer(mcstrike plugin, String title, int durationSeconds) {
        this.plugin = plugin;
        this.bossBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
        this.bossBar.setVisible(true);
        this.bossBar.setProgress(1.0);

        startTimer(durationSeconds);
    }

    private void startTimer(int durationSeconds) {
        AtomicInteger timeLeft = new AtomicInteger(durationSeconds);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                int remaining = timeLeft.getAndDecrement();
                if (remaining < 0) {
                    cancel();
                    bossBar.removeAll();
                    plugin.getGameManager().endRound("タイムアップ");
                    return;
                }

                double progress = remaining / (double) durationSeconds;
                bossBar.setProgress(progress);
            }
        };
        task.runTaskTimer(plugin, 0L, 20L); // 1秒ごと
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }

    public void cancel() {
        if (task != null) task.cancel();
        bossBar.removeAll();
    }
}
