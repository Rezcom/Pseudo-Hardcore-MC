package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.Main;
import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DeathHandler implements Listener {

    // Whenever a player dies

    private static Map<UUID,Location> playerDeathLoc = new HashMap<>();

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event){

        Player player = event.getPlayer();

        World world = player.getWorld();

        UUID uuid = player.getUniqueId();

        playerDeathLoc.put(uuid,player.getLocation());
        /*if (RespawnData.locationMap.containsKey(uuid)){

            Location locationMapSpawn = RespawnData.locationMap.get(uuid);
            if (player.getWorld() == locationMapSpawn.getWorld()){
                player.setBedSpawnLocation(locationMapSpawn);
            }

        }*/

        if (world.isHardcore() && !RespawnData.respawnMap.containsKey(player.getUniqueId())){
            // Actually died lmfao
            long deathTime = System.currentTimeMillis();


            long respawnTime = calculateDeathTime(deathTime); // Calculate respawn date

            // Add the Respawn Time to the RespawnMap
            RespawnData.respawnMap.put(event.getEntity().getUniqueId(),respawnTime);
            RespawnData.saveRespawns();



        }

    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event){

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!ReviveHandler.canRevive(player) && player.getWorld().isHardcore()){
            //Main.logger.log(Level.INFO,"Player can't revive.");
            event.setRespawnLocation(playerDeathLoc.get(uuid));
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    // Calculate how long a player should be dead from now, according to the death map.
    long calculateDeathTime(long curDeathTime){

        long seconds = 1000 * (long)DeathTimeData.deathMap.get("seconds");
        long minutes = 60000 * (long)DeathTimeData.deathMap.get("minutes");
        long hours = 3600000 * (long)DeathTimeData.deathMap.get("hours");
        long days = 86400000 * (long)DeathTimeData.deathMap.get("days");
        long weeks = 604800000 * (long)DeathTimeData.deathMap.get("weeks");
        return curDeathTime + seconds + minutes + hours + days + weeks;
    }
}
