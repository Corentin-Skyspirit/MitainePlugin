package io.papermc.mitaine;

import io.papermc.mitaine.courrier.Courrier;
import io.papermc.mitaine.economie.Economie;
import io.papermc.mitaine.teleport.Teleport;
import io.papermc.mitaine.vote.Vote;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MitaineMain extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getLogger().info("Merci d'utiliser Mitaine Economy");
        getCommand("economie").setExecutor(new Economie(this));
        getCommand("achetericone").setExecutor(new Economie(this));

        getCommand("vote").setExecutor(new Vote(this));
        getCommand("creervote").setExecutor(new Vote(this));
        getCommand("resultats").setExecutor(new Vote(this));
        getServer().getPluginManager().registerEvents(new Vote(this), this);

        getCommand("courrier").setExecutor(new Courrier(this));
        getServer().getPluginManager().registerEvents(new Courrier(this), this);

        getCommand("setspawn").setExecutor(new Teleport(this));
        getCommand("spawn").setExecutor(new Teleport(this));
        getCommand("sethome").setExecutor(new Teleport(this));
        getCommand("home").setExecutor(new Teleport(this));
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Au revoir");
    }
}
