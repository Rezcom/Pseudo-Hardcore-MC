package me.rezcom.pseudohardcore;

// import org.bukkit.World;
import me.rezcom.pseudohardcore.commandhandler.PHCCommandHandler;
import me.rezcom.pseudohardcore.commandhandler.RespawnsCommand;
import me.rezcom.pseudohardcore.commandhandler.ReviveCommand;
import me.rezcom.pseudohardcore.commandhandler.ToggleHardcoreCommand;
import me.rezcom.pseudohardcore.event.DeathHandler;
import me.rezcom.pseudohardcore.event.HomewardBoneHandler;
import me.rezcom.pseudohardcore.event.ReviveHandler;
import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Logger logger;
    public static Plugin thisPlugin;

    @Override
    public void onEnable() {

        thisPlugin = this;

        // Initialize the plugin's logger.
        logger = this.getLogger();
        logger.log(Level.INFO,"Initializing Plugin");

        // Check if plugin folder exists; creates new one
        if (!getDataFolder().exists()) {
            boolean mkdirSuccess = getDataFolder().mkdir();
            if (!mkdirSuccess){
                logger.log(Level.WARNING,"COULDN'T MAKE NEW PLUGIN DIRECTORY. Is there a conflict? Plugin will now be disabled.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //this.RespawnFileManager = new DataManager(this, "respawns.yml");
        RespawnData.respawnConfig = new DataManager(this, "respawns.yml");
        DeathTimeData.deathConfig = new DataManager(this, "deathtime.yml");

        RespawnData.restoreRespawns();
        HomewardBoneHandler.initializeHomewardBone();

        DeathTimeData.readConfig();
        logger.log(Level.INFO,"DataManagers initialized.");

        // Events
        //getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        getServer().getPluginManager().registerEvents(new ReviveHandler(), this);
        getServer().getPluginManager().registerEvents(new HomewardBoneHandler(),this);

        logger.log(Level.INFO,"DataManagers initialized");

        // Commands
        try {
            Objects.requireNonNull(getCommand("phc")).setExecutor(new PHCCommandHandler());
            Objects.requireNonNull(getCommand("revive")).setExecutor(new ReviveCommand());
            Objects.requireNonNull(getCommand("respawns")).setExecutor(new RespawnsCommand());
            Objects.requireNonNull(getCommand("togglehardcore")).setExecutor(new ToggleHardcoreCommand());
            logger.log(Level.INFO,"Registered Commands.");
        } catch (NullPointerException e){
            logger.log(Level.SEVERE, "Couldn't set the executors for the commands! Were they included in the plugin.yml?");
        }


    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!RespawnData.respawnMap.isEmpty()){
            RespawnData.saveRespawns();
        }
    }

    public static void sendDebugMessage(String string, boolean send){
        if (send){
            logger.log(Level.INFO, string);
        }
    }

}
