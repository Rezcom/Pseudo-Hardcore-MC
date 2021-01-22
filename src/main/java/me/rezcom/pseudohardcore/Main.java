package me.rezcom.pseudohardcore;

// import org.bukkit.World;
import me.rezcom.pseudohardcore.commandhandler.PHCCommandHandler;
import me.rezcom.pseudohardcore.commandhandler.RespawnsCommand;
import me.rezcom.pseudohardcore.commandhandler.ReviveCommand;
import me.rezcom.pseudohardcore.event.DeathHandler;
import me.rezcom.pseudohardcore.event.ReviveHandler;
import me.rezcom.pseudohardcore.ymldata.DeathTimeData;
import me.rezcom.pseudohardcore.ymldata.RespawnData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Check if server is Hardcore
        if (!getServer().isHardcore()){
            System.out.println("[PseudoHardcoreMC] WARNING: Hardcore is set to false in your server.properties file! Set to true to enable the plugin! Plugin will now be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Check if plugin folder exists; creates new one
        if (!getDataFolder().exists()) {
            boolean mkdirSuccess = getDataFolder().mkdir();
            if (!mkdirSuccess){
                System.out.println("[PseudoHardcoreMC] COULDN'T MAKE NEW PLUGIN DIRECTORY. Is there a conflict? Plugin will now be disabled.");
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
        DeathTimeData.readConfig();
        System.out.println("[PseudoHardcoreMC] DataManagers initialized");

        // Events
        //getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        getServer().getPluginManager().registerEvents(new ReviveHandler(), this);
        System.out.println("[PseudoHardcoreMC] Registered Events");

        // Commands
        try {
            Objects.requireNonNull(getCommand("phc")).setExecutor(new PHCCommandHandler());
            Objects.requireNonNull(getCommand("revive")).setExecutor(new ReviveCommand());
            Objects.requireNonNull(getCommand("respawns")).setExecutor(new RespawnsCommand());
            System.out.println("[PseudoHardcoreMC] Registered Commands");
        } catch (NullPointerException e){
            this.getLogger().log(Level.SEVERE, "Couldn't set the executors for the commands! Were they included in the plugin.yml?");
        }


    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!RespawnData.respawnMap.isEmpty()){
            RespawnData.saveRespawns();
        }
    }
}
