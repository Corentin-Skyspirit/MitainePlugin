package io.papermc.mitaine.teleport;

import io.papermc.mitaine.MitaineMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public class Teleport implements CommandExecutor {
    private final MitaineMain main;

    public Teleport(MitaineMain mitaineMain) {
        this.main = mitaineMain;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {

        if (sender instanceof Player player) {

            if (cmd.getName().equalsIgnoreCase("setspawn")) {
                FileConfiguration config = main.getConfig();
                if (args.length == 0) {
                    config.set("spawnPoint.x", player.getLocation().getBlockX());
                    config.set("spawnPoint.y", player.getLocation().getBlockY());
                    config.set("spawnPoint.z", player.getLocation().getBlockZ());
                    main.saveConfig();
                } else {
                    player.sendMessage(config.getString("erreur") + "Faites /setspawn");
                }
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("spawn")) {
                FileConfiguration config = main.getConfig();
                if (args.length == 0) {
                    if (player.getWorld().getName().equalsIgnoreCase("world")) {
                        player.teleport(new Location(player.getWorld(), config.getInt("spawnPoint.x"), config.getInt("spawnPoint.y"), config.getInt("spawnPoint.z")));
                    } else {
                        player.sendMessage(config.getString("erreur") + "La commande ne peut se faire que dans le monde normal (overworld)");
                    }
                } else {
                    player.sendMessage(config.getString("erreur") + "Faites /spawn");
                }
                return true;

            } else if (cmd.getName().equalsIgnoreCase("sethome")) {
                FileConfiguration config = main.getConfig();
                if (args.length == 1) {
                    if (config.getString(player.getUniqueId() + ".teleports." + args[0] + ".world") == null) {
                        config.set(player.getUniqueId() + ".teleports." + args[0] + ".world", player.getWorld().getName());
                        config.set(player.getUniqueId() + ".teleports." + args[0] + ".x", player.getLocation().getBlockX());
                        config.set(player.getUniqueId() + ".teleports." + args[0] + ".y", player.getLocation().getBlockY());
                        config.set(player.getUniqueId() + ".teleports." + args[0] + ".z", player.getLocation().getBlockZ());
                        main.saveConfig();
                        player.sendMessage("Le point de téléportation \"" + config.getString("important") + args[0] + config.getString("normal") + "\" a été défini");
                    } else {
                        player.sendMessage("Le point a déjà été défini");
                        // Faire des trucs cliquables
                    }
                } else {
                    player.sendMessage(config.getString("erreur") + "Faites /sethome <nom>");
                }
                return true;

            } else if (cmd.getName().equalsIgnoreCase("home")) {
                FileConfiguration config = main.getConfig();
                if (args.length == 1) {
                    if (player.getWorld().getName().equalsIgnoreCase(config.getString(player.getUniqueId() + ".teleports." + args[0] + ".world"))) {
                        int x = config.getInt(player.getUniqueId() + ".teleports." + args[0] + ".x");
                        int z = config.getInt(player.getUniqueId() + ".teleports." + args[0] + ".z");
                        player.teleport(new Location(player.getWorld(), x, config.getInt(player.getUniqueId() + ".teleports." + args[0] + ".y"), z));
                        double distance = distance(player.getLocation().getBlockX(), player.getLocation().getBlockZ(), x, z);
                        Bukkit.getLogger().info(player.getLocation().getBlockX() + " " + player.getLocation().getBlockZ() + " " + x + " " + z);
                        player.sendMessage("Vous étiez à " + config.getString("important") + distance + config.getString("normal") + " blocs du point");
                    } else if (config.getString(player.getUniqueId() + ".teleports." + args[0] + ".world") == null) {
                        player.sendMessage("Le point de téléportation \"" + config.getString("important") + args[0] + config.getString("normal") + "\" n'existe pas");
                    } else {
                        player.sendMessage("La commande ne peut se faire que dans le monde du point de téléportation");
                    }
                } else {
                    player.sendMessage(config.getString("erreur") + "Faites /spawn");
                }
                return true;
            }
        }
        return false;
    }

    private double distance(int xa, int za, int xb, int zb) {
        return sqrt(pow(xa - xb, 2) + pow(za - zb, 2));
    }
}
