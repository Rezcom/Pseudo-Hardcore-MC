package me.rezcom.pseudohardcore.commandhandler;

import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

public class RespawnsCommand implements CommandExecutor {
    //final private static String[] respawnColors = new String[]{"§e","§2","§3","§1","§5"};

    // For the command /respawns

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        return showRespawns(sender);
    }

    // Prints all players' current respawn times relative to now.
    public static boolean showRespawns(CommandSender sender){
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        Instant rTime;
        //int colorCounter = 0;
        sender.sendMessage("§c§n[Respawn Times]");
        if (RespawnData.respawnMap.isEmpty()){
            sender.sendMessage("No respawns to show.");
            return true;
        }
        for (Map.Entry<UUID, Long> entry : RespawnData.respawnMap.entrySet()){

            rTime = Instant.ofEpochMilli(entry.getValue());
            sender.sendMessage(buildRespawnString(now,rTime, Bukkit.getOfflinePlayer(entry.getKey()).getName()));

            //colorCounter++;
        }
        return true;
    }
    // Builds the string that will be sent to the command sender.
    private static String buildRespawnString(Instant now, Instant rTime, String playerName){
        String color;
        String begin;
        String ago;
        String hours = "";
        if (now.isAfter(rTime)){
            // If now is ahead of respawn time, they should already be respawned.
            begin = "Can respawn as of";
            ago = " ago.";
            color = "§a";
        } else{
            // Respawn is still yet to happen.
            begin = "Respawns in";
            ago = ".";
            color = "§e";
        }

        long timeDiffHours = now.until(rTime, ChronoUnit.HOURS);
        long timeDiffMinutes = now.until(rTime, ChronoUnit.MINUTES) - (60 * timeDiffHours);
        if (java.lang.Math.abs(timeDiffHours) > 0){
            hours = " " + java.lang.Math.abs(timeDiffHours) + " Hours,";
        }
        String minutes = " " + java.lang.Math.abs(timeDiffMinutes) + " Minutes";

        return color + playerName + ": " + begin + hours + minutes + ago;
    }

}
