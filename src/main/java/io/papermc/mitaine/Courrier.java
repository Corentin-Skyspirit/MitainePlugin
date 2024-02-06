package io.papermc.mitaine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Courrier implements CommandExecutor, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        player.sendMessage("Bonjour, vous avez §c" + 0 + "§f message en attente.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        return false;
    }
}
