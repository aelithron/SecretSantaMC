package com.aelithron.secretsanta;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class CoreTools {
    private SecretSanta plugin;
    private static CoreTools instance;
    private Map<UUID, UUID> adminsViewingMenus = new HashMap<>();

    public static CoreTools getInstance() {
        if (instance == null) {
            instance = new CoreTools();
        }
        return instance;
    }

    void setPlugin(SecretSanta plugin) { this.plugin = plugin; }

    public String getPrefix() {
        if (plugin.getConfig().getString("Prefix") == null) { return ""; }
        return (ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("Prefix"))) + " ");
    }

    public void checkForUpdates() {
        if (plugin.getConfig().getBoolean("CheckForUpdates")) {
            HttpRequest check = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://api.github.com/repos/aelithron/SecretSantaMC/releases/latest"))
                    .build();
            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(check, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                plugin.getLogger().info("Failed to check for updates.");
                e.printStackTrace();
            }
            assert response != null;
            String json = response.body();
            String latestVersion = json.substring(json.indexOf("tag_name") + 11, json.indexOf('"', json.indexOf("tag_name") + 11));
            String version = "v" + plugin.getDescription().getVersion();
            if (!version.equals(latestVersion)) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getLogger().info("An update is available!");
                    plugin.getLogger().info("Your version: " + version + " - Latest version: " + latestVersion);
                    plugin.getLogger().info("Please update at https://github.com/aelithron/SecretSantaMC/releases!");
                }, 60L);
            }
        }
    }

    public UUID getGifteeFromGifter(UUID gifter) {
        ConfigurationSection secretSanta = plugin.getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            plugin.getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return null;
        }
        for (String playerUUIDstr : secretSanta.getKeys(false)) {
            UUID gifteeAssignment = UUID.fromString(Objects.requireNonNull(secretSanta.getString(playerUUIDstr + ".gifter")));
            if (gifter.equals(gifteeAssignment)) {
                return UUID.fromString(playerUUIDstr);
            }
        }
        return null;
    }
    public UUID getGifterFromGiftee(UUID giftee) {
        ConfigurationSection secretSanta = plugin.getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            plugin.getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return null;
        }
        if (!secretSanta.contains(String.valueOf(giftee))) { return null; }
        return UUID.fromString(Objects.requireNonNull(secretSanta.getString(giftee + ".gifter")));
    }

    public Boolean hasReceievedGift(UUID giftee) {
        ConfigurationSection secretSanta = plugin.getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            plugin.getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return null;
        }
        ConfigurationSection gifts = secretSanta.getConfigurationSection(giftee + ".gifts");
        return gifts != null && !gifts.getKeys(false).isEmpty();
    }

    public List<ItemStack> deserializeGifts(UUID giftee) {
        ConfigurationSection secretSanta = plugin.getConfig().getConfigurationSection("SecretSanta");
        if (secretSanta == null) {
            plugin.getLogger().severe("Secret Santa config is missing! This is a MAJOR error and will break the plugin!");
            return null;
        }
        ConfigurationSection gifts = secretSanta.getConfigurationSection(giftee + ".gifts");
        if (gifts == null) { return null; }
        List<ItemStack> giftList = new ArrayList<>();
        for (String key : gifts.getKeys(false)) {
            giftList.add(gifts.getItemStack(key));
        }
        return giftList;
    }

    void setAdminView(UUID admin, UUID viewing) {
        if (viewing == null) {
            adminsViewingMenus.remove(admin);
            return;
        }
        adminsViewingMenus.put(admin, viewing);
    }
    UUID getAdminView(UUID admin) { return adminsViewingMenus.get(admin); }
}
