package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GUIManager {
    private SecretSanta plugin;
    public GUIManager(SecretSanta plugin) { this.plugin = plugin; }

    public Inventory getGifterGUI(Player player) {
        Inventory inv = plugin.getServer().createInventory(player, 9, CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Gifter");
        UUID giftRecipient = CoreTools.getInstance().getGifteeFromGifter(player.getUniqueId());
        if (giftRecipient == null) {
            return null;
        }
        inv.setItem(7, ItemBuilder.separator());
        inv.setItem(8, ItemBuilder.closeButton());

        int checkSlotCount = 0; // This is here to make sure we don't overflow the GUI :3
        for (ItemStack gift : CoreTools.getInstance().deserializeGifts(giftRecipient)) {
            if (checkSlotCount >= 7) { break; }
            inv.addItem(gift);
            checkSlotCount++;
        }
        return inv;
    }

    public Inventory getGifteeGUI(Player player) {
        Inventory inv = plugin.getServer().createInventory(player, 9, CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Recipient");
        inv.setItem(7, ItemBuilder.separator());
        inv.setItem(8, ItemBuilder.claimButton());

        int checkSlotCount = 0; // This is here to make sure we don't overflow the GUI :3
        for (ItemStack gift : CoreTools.getInstance().deserializeGifts(player.getUniqueId())) {
            if (checkSlotCount >= 7) { break; }
            inv.addItem(gift);
            checkSlotCount++;
        }
        return inv;
    }

    public Inventory getAdminGUI(Player admin, UUID playerToCheck) {
        Inventory inv = plugin.getServer().createInventory(admin, 9, CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.DARK_RED + " Admin");
        inv.setItem(7, ItemBuilder.separator());
        inv.setItem(8, ItemBuilder.closeButton());

        int checkSlotCount = 0; // This is here to make sure we don't overflow the GUI :3
        for (ItemStack gift : CoreTools.getInstance().deserializeGifts(playerToCheck)) {
            if (checkSlotCount >= 7) { break; }
            inv.addItem(gift);
            checkSlotCount++;
        }
        return inv;
    }
}
