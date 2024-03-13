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
        player.sendMessage("Vous avez " + config.getString("important") + config.getString(player.getUniqueId() + ".courriers.nombre") + config.getString("normal") + " courriers en attente");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("courrier")) {
            FileConfiguration config = main.getConfig();
            if (args.length != 0) {

                if (args[0].equalsIgnoreCase("envoyer")) {
                    if (args.length >= 3) {
                        StringBuilder contenu = new StringBuilder();
                        UUID reciever = Bukkit.getPlayerUniqueId(args[1]);
                        if (reciever == null) {
                            sender.sendMessage(config.getString("erreur") + " Le joueur renseigné n'existe pas");
                            return false;
                        }
                        for (int i = 2; i < args.length; i++) {
                            contenu.append(args[i]).append(" ");
                        }
                        String nameSender;
                        UUID idSender = null;
                        if (sender instanceof Player player) {
                            nameSender = player.getName();
                            idSender = player.getUniqueId();
                        } else {
                            nameSender = config.getString("titre") + config.getString("important") + " Admin";
                        }
                        String nombre = config.getString(reciever + ".courriers.nombre");
                        int nbMsg = 1;
                        if (nombre != null) {
                            nbMsg += Integer.parseInt(nombre);
                        }
                        config.set(reciever + ".courriers.nombre", nbMsg);
                        config.set(reciever + ".courriers." + nbMsg + ".date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy à HH:mm:ss")));
                        config.set(reciever + ".courriers." + nbMsg + ".sender", nameSender);
                        assert idSender != null;
                        config.set(reciever + ".courriers." + nbMsg + ".idSender", idSender.toString());
                        config.set(reciever + ".courriers." + nbMsg + ".message", contenu.toString());
                        main.saveConfig();
                        try {
                            Objects.requireNonNull(Bukkit.getPlayer(reciever)).sendMessage(config.getString("titre") + " Vous avez reçu un message !");
                            sender.sendMessage("Le courrier a bien été envoyé à " + config.getString("important") + args[1]);
                        } catch (NullPointerException e) {
                            sender.sendMessage("Le joueur n'est pas en ligne, il verra le message à la prochaine connexion");
                        }
                    } else {
                        sender.sendMessage(config.getString("erreur") + "La commande est /courrier envoyer <destinataire> <message>");
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("liste")) {
                    if (sender instanceof Player player) {
                        if (args.length == 1) {
                            UUID idPlayer = player.getUniqueId();
                            int nbMsg = config.getInt(idPlayer + ".courriers.nombre");
                            player.sendMessage("Vous avez " + config.getString("important") + config.getString(idPlayer + ".courriers.nombre") + config.getString("normal") + " courriers en attente");
                            for (int i = 1; i <= nbMsg; i++) {
                                player.sendMessage(config.getString("discret") + i + config.getString("normal") + " - Reçu le " + config.getString("discret") + config.getString(idPlayer + ".courriers." + i + ".date") + config.getString("normal") + " par " + config.getString("important") + config.getString(idPlayer + ".courriers." + i + ".sender"));
                                // Faire un truc cliquable pour ouvrir le message ?
                            }
                        } else {
                            player.sendMessage(config.getString("erreur") + "La commande est /courrier liste");
                        }
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("lire")) {
                    if (sender instanceof Player player) {
                        int nbMsg = config.getInt(player.getUniqueId() + ".courriers.nombre");
                        int entree = 0;
                        try {
                            entree = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            player.sendMessage(config.getString("erreur") + "La commande est /courrier lire <nombre>");
                        }
                        if (args.length == 2 && entree <= nbMsg && entree > 0) {
                            String enTete = player.getUniqueId() + ".courriers." + args[1];
                            player.sendMessage(config.getString("discret") + config.getString(enTete + ".date") + config.getString("normal") + " - " + config.getString("important") + config.getString(enTete + ".sender"));
                            player.sendMessage(Objects.requireNonNull(config.getString(enTete + ".message")));
                            // Faire un truc cliquable pour répondre / supprimer
                        } else {
                            player.sendMessage(config.getString("erreur") + "La commande est /courrier lire <nombre>");
                        }
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("supprimer")) {
                    if (sender instanceof Player player) {
                        int nbMsg = config.getInt(player.getUniqueId() + ".courriers.nombre");
                        int entree = 0;
                        try {
                            entree = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            player.sendMessage(config.getString("erreur") + "La commande est /courrier supprimer <nombre>");
                        }
                        if (args.length == 2 && entree <= nbMsg && entree > 0) {
                            for (int i = entree; i <= nbMsg; i++) {
                                config.set(player.getUniqueId() + ".courriers." + i + ".date", config.getString(player.getUniqueId() + ".courriers." + (i + 1) + ".date"));
                                config.set(player.getUniqueId() + ".courriers." + i + ".sender", config.getString(player.getUniqueId() + ".courriers." + (i + 1) + ".sender"));
                                config.set(player.getUniqueId() + ".courriers." + i + ".idSender", config.getString(player.getUniqueId() + ".courriers." + (i + 1) + ".idSender"));
                                config.set(player.getUniqueId() + ".courriers." + i + ".message", config.getString(player.getUniqueId() + ".courriers." + (i + 1) + ".message"));
                            }
                            config.set(player.getUniqueId() + ".courriers." + nbMsg, null);
                            config.set(player.getUniqueId() + ".courriers.nombre", nbMsg - 1);
                            main.saveConfig();
                            player.sendMessage("Message Supprimé");
                            // Faire que le message supprimé réarange la liste
                            // Afficher la liste à chaque fois
                            player.performCommand("courrier liste");
                        } else {
                            player.sendMessage(config.getString("erreur") + "La commande est /courrier supprimer <nombre>");
                        }
                    }
                    return true;
                }

            } else {
                sender.sendMessage(config.getString("erreur") + "La commande est /courrier <option>");
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
