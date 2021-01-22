package me.rezcom.pseudohardcore.commandhandler;

import me.rezcom.pseudohardcore.event.ReviveHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {

    // Handles the /revive command

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player){
            // Player sent command
            Player player = (Player) sender;
            if (args.length < 1){
                // Revive myself
                return attemptRevive(player,true);
            } else {
                // Revive others
                return true;
            }
        } else {
            // Console sent command
            if (args.length < 1){
                System.out.println("Please enter a player name!");
                return true;
            }

        }


        return true;
    }

    // Attempts to revive the specified player object.
    // If selfRevive is true, messages the player an error message if they cannot revive.
    public boolean attemptRevive(Player player, boolean selfRevive){
        if (ReviveHandler.canRevive(player)){
            ReviveHandler.revivePlayer(player);
        } else if (selfRevive){
            player.sendMessage("Cannot be revived yet!");
        }
        return true;
    }
}
