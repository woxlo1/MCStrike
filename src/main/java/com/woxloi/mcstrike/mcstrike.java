package com.woxloi.mcstrike;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.woxloi.mcstrike.game.GameManager;

public class mcstrike extends JavaPlugin {

    public static final String prefix = "§6[§4§lMCStrike§6]§f";
    private GameManager gameManager;

    @Override
    public void onEnable() {
        getLogger().info("mcstrike が有効になりました！");
        this.getCommand("mcstrike").setExecutor(new com.woxloi.mcstrike.commands.StrikeCommand());

        getServer().getPluginManager().registerEvents(new com.woxloi.mcstrike.listeners.BombListener(this), this);

        gameManager = new GameManager();
    }

    @Override
    public void onDisable() {
        getLogger().info("mcstrike が無効になりました！");
    }

    public static void sendPrefixMessage(CommandSender sender, String msg) {
        sender.sendMessage(prefix + msg);
    }

    public static void BroadPrefixMessage(CommandSender sender, String msg) {
        Bukkit.broadcastMessage(prefix + msg);
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
