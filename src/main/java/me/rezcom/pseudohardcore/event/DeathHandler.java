package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.Main;
import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DeathHandler implements Listener {

    // Whenever a player dies

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event){

        World world = event.getPlayer().getWorld();
        if (RespawnData.respawnMap.containsKey(event.getPlayer().getUniqueId())){
            TextComponent deathMsg = Component.text("");
            event.deathMessage(deathMsg);
            return;
        }

        if (world.isHardcore() && !RespawnData.respawnMap.containsKey(event.getPlayer().getUniqueId())){
            // Actually died lmfao
            long deathTime = System.currentTimeMillis();
            //Main.logger.log(Level.INFO,event.getPlayer().getName() + " died at SystemTime: " + deathTime);

            long respawnTime = calculateDeathTime(deathTime); // Calculate respawn date

            // Add the Respawn Time to the RespawnMap
            RespawnData.respawnMap.put(event.getEntity().getUniqueId(),respawnTime);
            RespawnData.saveRespawns();

            //Main.logger.log(Level.INFO, "Player Gamemode: " + event.getPlayer().getGameMode());

        }

    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event){
        //Main.logger.log(Level.INFO,event.getPlayer().getName() + " hit the Respawn Button!");
        Player player = event.getPlayer();
        //Main.logger.log(Level.INFO, "World hardcore status: " + player.getWorld().isHardcore());
        if (!player.getWorld().isHardcore()){
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (!ReviveHandler.canRevive(player) && player.getWorld().isHardcore()){
            //Main.logger.log(Level.INFO,"Should be spectator.");
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
