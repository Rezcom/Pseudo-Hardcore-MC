package me.rezcom.pseudohardcore.commandhandler;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleHardcoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args){
        if (!sender.hasPermission("PHC.togglehardcore") || (!(sender instanceof Player))){
            sender.sendMessage("Can only be used by players who have permission.");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        world.setHardcore(!world.isHardcore());
        player.sendMessage("Hardcore in " + world.getName() + " has been toggled to " + world.isHardcore());
        return true;
    }
}
