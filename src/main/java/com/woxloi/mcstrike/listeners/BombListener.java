package com.woxloi.mcstrike.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.woxloi.mcstrike.mcstrike;

import java.util.HashMap;
import java.util.Map;

public class BombListener implements Listener {

    private final mcstrike plugin;
    private final Map<Player, BukkitRunnable> sneakTasks = new HashMap<>();
    private final int SNEAK_TIME = 5; // 秒

    public BombListener(mcstrike plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        // TNTを持っていない場合は無視
        if (player.getInventory().getItemInMainHand().getType() != Material.TNT) return;

        // シフト押下
        if (event.isSneaking()) {

            // 既にタイマーがある場合は無視
            if (sneakTasks.containsKey(player)) return;

            BukkitRunnable task = new BukkitRunnable() {
                int timeLeft = SNEAK_TIME;

                @Override
                public void run() {
                    if (!player.isSneaking()) {
                        player.sendMessage("§c設置/解除をキャンセルしました。");
                        cancel();
                        sneakTasks.remove(player);
                        return;
                    }

                    if (timeLeft <= 0) {
                        // シフト5秒保持成功 → 爆弾設置 or 解除
                        var bombManager = plugin.getGameManager().getBombManager();
                        if (!bombManager.isBombExploded() && !bombManager.isBombDefused() && !bombManager.isBombPlanted()) {
                            bombManager.plantBomb(player.getLocation());
                            mcstrike.sendPrefixMessage(player, "爆弾を設置しました！");
                        } else if (bombManager.isBombPlanted()) {
                            bombManager.defuseBomb();
                            mcstrike.sendPrefixMessage(player, "爆弾を解除しました！");
                        }


                        cancel();
                        sneakTasks.remove(player);
                        return;
                    }

                    timeLeft--;
                }
            };

            task.runTaskTimer(plugin, 0L, 20L); // 1秒ごと
            sneakTasks.put(player, task);

        } else {
            // シフト離した場合
            if (sneakTasks.containsKey(player)) {
                sneakTasks.get(player).cancel();
                sneakTasks.remove(player);
                player.sendMessage("§c設置/解除をキャンセルしました。");
            }
        }
    }
}
