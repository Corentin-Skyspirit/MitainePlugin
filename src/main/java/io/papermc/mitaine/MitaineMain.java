package io.papermc.mitaine;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MitaineMain extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Merci d'utiliser Mitaine Economy");
        getCommand("economie").setExecutor(new Economie());
//        getCommand("achetericone").setExecutor(new Economie());
//        getServer().getPluginManager().registerEvents(new Economie(), this);
//
        getCommand("vote").setExecutor(new Vote());
        getCommand("creervote").setExecutor(new Vote());
        getCommand("resultats").setExecutor(new Vote());
//
//        getCommand("courrier").setExecutor(new Courrier());
//        getServer().getPluginManager().registerEvents(new Courrier(), this);
//
//        getCommand("spawn").setExecutor(new Teleport());
//        getCommand("base").setExecutor(new Teleport());
//        getCommand("home").setExecutor(new Teleport());
//        getCommand("sethome").setExecutor(new Teleport());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Au revoir");
    }
}
