package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SecretSantaCMD implements CommandExecutor {
    private final SecretSanta plugin;
    public SecretSantaCMD(SecretSanta plugin) { this.plugin = plugin; }
    String prefix = CoreTools.getInstance().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command!");
            return false;
        }

        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(prefix + ChatColor.RED + "Incorrect usage!");
        sender.sendMessage(ChatColor.DARK_AQUA + "Secret Santa Command Help");
        sender.sendMessage(ChatColor.AQUA + "/secretsanta info: Shows your assigned player.");
        if (plugin.getConfig().getBoolean("Mode")) {
            sender.sendMessage(ChatColor.AQUA + "/secretsanta claim: Lets you claim your gifts.");
        } else {
            sender.sendMessage(ChatColor.AQUA + "/secretsanta give: Lets you give a gift.");
        }
    }
}