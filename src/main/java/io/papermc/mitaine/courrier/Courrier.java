package io.papermc.mitaine.courrier;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Courrier implements CommandExecutor, Listener, TabCompleter {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) throws IOException, FileNotFoundException, IllegalArgumentException {
        Player player = join.getPlayer();
        int nbMsg = 0;
        File messages = new File("plugins/mitaine/messages.txt");
        try {
            Scanner sc = new Scanner(messages);
            while (sc.hasNextLine()) {
                if (player.getUniqueId().equals(UUID.fromString(sc.next()))) {
                    nbMsg++;
                }
                sc.nextLine();
            }
            player.sendMessage("[§6Mitaine§f] Bonjour, vous avez §c" + nbMsg + "§f message en attente.");
        } catch (FileNotFoundException e) {
            try {
                FileWriter file = new FileWriter("plugins/mitaine/messages.txt");
                file.close();
            } catch (IOException e1) {
                Bukkit.getLogger().info("Problème lors de la création du fichier de messages");
            }
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info("Problème lors de la lecture de messages");
        }
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
                    try { // Ah bon bah je vais le refaire en yaml
                        FileWriter courriers = new FileWriter("plugins/mitaine/messages.txt");
                        courriers.write(reciever + " " + sender.getName() + " " + message + "\n");
                        courriers.close();
                    } catch (IOException e) {
                        Bukkit.getLogger().info("Il y a eu une erreur dans la création du fichier messages.txt");
                    }
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
