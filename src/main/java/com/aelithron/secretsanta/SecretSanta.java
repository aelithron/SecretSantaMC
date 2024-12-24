package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public final class SecretSanta extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CoreTools.getInstance().setPlugin(this);
        getCommand("secretsanta").setExecutor(new SecretSantaCMD(this));
        getCommand("ssadmin").setExecutor(new AdminCMD(this));
        CoreTools.getInstance().checkForUpdates();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void reloadPlugin() {
        reloadConfig();
        CoreTools.getInstance().setPlugin(this);
        CoreTools.getInstance().checkForUpdates();
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        ConfigurationSection secretSanta = getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return;
        }
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Gifter")) {
            UUID gifterUUID = e.getPlayer().getUniqueId();
            UUID gifteeUUID = CoreTools.getInstance().getGifteeFromGifter(gifterUUID);
            if (gifteeUUID == null) {
                for (ItemStack itemFromInv : e.getInventory().getContents()) {
                    if (itemFromInv != null && itemFromInv.getType() != Material.AIR && !itemFromInv.isSimilar(ItemBuilder.separator()) && !itemFromInv.isSimilar(ItemBuilder.closeButton())) {
                        e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), itemFromInv);
                    }
                }
                e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Due to an error, your recipient could not be found! The items have been dropped on the ground.");
                return;
            }
            secretSanta.set(gifteeUUID + ".gifts", null);
            int itemIndex = 0;
            for (ItemStack item : e.getInventory().getContents()) {
                if (item != null && item.getType() != Material.AIR && !item.isSimilar(ItemBuilder.separator()) && !item.isSimilar(ItemBuilder.closeButton())) {
                    secretSanta.set(gifteeUUID + ".gifts." + itemIndex, item);
                    itemIndex++;
                }
            }
            saveConfig();
            e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "You have successfully saved your gifts to " + getServer().getOfflinePlayer(gifteeUUID).getName() + ".");
            return;
        }
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Recipient")) {
            UUID gifteeUUID = e.getPlayer().getUniqueId();
            if (getConfig().getBoolean("SecretSanta." + gifteeUUID + ".claimed")) {
                e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You have already claimed your gift!");
                return;
            }
            List<ItemStack> gifts = CoreTools.getInstance().deserializeGifts(gifteeUUID);
            if (gifts == null) { return; }
            for (ItemStack gift : gifts) { e.getPlayer().getInventory().addItem(gift); }
            getConfig().set("SecretSanta." + gifteeUUID + ".claimed", true);
            e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Your gift has been delivered!");
            return;
        }
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.DARK_RED + " Admin")) {
            UUID gifteeUUID = CoreTools.getInstance().getAdminView(e.getPlayer().getUniqueId());
            if (gifteeUUID == null) {
                for (ItemStack itemFromInv : e.getInventory().getContents()) {
                    if (itemFromInv != null && itemFromInv.getType() != Material.AIR && !itemFromInv.isSimilar(ItemBuilder.separator()) && !itemFromInv.isSimilar(ItemBuilder.closeButton())) {
                        e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), itemFromInv);
                    }
                }
                e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "(ADMIN MODE) Due to an error, the recipient could not be found! The items have been dropped on the ground.");
                return;
            }
            secretSanta.set(gifteeUUID + ".gifts", null);
            int itemIndex = 0;
            for (ItemStack item : e.getInventory().getContents()) {
                if (item != null && item.getType() != Material.AIR && !item.isSimilar(ItemBuilder.separator()) && !item.isSimilar(ItemBuilder.closeButton())) {
                    secretSanta.set(gifteeUUID + ".gifts." + itemIndex, item);
                    itemIndex++;
                }
            }
            saveConfig();
            CoreTools.getInstance().setAdminView(e.getPlayer().getUniqueId(), null);
            e.getPlayer().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "(ADMIN MODE) You have successfully saved gift data for " + getServer().getOfflinePlayer(gifteeUUID).getName() + ".");
            return;
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Gifter") || e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.DARK_RED + " Admin")) {
            switch (e.getRawSlot()) {
                case 7:
                    e.setCancelled(true);
                    return;
                case 8:
                    e.setCancelled(true);
                    e.getWhoClicked().closeInventory();
                    return;
                default:
                    return;
            }
        }
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "-" + ChatColor.GRAY + " Recipient")) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            return;
        }
    }
}