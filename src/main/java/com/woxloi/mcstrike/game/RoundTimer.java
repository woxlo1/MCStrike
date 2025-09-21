package com.woxloi.mcstrike.game;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class RoundTimer {

    private final JavaPlugin plugin;
    private final BossBar bossBar;
    private final int duration; // 秒
    private int timeLeft;
    private final Set<Player> players = new HashSet<>();
    private BukkitRunnable task;

    public RoundTimer(JavaPlugin plugin, String title, int duration) {
        this.plugin = plugin;
        this.duration = duration;
        this.timeLeft = duration;
        this.bossBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
    }

    public void addPlayer(Player player) {
        players.add(player);
        bossBar.addPlayer(player);
    }

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft--;
                double progress = (double) timeLeft / duration;
                bossBar.setProgress(progress);

                if (timeLeft <= 0) {
                    cancel();
                    bossBar.setVisible(false);
                }
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancel() {
        if (task != null) task.cancel();
        bossBar.setVisible(false);
    }

    public void setTitle(String title) {
        bossBar.setTitle(title);
    }
}
