package me.rezcom.pseudohardcore.ymldata;

import me.rezcom.pseudohardcore.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DeathTimeData {

    // Handles all config data concerning how long it takes to respawn.

    public static Map<String,Integer> deathMap = new HashMap<String, Integer>(){{
        put("seconds",0);
        put("minutes",0);
        put("hours",20);
        put("days",0);
        put("weeks",0);
    }};
    public static DataManager deathConfig;

    // Default to be used in case something goes wrong in config
    private static final Map<String,Integer> defaultDeathMap = new HashMap<String,Integer>(){{
        put("seconds",0);
        put("minutes",0);
        put("hours",20);
        put("days",0);
        put("weeks",0);
    }};

    // Write to deathtime.yml
    /*public static void saveConfig(){
        proofreadConfig();
    }*/

    // Reads the deathtime.yml and puts info in the death map.
    public static void readConfig(){
        // If there's a problem, just uses the default death map values instead.
        try {
            Map<String,Object> oldMap = Objects.requireNonNull(deathConfig.getConfig().getConfigurationSection("default")).getValues(false);

            deathMap.put("seconds",(Integer)oldMap.get("seconds"));
            deathMap.put("minutes",(Integer)oldMap.get("minutes"));
            deathMap.put("hours",(Integer)oldMap.get("hours"));
            deathMap.put("days",(Integer)oldMap.get("days"));
            deathMap.put("weeks",(Integer)oldMap.get("weeks"));

            proofreadConfig();

        } catch (NullPointerException e){
            System.out.println("[PseudoHardcoreMC] ERROR: Incorrect format: Delete your deathtime.yml and reboot the plugin!");
        }
    }

    // Ensures that every entry in the death map exists or is a positive value.
    private static void proofreadConfig(){
        // If the criteria isn't met for a value, uses the default death map's value.
        for (Map.Entry<String, Integer> entry : deathMap.entrySet()){
            if (entry.getValue() == null || entry.getValue() < 0){
                deathMap.put(entry.getKey(),defaultDeathMap.get(entry.getKey()));
            }
        }
    }

}
