package com.woxloi.mcstrike.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final List<Player> attackers = new ArrayList<>();
    private final List<Player> defenders = new ArrayList<>();

    private Location attackSpawn;
    private Location defenseSpawn;

    public void setAttackSpawn(Location loc) { this.attackSpawn = loc; }
    public void setDefenseSpawn(Location loc) { this.defenseSpawn = loc; }

    public void addPlayerToAttackers(Player player) { attackers.add(player); }
    public void addPlayerToDefenders(Player player) { defenders.add(player); }

    public List<Player> getAttackers() { return attackers; }
    public List<Player> getDefenders() { return defenders; }

    // 🔹 全員をスポーン位置にテレポート
    public void teleportTeams() {
        if (attackSpawn != null) attackers.forEach(p -> p.teleport(attackSpawn));
        if (defenseSpawn != null) defenders.forEach(p -> p.teleport(defenseSpawn));
    }

    // 🔹 チームを安全に入れ替える
    public void swapTeams() {
        List<Player> tempAttackers = new ArrayList<>(attackers);
        List<Player> tempDefenders = new ArrayList<>(defenders);

        attackers.clear();
        defenders.clear();

        attackers.addAll(tempDefenders);
        defenders.addAll(tempAttackers);
    }
}
