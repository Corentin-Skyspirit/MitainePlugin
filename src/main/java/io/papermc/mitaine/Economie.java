package io.papermc.mitaine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Economie implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("banque")) {
                player.sendMessage("Vous avez §c" + 64 + "§f diamants en banque.");
                return true;
            }
        }

        return false;
    }
}
