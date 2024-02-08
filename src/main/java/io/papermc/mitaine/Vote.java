package io.papermc.mitaine;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class Vote implements CommandExecutor {

    private static int nb_choix = 0;
    private static ArrayList<Integer> votes = new ArrayList<>();
    private static ArrayList<UUID> votants = new ArrayList<>();
    private static NamespacedKey cleBar = new NamespacedKey("vote", "vote");
    private final static BossBar barVote = Bukkit.createBossBar(cleBar, "Un vote est en cours", BarColor.RED, BarStyle.SEGMENTED_10);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("creervote")) {
                if (args.length > 1) {
                    StringBuilder bc = new StringBuilder();
                    int i = 1;
                    votes.add(0);
                    for (String part : args) {
                        bc.append("- " + i + " pour " + part + "\n");
                        votes.add(0);
                        i++;
                    }
                    nb_choix = i - 1;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage("Un vote est en cours, faites §c/vote§f pour voter.\n" + bc);
                        barVote.addPlayer(p); // TODO Faire diminuer la bossbar
                    }
                    return true;
                } else {
                    player.sendMessage("§cLa commande est : /creervote <choix 1> ... <choix n>");
                }
            }

            if (cmd.getName().equalsIgnoreCase("resultats")) {
                if (nb_choix == 0) {
                    player.sendMessage("§cIl n'y a pas de vote pour le moment");
                } else {
                    StringBuilder res = new StringBuilder();
                    for (int i = 1; i <= nb_choix; i++) {
                        res.append("- §c" + votes.get(i) + "§f pour le vote " + i + "\n");
                    }
                    player.sendMessage("Le résultat du vote est :\n" + res);
                    nb_choix = 0;
                    votes.clear();
                    votants.clear();
                    barVote.removeAll();
                }
            }

            if (cmd.getName().equalsIgnoreCase("vote")) {

                if (args.length == 1 && !votants.contains(player.getUniqueId())) {
                    try {
                        if (nb_choix == 0) {
                            player.sendMessage("§cIl n'y a pas de vote en cours");
                        } else {
                            int choix = Integer.parseInt(args[0]);
                            if (choix <= 0 || choix > nb_choix) {
                                player.sendMessage("§cVous devez rentre un chiffre compris entre 1 et " + nb_choix);
                            } else {
                                votes.set(choix, votes.get(choix) + 1);
                                votants.add(player.getUniqueId());
                                player.sendMessage("Merci, vous avez voté : §c" + choix);
                            }
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cVous devez entrer un chiffre en paramètre");
                    }
                } else {
                    if (args.length != 1) {
                        player.sendMessage("§cLa commande est : /vote <choix>");
                    } else if (votants.contains(player.getUniqueId())) {
                        player.sendMessage("§cVous ne pouvez pas voter plusieurs fois avec le même compte");
                    }
                }
            }
        }
        return false;
    }
}
