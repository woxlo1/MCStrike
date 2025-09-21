package com.woxloi.mcstrike.commands;

import com.woxloi.mcstrike.mcstrike;
import com.woxloi.mcstrike.game.GameManager;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrikeCommand implements CommandExecutor, TabCompleter {

    private final GameManager gameManager = mcstrike.getPlugin(mcstrike.class).getGameManager();
    private final List<String> mainCommands = Arrays.asList(
            "start", "stop", "join", "leave", "spawnset", "info", "bomb", "reset"
    );

    private final List<String> spawnArgs = Arrays.asList("attack", "defense");
    private final List<String> bombArgs = Arrays.asList("status");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤーのみ使用可能です！");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                gameManager.startNextRound();
                mcstrike.sendPrefixMessage(player, "ラウンドを開始しました！");
                break;

            case "stop":
                gameManager.endRound("管理者による停止");
                mcstrike.sendPrefixMessage(player, "ゲームを終了しました！");
                break;

            case "join":
                autoJoin(player);
                break;

            case "leave":
                gameManager.getTeamManager().getAttackers().remove(player);
                gameManager.getTeamManager().getDefenders().remove(player);
                mcstrike.sendPrefixMessage(player, "ゲームから離脱しました！");
                break;

            case "spawnset":
                if (args.length < 2) {
                    mcstrike.sendPrefixMessage(player, "使用方法: /mcstrike spawnset [attack|defense]");
                    break;
                }
                Location loc = player.getLocation();
                if (args[1].equalsIgnoreCase("attack")) {
                    gameManager.getTeamManager().setAttackSpawn(loc);
                    mcstrike.sendPrefixMessage(player, "攻撃側スポーンを設定しました！");
                } else if (args[1].equalsIgnoreCase("defense")) {
                    gameManager.getTeamManager().setDefenseSpawn(loc);
                    mcstrike.sendPrefixMessage(player, "防御側スポーンを設定しました！");
                }
                break;

            case "info":
                mcstrike.sendPrefixMessage(player, "ラウンド " + (gameManager.isRoundActive() ? gameManager.getTeamManager().getAttackers().size() + "/" + gameManager.getTeamManager().getDefenders().size() : "未開始"));
                break;

            case "bomb":
                if (args.length >= 2 && args[1].equalsIgnoreCase("status")) {
                    mcstrike.sendPrefixMessage(player, "爆弾設置状態: " + (gameManager.getBombManager().isBombPlanted() ? "設置済み" : "未設置"));
                }
                break;

            case "reset":
                gameManager.endRound("管理者によるリセット");
                mcstrike.sendPrefixMessage(player, "ゲームをリセットしました！");
                break;

            default:
                sendHelp(player);
        }

        return true;
    }

    private void autoJoin(Player player) {
        if (gameManager.getTeamManager().getAttackers().size() <= gameManager.getTeamManager().getDefenders().size()) {
            gameManager.getTeamManager().addPlayerToAttackers(player);
            mcstrike.sendPrefixMessage(player, "攻撃側に参加しました！");
        } else {
            gameManager.getTeamManager().addPlayerToDefenders(player);
            mcstrike.sendPrefixMessage(player, "防御側に参加しました！");
        }
    }

    private void sendHelp(Player player) {
        mcstrike.sendPrefixMessage(player, "=== MCStrike コマンド一覧 ===");
        mcstrike.sendPrefixMessage(player, "/mcstrike start - ラウンド開始");
        mcstrike.sendPrefixMessage(player, "/mcstrike stop - ゲーム終了");
        mcstrike.sendPrefixMessage(player, "/mcstrike join - ゲーム参加");
        mcstrike.sendPrefixMessage(player, "/mcstrike leave - ゲーム離脱");
        mcstrike.sendPrefixMessage(player, "/mcstrike spawnset [attack|defense] - スポーン設定");
        mcstrike.sendPrefixMessage(player, "/mcstrike info - 現在ラウンド情報");
        mcstrike.sendPrefixMessage(player, "/mcstrike bomb status - 爆弾状態確認");
        mcstrike.sendPrefixMessage(player, "/mcstrike reset - ゲームリセット");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (String cmd : mainCommands) {
                if (cmd.startsWith(args[0].toLowerCase())) completions.add(cmd);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spawnset")) {
                for (String s : spawnArgs) {
                    if (s.startsWith(args[1].toLowerCase())) completions.add(s);
                }
            } else if (args[0].equalsIgnoreCase("bomb")) {
                for (String s : bombArgs) {
                    if (s.startsWith(args[1].toLowerCase())) completions.add(s);
                }
            }
        }

        return completions;
    }
}
