package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    public static ItemStack closeButton() {
        ItemStack closeItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta closeMeta = closeItem.getItemMeta();
        assert closeMeta != null;
        closeMeta.setDisplayName(ChatColor.RED + "Close Menu");
        closeItem.setItemMeta(closeMeta);
        return closeItem;
    }
    public static ItemStack separator() {
        ItemStack separator = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta separatorMeta = separator.getItemMeta();
        assert separatorMeta != null;
        separatorMeta.setDisplayName(" ");
        separator.setItemMeta(separatorMeta);
        return separator;
    }
}
