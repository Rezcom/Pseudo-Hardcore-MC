package me.rezcom.pseudohardcore.ymldata;

import me.rezcom.pseudohardcore.DataManager;
import me.rezcom.pseudohardcore.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class RespawnData {

    // Handles all data concerning who's still yet to respawn.

    public static Map<UUID,Long> respawnMap = new HashMap<>();
    public static DataManager respawnConfig; // Handles writing/reading from respawns.yml

    // Writes down current respawns in the respawns.yml file
    public static void saveRespawns(){
        // Delete old data
        respawnConfig.getConfig().set("data",null);

        // Update New Data
        for (Map.Entry<UUID, Long> entry : respawnMap.entrySet()){
            respawnConfig.getConfig().set("data." + entry.getKey(), entry.getValue());
        }
        respawnConfig.saveConfig();
    }

    // Restores respawns from the respawns.yml file to the RespawnMap
    public static void restoreRespawns(){
        try {
            Map<String,Object> oldMap = Objects.requireNonNull(respawnConfig.getConfig().getConfigurationSection("data")).getValues(false);
            for (Map.Entry<String,Object> entry : oldMap.entrySet()){
                respawnMap.put(UUID.fromString(entry.getKey()),(long)entry.getValue());
            }
            Main.logger.log(Level.INFO,"Respawn times loaded.");
        } catch (NullPointerException e){
            respawnConfig.reloadConfig();
            Main.logger.log(Level.INFO,"No respawns to load.");

        }

    }



}
