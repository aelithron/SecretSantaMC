package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
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

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        OfflinePlayer giftRecipient;
        ConfigurationSection secretSanta = getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return;
        }
        for (String playerUUIDstr : secretSanta.getKeys(false)) {
            if (e.getPlayer().getUniqueId().equals(UUID.fromString(Objects.requireNonNull(secretSanta.getString(playerUUIDstr + ".assigned"))))) {
                giftRecipient = getServer().getOfflinePlayer(UUID.fromString(Objects.requireNonNull(secretSanta.getString("SecretSanta." + playerUUIDstr + ".assigned"))));
            }
        }
        if (e.getView().getTitle().equals(CoreTools.getInstance().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.GRAY + "(Gifter)")) {

        }
    }
}