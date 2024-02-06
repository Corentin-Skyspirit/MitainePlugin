package io.papermc.mitaine;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("creervote")) {

                if (args.length <= 1) {
                    player.sendMessage("§c La commande est : /creervote <option1> <option2> <autres options>");
                }
                if (args.length >= 1) {
                    StringBuilder bc = new StringBuilder();
                    int i = 1;
                    for (String part : args) {
                        bc.append("- " + i + " pour " + part + "\n");
                        i++;
                    }
                    Bukkit.broadcastMessage("Un vote est en cours, faites §c/vote§f pour voter.\n" + bc);
                }
                return true;
            }
        }

        return false;
    }
}
