package io.papermc.mitaine.vote;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Vote implements CommandExecutor, Listener {

    private static int nb_choix = 0;
    private static ArrayList<Integer> votes = new ArrayList<>();
    private static ArrayList<UUID> votants = new ArrayList<>();
    private static NamespacedKey cleBar = new NamespacedKey("vote", "vote");
    private final static BossBar barVote = Bukkit.createBossBar(cleBar, "Un vote est en cours faites §c/vote", BarColor.RED, BarStyle.SEGMENTED_20);
    private static String bc = "";

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        if (nb_choix != 0) {
            player.sendMessage("[§6Mitaine§f] Un vote est en cours, faites §c/vote§f pour voter.\n" + bc);
            barVote.addPlayer(player);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("creervote")) {
                if (args.length > 1) {
                    votes.add(0);
                    for (String choix : args) {
                        nb_choix++;
                        bc += "- §c" + nb_choix + "§f pour " + choix + "\n";
                        votes.add(0);
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage("[§6Mitaine§f] Un vote est en cours, faites §c/vote§f pour voter.\n" + bc);
                        barVote.addPlayer(p);
                    }
                    barVote.setProgress(1.0);
                    return true;
                } else {
                    player.sendMessage("§cLa commande est : /creervote <choix 1> ... <choix n>");
                }
            }

            if (cmd.getName().equalsIgnoreCase("vote")) {

                if (args.length == 1 && !votants.contains(player.getUniqueId())) {
                    if (nb_choix == 0) {
                        player.sendMessage("[§6Mitaine§f] Il n'y a pas de vote en cours");
                    } else {
                        try {
                            int choix = Integer.parseInt(args[0]);
                            if (choix <= 0 || choix > nb_choix) {
                                player.sendMessage("§cVous devez rentre un chiffre compris entre 1 et " + nb_choix);
                            } else {
                                votes.set(choix, votes.get(choix) + 1);
                                votants.add(player.getUniqueId());
                                player.sendMessage("[§6Mitaine§f] Merci, vous avez voté : §c" + choix);
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cVous devez entrer un chiffre en paramètre");
                        }
                    }
                } else {
                    if (args.length != 1) {
                        player.sendMessage("§cLa commande est : /vote <choix>");
                    } else if (votants.contains(player.getUniqueId())) {
                        player.sendMessage("[§6Mitaine§f] Vous ne pouvez pas voter plusieurs fois avec le même compte");
                    }
                }
            }

            if (cmd.getName().equalsIgnoreCase("resultats")) {
                if (nb_choix == 0) {
                    player.sendMessage("[§6Mitaine§f] Il n'y a pas de vote pour le moment");
                } else {
                    StringBuilder res = new StringBuilder();
                    for (int i = 1; i <= nb_choix; i++) {
                        res.append("- §c" + votes.get(i) + "§f pour le vote " + i + "\n");
                    }
                    player.sendMessage("[§6Mitaine§f] Le résultat du vote est :\n" + res);
                    int idMax = 0;
                    int sum = 0;
                    for (int v = 0; v < votes.size(); v++) {
                        sum += votes.get(v);
                        if (votes.get(v) > votes.get(idMax)) {
                            idMax = v;
                        }
                    }
                    if (sum != 0) {
                        String message = "[§6Mitaine§f] Le choix §cn°" + idMax + "§f remporte le vote avec §c" + (double) 100 * votes.get(idMax) / sum + "%§f des voix";
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(message);
                        }
                    } else {
                        player.sendMessage("§c Il n'y a eu aucun vote");
                    }
                    nb_choix = 0;
                    votes.clear();
                    votants.clear();
                    barVote.removeAll();
                    bc = "";
                }
            }
        }
        return false;
    }
}
