package io.papermc.mitaine;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MitaineEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Merci d'utiliser Mitaine Economy");
        getCommand("banque").setExecutor(new Economie());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Au revoir");
    }
}
