package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;

public class ReviveHandler implements Listener {

    // Handles whenever a player logs in.
    // Should respawn the player in Survival mode, and remove them from the
    // respawn list if their respawn time has passed.

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){

        // Attempt to revive player
        if (canRevive(event.getPlayer())){
            revivePlayer(event.getPlayer());
        }

        //UUID playerUUID = event.getPlayer().getUniqueId();
        //Location respawnLocation =
    }


    // Returns true if their respawn time has passed, or if
    // they aren't in the respawn map at all.
    public static boolean canRevive(Player player){
        // If Player isn't in Survival and isn't in the RespawnMap, they can revive
        if (RespawnData.respawnMap.containsKey(player.getUniqueId())) {
            // Player is in RespawnMap
            Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
            Instant rTime = Instant.ofEpochMilli(RespawnData.respawnMap.get(player.getUniqueId()));

            return now.isAfter(rTime);
        } else return player.getGameMode() != GameMode.SURVIVAL;

    }

    // Revives the player from being dead.
    public static void revivePlayer(Player player){
        player.setHealth(0.0D);
        player.spigot().respawn();

        player.setGameMode(GameMode.SURVIVAL);
    }

}

