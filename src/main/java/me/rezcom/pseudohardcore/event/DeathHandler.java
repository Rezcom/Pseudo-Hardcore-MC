package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathHandler implements Listener {

    // Whenever a player dies

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event){

        // Death in order to revive, see revivePlayer() in ReviveHandler
        if (ReviveHandler.canRevive(event.getEntity())){
            // Remove from RespawnMap
            event.setDeathMessage(event.getEntity().getDisplayName() + " has revived.");
            RespawnData.respawnMap.remove(event.getEntity().getUniqueId());
            RespawnData.saveRespawns();
            return;
        }

        // Actually died lmfao
        long deathTime = System.currentTimeMillis();
        System.out.println(event.getEntity().getDisplayName() + " died at SystemTime: " + deathTime);

        long respawnTime = calculateDeathTime(deathTime); // Calculate respawn date

        // Add the Respawn Time to the RespawnMap
        RespawnData.respawnMap.put(event.getEntity().getUniqueId(),respawnTime);
        RespawnData.saveRespawns();
    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event){
        System.out.println(event.getPlayer().getDisplayName() + " Respawned!");
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
