package me.rezcom.pseudohardcore.event;


import me.rezcom.pseudohardcore.ymldata.RespawnData;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;

import java.time.Instant;

public class ReviveHandler implements Listener {


    // Handles whenever a player logs in.
    // Should respawn the player in Survival mode, and remove them from the
    // respawn list if their respawn time has passed.

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){

        // Attempt to revive player
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL){
            RespawnData.respawnMap.remove(player.getUniqueId());
            RespawnData.saveRespawns();
        }
        if (canRevive(player)){
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
        } else return player.getGameMode() == GameMode.SPECTATOR;

    }

    // Revives the player from being dead.
    public static void revivePlayer(Player player){

        // Broadcast they revived
        TextComponent reviveText = Component.text(player.getName() + " has revived.").color(TextColor.color(0x66c1fa));
        Bukkit.broadcast(reviveText);

        // Remove from list of respawning players
        RespawnData.respawnMap.remove(player.getUniqueId());
        RespawnData.saveRespawns();

        Location bedspawn = player.getBedSpawnLocation();
        if (bedspawn != null){
            player.teleport(bedspawn);
        } else {
            player.teleport(player.getWorld().getSpawnLocation());
        }

        player.setGameMode(GameMode.SURVIVAL);
    }

}

