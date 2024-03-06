package io.papermc.mitaine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Teleport implements CommandExecutor {
    private final MitaineMain main;

    public Teleport(MitaineMain mitaineMain) {
        this.main = mitaineMain;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("spawn")) {
                return true;
            }
        }
        return false;
    }
}
