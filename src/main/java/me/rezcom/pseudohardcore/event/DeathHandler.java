package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.Main;
import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.logging.Level;

public class DeathHandler implements Listener {

    // Whenever a player dies

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event){

        // Death in order to revive, see revivePlayer() in ReviveHandler
        if (ReviveHandler.canRevive(event.getEntity())){
            // Remove from RespawnMap
            event.deathMessage(Component.text(event.getPlayer().getName() + " has revived.").color(TextColor.color(0x9deb7c)));
            RespawnData.respawnMap.remove(event.getEntity().getUniqueId());
            RespawnData.saveRespawns();
            return;
        }

        // Actually died lmfao
        long deathTime = System.currentTimeMillis();
        Main.logger.log(Level.INFO,event.getPlayer().getName() + " died at SystemTime: " + deathTime);

        long respawnTime = calculateDeathTime(deathTime); // Calculate respawn date

        // Add the Respawn Time to the RespawnMap
        RespawnData.respawnMap.put(event.getEntity().getUniqueId(),respawnTime);
        RespawnData.saveRespawns();
    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event){
        Main.logger.log(Level.INFO,event.getPlayer().getName() + " Respawned!");

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
