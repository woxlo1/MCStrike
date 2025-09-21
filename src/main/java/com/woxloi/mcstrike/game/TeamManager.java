package com.woxloi.mcstrike.game;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

public class TeamManager {
    private final Set<Player> attackers = new HashSet<>();
    private final Set<Player> defenders = new HashSet<>();

    public void addPlayerToAttackers(Player player) {
        attackers.add(player);
        defenders.remove(player);
    }

    public void addPlayerToDefenders(Player player) {
        defenders.add(player);
        attackers.remove(player);
    }

    public Set<Player> getAttackers() {
        return attackers;
    }

    public Set<Player> getDefenders() {
        return defenders;
    }

    public void clearTeams() {
        attackers.clear();
        defenders.clear();
    }
}
