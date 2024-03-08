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
        String nombre = config.getString(player + ".courriers.nombre");
        if (nombre != null) {
            config.set(player + ".courriers.nombre", 0);
        }
        player.sendMessage("Vous avez " + main.getConfig().getString("important") + config.getString(player.getUniqueId() + ".courriers.nombre") + main.getConfig().getString("texte") + " courriers en attente");
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
                            sender.sendMessage(main.getConfig().getString("erreur") + " Le joueur renseigné n'existe pas");
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
                        String nameSender;
                        if (sender instanceof Player) {
                            nameSender = sender.getName();
                        } else {
                            nameSender = main.getConfig().getString("titre") + main.getConfig().getString("important") + " Admin";
                        }
                        FileConfiguration config = main.getConfig();
                        String nombre = config.getString(reciever + ".courriers.nombre");
                        int nbMsg = 1;
                        if (nombre != null) {
                            nbMsg += Integer.parseInt(nombre);
                        }
                        config.set(reciever + ".courriers.nombre", nbMsg);
                        Message message = new Message(nameSender, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy à HH:mm:ss")), contenu.toString());
                        config.set(reciever + ".courriers." + nbMsg + ".date", message.getDate());
                        config.set(reciever + ".courriers." + nbMsg + ".sender", message.getSender());
                        config.set(reciever + ".courriers." + nbMsg + ".message", message.getMessage());
                        main.saveConfig();
                        try {
                            Objects.requireNonNull(Bukkit.getPlayer(reciever)).sendMessage(main.getConfig().getString("titre") + " Vous avez reçu un message !");
                            sender.sendMessage("Le courrier a bien été envoyé à " + main.getConfig().getString("important") + args[1]);
                        } catch (NullPointerException e) {
                            sender.sendMessage("Le joueur n'est pas en ligne, il verra le message à la prochaine connexion");
                        }
                    } else {
                        sender.sendMessage(main.getConfig().getString("erreur") + "La commande est /courrier envoyer <destinataire> <message>");
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("liste")) {
                    if (sender instanceof Player player) {
                        if (args.length == 1) {
                            UUID idPlayer = player.getUniqueId();
                            FileConfiguration config = main.getConfig();
                            int nbMsg = config.getInt(idPlayer + ".courriers.nombre");
                            player.sendMessage("Vous avez " + main.getConfig().getString("important") + config.getString(idPlayer + ".courriers.nombre") + main.getConfig().getString("texte") + " courriers en attente");
                            for (int i = 1; i <= nbMsg; i++) {
                                player.sendMessage(i + " - Reçu le " + config.getString("discret") + config.getString(idPlayer + ".courriers." + i + ".date") + main.getConfig().getString("texte") + " par " + main.getConfig().getString("important") + config.getString(idPlayer + ".courriers." + i + ".sender"));
                                // Faire un truc cliquable pour ouvrir le message ?
                            }
                        } else {
                            player.sendMessage( main.getConfig().getString("erreur") + "La commande est /courrier liste");
                        }
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("lire")) {
                    if (sender instanceof Player player) {
                        if (args.length == 2) {
                            // Faire un truc cliquable pour répondre / supprimer
                        } else {
                            player.sendMessage(main.getConfig().getString("erreur") + "La commande est /courrier lire <nombre>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("supprimer")) {
                    if (sender instanceof Player player) {
                        if (args.length == 2) {
                            // Faire que le message supprimé réarange la liste
                            // Afficher la liste à chaque fois
                        } else {
                            player.sendMessage(main.getConfig().getString("erreur") + "La commande est /courrier supprimer <nombre>");
                        }
                    }
                    return true;
                }

            } else {
                sender.sendMessage(main.getConfig().getString("erreur") + "La commande est /courrier <option>");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            List<String> completions = new ArrayList<>();
            completions.add("liste");
            completions.add("envoyer");
            completions.add("lire");
            completions.add("supprimer");
            return completions;
        }
        return null;
    }
}
