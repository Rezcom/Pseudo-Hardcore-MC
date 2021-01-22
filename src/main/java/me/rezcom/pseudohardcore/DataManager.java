package me.rezcom.pseudohardcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    final private Main plugin;
    final private String filename;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin, String filename){
        this.filename = filename;
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if (this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(), this.filename);
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(this.filename);
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if (this.dataConfig == null){
            reloadConfig();
        }
        return this.dataConfig;
    }

    public void saveConfig(){
        if (this.dataConfig == null || this.configFile == null){
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e){
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }

    }
    public void saveDefaultConfig(){
        if (this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(), this.filename);
        }

        if (!this.configFile.exists()){
            this.plugin.saveResource(this.filename,false);
        }

    }
}
