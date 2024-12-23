package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class GUIManager {
    private SecretSanta plugin;
    public GUIManager(SecretSanta plugin) { this.plugin = plugin; }

    public Inventory getSenderGUI(Player player, OfflinePlayer recipient) {
        Inventory inv = plugin.getServer().createInventory(player, 9, CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.DARK_RED + recipient.getName() + ChatColor.GRAY + "(Gifter)");
        inv.setItem(7, ItemBuilder.separator());
        inv.setItem(8, ItemBuilder.closeButton());
        return inv;
    }

    public Inventory getRecipientGUI(Player player) {
        Inventory inv = plugin.getServer().createInventory(player, 9, CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.DARK_GREEN + "Here's your gift!");
        inv.setItem(7, ItemBuilder.separator());
        inv.setItem(8, ItemBuilder.closeButton());

        // Temporary filler code!
        for (int i = 0; i < 7; i++) {
            inv.setItem(i, ItemBuilder.separator());
        }

        //List<?> gifts = plugin.getConfig().getList("SecretSanta." + player.getName() + ".gifts");
        //if (gifts == null) {
        //    return inv;
        //}
        //for (Object gift : gifts) {
        //    ItemStack giftItem = ItemStack.deserialize();
        //}
        return inv;
    }
}
