package zerog228.plugin.ultimategenerator;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import zerog228.plugin.ultimategenerator.commands.ReloadConfigCommand;
import zerog228.plugin.ultimategenerator.generator.CustomBiomeProvider;
import zerog228.plugin.ultimategenerator.generator.CustomChunkGenerator;
import zerog228.plugin.ultimategenerator.generator.CustomChunkPopulator;
import zerog228.plugin.ultimategenerator.listeners.Listeners;
import zerog228.plugin.ultimategenerator.utils.ConfigData;
import zerog228.plugin.ultimategenerator.utils.CustomConfig;
import zerog228.plugin.ultimategenerator.utils.DifferentMaps;
import zerog228.plugin.ultimategenerator.utils.GeneratorUtils;

import javax.annotation.Nullable;

public final class UltimateGenerator extends JavaPlugin {

    private static UltimateGenerator plugin;
    public static WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin worldEdit){
            return worldEdit;
        } else {
            return null;
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        DifferentMaps.put();
        plugin = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("ultimate_config_reload").setExecutor(new ReloadConfigCommand());

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        CustomConfig.setup();
        CustomConfig.get().addDefault("MIN_Y", 61);
        CustomConfig.get().addDefault("MAX_Y", 320);
        CustomConfig.get().addDefault("WATER_LEVEL", 60);
        CustomConfig.get().addDefault("COLD_OCEAN_NUMBER", -0.4);
        CustomConfig.get().addDefault("WARM_OCEAN_NUMBER", 0.6);
        CustomConfig.get().addDefault("SEED", 25566);
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();

        ConfigData.getSpecializationsList();
        GeneratorUtils.tuneGenerators();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin(){
        return plugin;
    }
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomChunkGenerator(); // Return an instance of the chunk generator we want to use.
    }

    @Override
    public @Nullable
    BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        return new CustomBiomeProvider();
    }
}
