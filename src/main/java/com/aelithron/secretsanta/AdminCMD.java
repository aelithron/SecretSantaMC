package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCMD implements CommandExecutor {
    private final SecretSanta plugin;
    public AdminCMD(SecretSanta plugin) { this.plugin = plugin; }
    String prefix = CoreTools.getInstance().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("secretsanta.admin")) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if (args.length == 0) {
            sendHelpMessage(sender);
            return false;
        }
        if (args[0].equalsIgnoreCase("mode")) {
            if (args.length != 2) {
                sendHelpMessage(sender);
                return false;
            }
            plugin.reloadConfig();
            if (args[1].equalsIgnoreCase("give")) {
                plugin.getConfig().set("Mode", false);
                sender.sendMessage(prefix + ChatColor.GREEN + "Secret Santa mode set to give! Players can now give gifts to their assignments.");
            } else if (args[1].equalsIgnoreCase("get")) {
                plugin.getConfig().set("Mode", true);
                sender.sendMessage(prefix + ChatColor.GREEN + "Secret Santa mode set to get! Assignees can now claim their gifts.");
            } else {
                sendHelpMessage(sender);
                return false;
            }
            plugin.saveConfig();
            return true;
        } else if (args[0].equalsIgnoreCase("claimmenu")) {
            if (args.length != 2) {
                sendHelpMessage(sender);
                return false;
            }
            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[1]);
            if (player.hasPlayedBefore()) {
                sender.sendMessage(prefix + ChatColor.RED + "Player not found!");
                return false;
            }

            return true;
        } else if (args[0].equalsIgnoreCase("markclaimed")) {
            if (args.length != 3) {
                sendHelpMessage(sender);
                return false;
            }
            Player player = plugin.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(prefix + ChatColor.RED + "Player not found!");
                return false;
            }
            plugin.reloadConfig();
            if (args[2].equalsIgnoreCase("true")) {
                plugin.getConfig().set("SecretSanta." + player.getUniqueId() + ".claimed", true);
                sender.sendMessage(prefix + ChatColor.GREEN + "Player " + player.getName() + " has been marked as having claimed their gift.");
            } else if (args[2].equalsIgnoreCase("false")) {
                plugin.getConfig().set("SecretSanta." + player.getUniqueId() + ".claimed", false);
                sender.sendMessage(prefix + ChatColor.GREEN + "Player " + player.getName() + " has been marked as not having claimed their gift.");
            } else {
                sendHelpMessage(sender);
                return false;
            }
            plugin.saveConfig();
            return true;
        } else if (args[0].equalsIgnoreCase("autoassign")) {
            List<String> autoAssign = plugin.getConfig().getStringList("AutoAssign");
            if (autoAssign.isEmpty()) {
                sender.sendMessage(prefix + ChatColor.RED + "Auto-Assign list is empty!");
                return false;
            }
            if (autoAssign.size() % 2 != 0) {
                sender.sendMessage(prefix + ChatColor.RED + "Auto-Assign list must have an even number of entries!");
                return false;
            }
            return true;
        }
        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(prefix + ChatColor.RED + "Incorrect usage!");
        sender.sendMessage(ChatColor.DARK_AQUA + "Secret Santa Admin Command Help");
        sender.sendMessage(ChatColor.AQUA + "/ssadmin mode (give|get): Changes the Secret Santa mode. Give means players give gifts, get means that players can claim the gifts they were given.");
        if (sender instanceof Player) { sender.sendMessage(ChatColor.AQUA + "/ssadmin claimmenu (player): Opens the claim menu for the entered player."); }
        sender.sendMessage(ChatColor.AQUA + "/ssadmin markclaimed (player) (false|true): Marks whether or not the player has claimed their gift.");
        sender.sendMessage(ChatColor.AQUA + "/ssadmin autoassign: Automatically assigns players to gift to other players. Provide the list of auto assign usernames in the configuration file.");
        sender.sendMessage(ChatColor.AQUA + "/ssadmin reload: Reloads the plugin and configs.");
    }
}