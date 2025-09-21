package com.woxloi.mcstrike;

import org.bukkit.plugin.java.JavaPlugin;

public class mcstrike extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("mcstrike が有効になりました！");
        // コマンド登録
        this.getCommand("mcstrike").setExecutor(new com.woxloi.mcstrike.commands.StrikeCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("mcstrike が無効になりました！");
    }
}
