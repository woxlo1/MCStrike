package com.woxloi.mcstrike.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StrikeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("§aMCStrike は正常に動作しています！");
        } else {
            sender.sendMessage("このコマンドはプレイヤーのみが実行できます。");
        }
        return true;
    }
}
