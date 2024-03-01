package io.papermc.mitaine.courrier;

import io.papermc.mitaine.MitaineMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class Courrier implements CommandExecutor, Listener, TabCompleter {
    private MitaineMain main;

    public Courrier(MitaineMain mitaineMain) {
        this.main = mitaineMain;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        main.reloadConfig();
        FileConfiguration config = main.getConfig();
        player.sendMessage("Vous avez §c" + config.getString(player.getUniqueId()+".courriers.nombre") + "§f courriers en attente");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("courrier")) {
            if (args[0].equalsIgnoreCase("envoyer")) {
                if (args.length >= 3) {
                    StringBuilder message = new StringBuilder();
                    int cpt = 0;
                    UUID reciever = null;
                    for (String mot : args) {
                        if (cpt == 1) {
                            reciever = Bukkit.getPlayerUniqueId(mot);
                        }
                        if (cpt >= 2) {
                            message.append(mot + " ");
                        }
                        cpt++;
                    }
                    /*
                    Écrire sur le message.yml les messages (ou txt parce que pas clair)
                    receiver -> UUID du mec qui reçoit le msg
                    sender -> player qui envoie le message (récup le nom)
                    message -> message à stocker
                    */

                    Bukkit.getPlayer(reciever).sendMessage("Vous avez reçu un message !");
                } else {
                    sender.sendMessage("§cLa commande est /message envoyer <message>");
                }
            } else if (args[0].equalsIgnoreCase("lire")) {
                if (args.length >= 3) {

                }
            } else if (args[0].equalsIgnoreCase("supprimer")) {
                if (args.length >= 3) {

                }
            } else {
                sender.sendMessage("§cLa commande est /message <option>");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            List<String> completions = new ArrayList<>();
            completions.add("envoyer");
            completions.add("lire");
            completions.add("supprimer");
            return completions;
        }
        return null;
    }
}
