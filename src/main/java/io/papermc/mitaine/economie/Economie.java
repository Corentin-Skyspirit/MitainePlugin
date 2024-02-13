package io.papermc.mitaine.economie;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class Economie implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("economie")) {
                if (args[0].equalsIgnoreCase("banque")) {
                    player.sendMessage("[§6Mitaine§f] Vous avez §c" + 64 + "§f diamants en banque.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("retirer")) {
                    if (args.length != 2) {
                        player.sendMessage("§cLa commande est /economie retirer <chiffre>");
                    }
                    //faire le test des diamants dispos
                    else if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage("§cVous n'avez plus de place dans votre inventaire");
                    } else {
                        try {
                            int nb = Integer.parseInt(args[1]); //pas clair qu'on sache si c'est un chiffre
                            player.getInventory().addItem(new ItemStack(Material.DIAMOND, nb));
                            return true;
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cVous devez entrer un chiffre en paramètre");
                        }
                    }
                } else {
                    player.sendMessage("§c La commande est : /economie <banque, retier, deposer, donner>");
                }
            }
        }
        return false;
    }
}
