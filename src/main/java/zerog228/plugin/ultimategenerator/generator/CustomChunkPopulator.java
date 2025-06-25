package zerog228.plugin.ultimategenerator.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import zerog228.plugin.ultimategenerator.UltimateGenerator;
import zerog228.plugin.ultimategenerator.utils.ConfigData;
import zerog228.plugin.ultimategenerator.utils.CustomConfig;
import zerog228.plugin.ultimategenerator.utils.GeneratorUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CustomChunkPopulator extends BlockPopulator {

    ConfigurationSection section = CustomConfig.get().getConfigurationSection("");
    private final int WATER_LEVEL = section.getInt("WATER_LEVEL");

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        for(int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = WATER_LEVEL + 1; y < worldInfo.getMaxHeight(); y++) {

                    Pair<Double, Double> ground_temperature = GeneratorUtils.getGroundLevelAndTemp(x + (chunkX * 16), y, z + (chunkZ * 16));
                    double ground_level = ground_temperature.getValue0();
                    double realTemperature = ground_temperature.getValue1();

                    Pair<Double, Float> riverInfo = GeneratorUtils.getRiverLevel(x + (chunkX * 16), y, z + (chunkZ * 16));
                    float river_noise = riverInfo.getValue1();

                    if (y > ground_level && y -1 < ground_level && river_noise > -0.16) {
                            List<String> ground_levels = ConfigData.ground_levels;
                            LinkedHashMap<String, Integer> level_height = ConfigData.levelHeights;
                            LinkedHashMap<String, Set<String>> levelToBiomeSet = ConfigData.levelToBiomeSet;
                            LinkedHashMap<String, LinkedHashMap<Double, List<String>>> biomeToTemp = ConfigData.biomeToTemp;
                            LinkedHashMap<String, Pair<List<String>, Integer>> biomeToTrees = ConfigData.biomeToTrees;

                            for (String level : ground_levels) {
                                if (y >= level_height.get(level)) {
                                    if (ground_levels.indexOf(level) + 1 < ground_levels.size() && y < level_height.get(ground_levels.get(ground_levels.indexOf(level) + 1))) {
                                        //Current level
                                        List<String> biomes = levelToBiomeSet.get(level).stream().toList();
                                        for (String biome : biomes) {
                                            double temperature = biomeToTemp.get(biome).keySet().stream().toList().get(0);
                                            if (realTemperature >= temperature) {
                                                if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < biomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                    //blocks
                                                    placeTree(biomeToTrees, biome, chunkX, chunkZ, x, z, y);
                                                    break;
                                                }
                                                if (biomes.indexOf(biome) + 1 == biomes.size()) {
                                                    //blocks
                                                    placeTree(biomeToTrees, biome, chunkX, chunkZ, x, z, y);
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    if (ground_levels.indexOf(level) + 1 == ground_levels.size()) {
                                        //Current level
                                        List<String> biomes = levelToBiomeSet.get(level).stream().toList();
                                        for (String biome : biomes) {
                                            double temperature = biomeToTemp.get(biome).keySet().stream().toList().get(0);
                                            if (realTemperature >= temperature) {
                                                if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < biomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                    //blocks
                                                    placeTree(biomeToTrees, biome, chunkX, chunkZ, x, z, y);
                                                    break;
                                                }
                                                if (biomes.indexOf(biome) + 1 == biomes.size()) {
                                                    //blocks
                                                    placeTree(biomeToTrees, biome, chunkX, chunkZ, x, z, y);
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        break;
                    }
                }
            }
        }
        super.populate(worldInfo, random, chunkX, chunkZ, limitedRegion);
    }

    private static void placeTree(LinkedHashMap<String, Pair<List<String>, Integer>> biomeToTrees, String biome, int chunkX, int chunkZ, int x, int z, int y){
        if(biomeToTrees.get(biome).getValue1() > 0) {
            int trees_rarity = biomeToTrees.get(biome).getValue1();
            if (new Random().nextInt(trees_rarity) == 0) {
                List<String> trees_schems = biomeToTrees.get(biome).getValue0();
                String tree_name = trees_schems.get(new Random().nextInt(trees_schems.size()));
                File schematic = new File(UltimateGenerator.getPlugin().getDataFolder() + File.separator + "schematics/trees/" +tree_name+".schem");
                Location loc = new Location(Bukkit.getServer().getWorld("world"),
                        chunkX*16+x, y-1, chunkZ*16+z);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
                            ClipboardReader reader = format.getReader(new FileInputStream(schematic));
                            Clipboard clipboard = reader.read();

                            AffineTransform transform = new AffineTransform();
                            transform = transform.rotateY(90*new Random().nextInt(4));
                            ClipboardHolder holder = new ClipboardHolder(clipboard);
                            holder.setTransform(holder.getTransform().combine(transform));

                            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), -1)) {
                                Operation operation = holder
                                        .createPaste(editSession)
                                        .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                                        // configure here
                                        .ignoreAirBlocks(true)
                                        .copyBiomes(false)
                                        .build();
                                Operations.complete(operation);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Something happened on pasting!");
                            }
                        }catch (Exception e){
                            System.out.println("Something happened on loading!");
                            System.out.println(schematic.getName());
                        }
                    }
                }.runTaskLater(UltimateGenerator.getPlugin(), 1);
            }
        }
    }
}