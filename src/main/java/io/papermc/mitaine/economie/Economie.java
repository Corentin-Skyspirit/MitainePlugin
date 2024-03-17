package io.papermc.mitaine.economie;

import io.papermc.mitaine.MitaineMain;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Economie implements CommandExecutor {
    private final MitaineMain main;

    public Economie(MitaineMain mitaineMain) {
        this.main = mitaineMain;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID pId = player.getUniqueId();

            if (cmd.getName().equalsIgnoreCase("economie")) {

                if (args.length != 0) {

                    if (args[0].equalsIgnoreCase("banque")) {
                        player.sendMessage(main.getConfig().getString("titre") + " Vous avez " + main.getConfig().getString("important") + 64 + main.getConfig().getString("normal") + " diamants en banque.");
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("retirer")) {
                        if (args.length != 2) {
                            player.sendMessage(main.getConfig().getString("erreur") + "la commande est /economie retirer <chiffre>");
                        }
                        //faire le test des diamants dispos
                        else if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(main.getConfig().getString("erreur") + "vous n'avez plus de place dans votre inventaire");
                        } else {
                            try {
                                int nb = Integer.parseInt(args[1]); //pas clair qu'on sache si c'est un chiffre
                                player.getInventory().addItem(new ItemStack(Material.DIAMOND, nb));
                            } catch (NumberFormatException e) {
                                player.sendMessage(main.getConfig().getString("erreur") + "vous devez entrer un chiffre en paramètre");
                            }
                        }
                        return true;
                    }
                } else {
                    player.sendMessage(main.getConfig().getString("erreur") + " La commande est : /economie <option>");
                }
            }
        }
        return false;
    }
}
