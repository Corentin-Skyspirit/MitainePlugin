package io.papermc.mitaine;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class Economie implements CommandExecutor, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("economie")) {
                if (args[0].equalsIgnoreCase("banque")) {
                    player.sendMessage("Vous avez §c" + 64 + "§f diamants en banque.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("retirer")) {
                    if (args.length == 2 && player.getInventory().firstEmpty() != -1) { //faire le test des diamants dispos
                        int nb = Integer.parseInt(args[0]);
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND, nb));
                        return true;
                    }
                }
            } else {
                player.sendMessage("§c La commande est : /economie <option>");
            }
        }
        return false;
    }
}
