package io.papermc.mitaine.courrier;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class Courrier implements CommandExecutor, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) throws FileNotFoundException {
        Player player = join.getPlayer();
        int nbMsg = 0;
        File messages = new File("plugins/mitaine/messages.txt");
        try {
            Scanner sc = new Scanner(messages);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
                if (sc.nextLine().contains(player.getUniqueId().toString())) {
                    nbMsg++;
                }
            }
            player.sendMessage("[§6Mitaine§f] Bonjour, vous avez §c" + nbMsg + "§f message en attente.");
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().info(player.getName() + "n'a pas de messages en attente");
        }
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
                    Écrire sur le message.yml les messages (ou txt parce que pas clair)
                    receiver -> UUID du mec qui reçoit le msg
                    sender -> player qui envoie le message (récup le nom)
                    message -> message à stocker
                    */
                    try {
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
            } else {
                sender.sendMessage("§cLa commande est /message <option>");
            }
        }
        return false;
    }
}
