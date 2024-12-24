package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SecretSantaCMD implements CommandExecutor {
    private final SecretSanta plugin;
    public SecretSantaCMD(SecretSanta plugin) { this.plugin = plugin; }
    String prefix = CoreTools.getInstance().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if (args.length == 0) {
            UUID gifteeUUID = CoreTools.getInstance().getGifteeFromGifter(player.getUniqueId());
            if (gifteeUUID == null) {
                sender.sendMessage(prefix + ChatColor.RED + "You are not assigned to anyone!");
            } else {
                sender.sendMessage(prefix + ChatColor.GREEN + "You are assigned to " + plugin.getServer().getOfflinePlayer(gifteeUUID).getName() + ".");
                if (CoreTools.getInstance().hasReceievedGift(gifteeUUID)) {
                    sender.sendMessage(ChatColor.GREEN + "You have given a gift. To change it, use '/secretsanta menu'.");
                } else {
                    sender.sendMessage(ChatColor.RED + "You haven't given any gift yet! Give a gift using '/secretsanta menu'.");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("menu")) {
            if (plugin.getConfig().getBoolean("Mode")) {
                UUID gifteeUUID = CoreTools.getInstance().getGifteeFromGifter(player.getUniqueId());
                if (gifteeUUID == null) {
                    sender.sendMessage(prefix + ChatColor.RED + "You are not assigned to anyone!");
                } else {
                    GUIManager guiManager = new GUIManager(plugin);
                    Inventory inv = guiManager.getGifterGUI(player);
                    if (inv == null) {
                        sender.sendMessage(prefix + ChatColor.RED + "There was an error opening the gifting menu!");
                        return false;
                    }
                }
            } else {
                UUID gifterUUID = CoreTools.getInstance().getGifterFromGiftee(player.getUniqueId());
                if (gifterUUID == null) {
                    sender.sendMessage(prefix + ChatColor.RED + "You are not assigned to anyone!");
                } else {
                    GUIManager guiManager = new GUIManager(plugin);
                    Inventory inv = guiManager.getGifteeGUI(player);
                    if (inv == null) {
                        sender.sendMessage(prefix + ChatColor.RED + "There was an error opening the receiving menu!");
                        return false;
                    }
                }
            }
            return true;
        } else {
            sendHelpMessage(sender);
        }
        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(prefix + ChatColor.RED + "Incorrect usage!");
        sender.sendMessage(ChatColor.DARK_AQUA + "Secret Santa Command Help");
        sender.sendMessage(ChatColor.AQUA + "/secretsanta: Shows your assigned player.");
        if (plugin.getConfig().getBoolean("Mode")) {
            sender.sendMessage(ChatColor.AQUA + "/secretsanta menu: Lets you claim your gifts.");
        } else {
            sender.sendMessage(ChatColor.AQUA + "/secretsanta menu: Lets you give a gift.");
        }
    }
}