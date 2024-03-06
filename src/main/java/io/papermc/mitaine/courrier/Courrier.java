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
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Courrier implements CommandExecutor, Listener, TabCompleter {
    private final MitaineMain main;

    public Courrier(MitaineMain mitaineMain) {
        this.main = mitaineMain;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        main.reloadConfig();
        FileConfiguration config = main.getConfig();
        player.sendMessage("Vous avez §c" + config.getString(player.getUniqueId() + ".courriers.nombre") + "§f courriers en attente");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("courrier")) {

            if (args.length != 0) {

                if (args[0].equalsIgnoreCase("envoyer")) {
                    if (args.length >= 3) {
                        StringBuilder contenu = new StringBuilder();
                        UUID reciever = Bukkit.getPlayerUniqueId(args[1]);
                        if (reciever == null) {
                            sender.sendMessage("§c Le joueur renseigné n'existe pas");
                            return false;
                        }
                        for (int i = 2; i < args.length; i++) {
                            contenu.append(args[i]).append(" ");
                        }
                        /*
                        Écrire sur le message.yml les messages (ou txt parce que pas clair)
                        receiver -> UUID du mec qui reçoit le msg
                        sender -> player qui envoie le message (récup le nom)
                        message -> message à stocker
                        */
                        String idSender;
                        if (sender instanceof Player) {
                            idSender = ((Player) sender).getUniqueId().toString();
                        } else {
                            idSender = "[§6Mitaine§f] §cNouvelle Information !§f";
                        }
                        String nombre = main.getConfig().getString(reciever + ".courriers.nombre");
                        int nbMsg = 1;
                        if (nombre != null) {
                            nbMsg += Integer.parseInt(nombre);
                        }
                        main.getConfig().set(reciever + ".courriers.nombre", nbMsg);
                        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                        Message message = new Message(idSender, date, contenu.toString());
                        main.getConfig().set(reciever + ".courriers." + date + ".date", message.getDate());
                        main.getConfig().set(reciever + ".courriers." + date + ".sender", message.getSender());
                        main.getConfig().set(reciever + ".courriers." + date + ".message", message.getMessage());
                        main.saveConfig();
                        // Objects.requireNonNull(Bukkit.getPlayer(reciever)).sendMessage("[§6Mitaine§f] Vous avez reçu un message !"); --> pour l'instant produit une NullPointerException
                    } else {
                        sender.sendMessage("§cLa commande est /courrier envoyer <destinataire> <message>");
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("lire")) {
                    if (args.length >= 3) {

                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("supprimer")) {
                    if (args.length >= 3) {

                    }
                    return true;
                }

            } else {
                sender.sendMessage("§cLa commande est /courrier <option>");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
