package io.papermc.mitaine.courrier;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class Courrier implements CommandExecutor, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        player.sendMessage("Bonjour, vous avez §c" + 0 + "§f message en attente.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("courrier")) {
            if (args[0].equalsIgnoreCase("envoyer")){
                if (args.length >= 3) {
                    StringBuilder message = new StringBuilder();
                    int cpt = 0;
                    UUID reciever = null;
                    for (String mot : args) {
                        if (cpt == 1){
                            reciever = Bukkit.getPlayerUniqueId(mot);
                        }
                        if (cpt >= 2){
                            message.append(mot + " ");
                        }
                        cpt++;
                    }
                    /*
                    Écrire sur le message.yml les messages
                    receiver -> UUID du mec qui reçoit le msg
                    message -> message à stocker
                    sender -> player qui envoie le message (récup le nom)
                    */
                    Bukkit.getPlayer(reciever).sendMessage("Vous avez reçu un message !");
                } else {
                    sender.sendMessage("§cLa commande est /message envoyer <message>");
                }
            } else {
                sender.sendMessage("§cLa commande est /message <option>");
            }
        }
        return false;
    }
}
