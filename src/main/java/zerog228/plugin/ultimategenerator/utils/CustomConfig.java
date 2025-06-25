package zerog228.plugin.ultimategenerator.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CustomConfig {

    private static File file;
    private static FileConfiguration customFile;

    private File getResource(){
        return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml")).toString());
    }

    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("UltimateGenerator").getDataFolder(), "config.yml");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (IOException e){}
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            System.out.println("Failed to save config file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
