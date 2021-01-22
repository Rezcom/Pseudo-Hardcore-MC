package me.rezcom.pseudohardcore.commandhandler;

import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PHCCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        // All commands starting with /phc
        if (!(cmd.getName().equalsIgnoreCase("phc"))){
            System.out.println("[PseudoHardcoreMC] You should never see this. Something went horribly wrong. (Non-PHC Command received by PHC Handler)");
            return true;
        }

        if (args.length == 0){
            return false;
        }

        if (args[0].equalsIgnoreCase("respawns")){
            return RespawnsCommand.showRespawns(sender);
        }

        if (args[0].equalsIgnoreCase("clear")){
            return clearRespawns(sender);
        }

        if (args[0].equalsIgnoreCase("rtime")){
            return showRespawnTime(sender);
        }

        return false;
    }




    // Clears all respawn data in the respawn map, and updates the respawns.yml accordingly.
    private boolean clearRespawns(CommandSender sender){

        if ((sender instanceof Player) && (!sender.isOp())){
            sender.sendMessage("§cYou don't have permission to do this");
            return true;
        }
        RespawnData.respawnMap.clear();
        RespawnData.saveRespawns();
        sender.sendMessage("Successfully cleared all Respawn Data");
        return true;
    }

    // Prints out to the command sender the current amount of time it takes to respawn.
    private boolean showRespawnTime(CommandSender sender){

        String seconds = "";
        String minutes = "";
        String hours = DeathTimeData.deathMap.get("hours") + " hours.";
        String days = "";
        String weeks = "";
        if (DeathTimeData.deathMap.get("seconds") > 0){
            hours = DeathTimeData.deathMap.get("hours") + " hours, ";
            seconds = DeathTimeData.deathMap.get("seconds") + " seconds.";
        }
        if (DeathTimeData.deathMap.get("minutes") > 0){
            hours = DeathTimeData.deathMap.get("hours") + " hours, ";
            if (DeathTimeData.deathMap.get("seconds") > 0){
                minutes = DeathTimeData.deathMap.get("minutes") + " minutes, ";
            } else {
                minutes = DeathTimeData.deathMap.get("minutes") + " minutes.";
            }

        }
        if (DeathTimeData.deathMap.get("days") > 0){
            days = DeathTimeData.deathMap.get("days") + " days, ";
        }
        if (DeathTimeData.deathMap.get("weeks") > 0){
            weeks = DeathTimeData.deathMap.get("weeks") + " weeks, ";
        }
        sender.sendMessage("Respawn Time after Death: " + weeks + days + hours + minutes + seconds);

        return true;
    }

}
