package com.woxloi.mcstrike.game;

import com.woxloi.mcstrike.mcstrike;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BombManager {

    private final GameManager gameManager;
    private boolean bombPlanted = false;
    private boolean bombExploded = false;
    private boolean bombDefused = false;
    private int bombTimer = 30; // 爆弾カウントダウン秒
    private BukkitRunnable task;

    private ArmorStand bombEntity;
    private Location bombLocation;

    public BombManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void plantBomb(Location loc) {
        if (bombPlanted) return;

        bombPlanted = true;
        bombExploded = false;
        bombDefused = false;
        bombLocation = loc;

        // ArmorStandで爆弾を視覚表示
        bombEntity = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class);
        bombEntity.setVisible(false);
        bombEntity.setGravity(false);
        bombEntity.setMarker(true);
        bombEntity.setCustomName("§c爆弾");
        bombEntity.setCustomNameVisible(true);
        bombEntity.getEquipment().setHelmet(new ItemStack(Material.TNT));

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

                    // 🔹 爆発処理
                    explodeBomb();

                    gameManager.checkRoundEnd();
                    cancel();
                    return;
                }

                // 点滅アニメーション
                bombEntity.setCustomName(timeLeft % 2 == 0 ? "§c爆弾" : "§6爆弾");

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
        removeBombDisplay();
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
        removeBombDisplay();
    }

    private void removeBombDisplay() {
        if (bombEntity != null) {
            bombEntity.remove();
            bombEntity = null;
        }
    }

    public Location getBombLocation() {
        return bombLocation;
    }

    // 🔹 爆発処理
    private void explodeBomb() {
        if (bombLocation == null) return;

        // 爆発の範囲（半径）
        double radius = 5.0;
        double damage = 10.0;

        // 周囲プレイヤーにダメージ
        for (Player player : bombLocation.getWorld().getPlayers()) {
            if (player.getLocation().distance(bombLocation) <= radius) {
                player.damage(damage);
            }
        }

        // 爆発エフェクト
        bombLocation.getWorld().createExplosion(bombLocation, 0F, false, false); // 爆発音と煙のみ

        // ArmorStand削除
        removeBombDisplay();

        bombPlanted = false;
    }
}
