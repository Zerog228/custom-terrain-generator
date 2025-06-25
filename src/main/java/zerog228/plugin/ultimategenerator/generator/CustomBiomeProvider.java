package zerog228.plugin.ultimategenerator.generator;

import com.google.common.collect.Lists;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import zerog228.plugin.ultimategenerator.utils.ConfigData;
import zerog228.plugin.ultimategenerator.utils.CustomConfig;
import zerog228.plugin.ultimategenerator.utils.GeneratorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class CustomBiomeProvider extends BiomeProvider {

    private static final ConfigurationSection section = CustomConfig.get().getConfigurationSection("");
    private static final int WATER_LEVEL = section.getInt("WATER_LEVEL");
    private static final double COLD_OCEAN_NUMBER = section.getDouble("COLD_OCEAN_NUMBER");
    private static final double WARM_OCEAN_NUMBER = section.getDouble("WARM_OCEAN_NUMBER");

    private static Biome biome = Biome.BADLANDS;

    //TODO Ставить кастомный биом для каждого биома
    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {

        List<String> ground_levels = ConfigData.ground_levels;
        LinkedHashMap<String, Integer> level_height = ConfigData.levelHeights;
        LinkedHashMap<String, Set<String>> levelToBiomeSet = ConfigData.levelToBiomeSet;
        LinkedHashMap<String, LinkedHashMap<Double, List<String>>> biomeToTemp = ConfigData.biomeToTemp;
        LinkedHashMap<String, String> biomeToBiome = ConfigData.biomeToBiome;

        Pair<Double, Float> riverInfo = GeneratorUtils.getRiverLevel(x, y, z);
        double riverLevel = riverInfo.getValue0();
        float river_noise = riverInfo.getValue1();

        Pair<Double, Double> ground_temp = GeneratorUtils.getGroundLevelAndTemp(x, y, z);
        double ground_level = ground_temp.getValue0();
        double realTemperature = ground_temp.getValue1();

        if (ground_level <= WATER_LEVEL) {
            if (realTemperature <= COLD_OCEAN_NUMBER) {
                biome = Biome.COLD_OCEAN;
                return biome;
            } else if (realTemperature >= WARM_OCEAN_NUMBER) {
                biome = Biome.DEEP_LUKEWARM_OCEAN;
                return biome;
            } else {
                biome = Biome.OCEAN;
                return biome;
            }
        } else {
            if(y <= WATER_LEVEL){
                return Biome.OCEAN;
            }else if(river_noise < -0.2 && y < riverLevel - 2){
                return Biome.RIVER;
            }
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
                                    CustomBiomeProvider.biome = Biome.valueOf(biomeToBiome.get(biome));
                                    return CustomBiomeProvider.biome;
                                }
                                if (biomes.indexOf(biome) + 1 == biomes.size()) {
                                    CustomBiomeProvider.biome = Biome.valueOf(biomeToBiome.get(biome));
                                    return CustomBiomeProvider.biome;
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
                                    CustomBiomeProvider.biome = Biome.valueOf(biomeToBiome.get(biome));
                                    return CustomBiomeProvider.biome;
                                }
                                if (biomes.indexOf(biome) + 1 == biomes.size()) {
                                    //blocks
                                    CustomBiomeProvider.biome = Biome.valueOf(biomeToBiome.get(biome));
                                    return CustomBiomeProvider.biome;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return Biome.BADLANDS;
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return Lists.newArrayList(Biome.ICE_SPIKES, Biome.SNOWY_TAIGA, Biome.DESERT, Biome.WINDSWEPT_GRAVELLY_HILLS, Biome.WINDSWEPT_SAVANNA, Biome.SAVANNA_PLATEAU, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.MUSHROOM_FIELDS, Biome.SAVANNA, Biome.BAMBOO_JUNGLE, Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.OLD_GROWTH_PINE_TAIGA, Biome.DARK_FOREST, Biome.MEADOW, Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.TAIGA, Biome.BADLANDS, Biome.WOODED_BADLANDS, Biome.DRIPSTONE_CAVES, Biome.LUSH_CAVES, Biome.BEACH, Biome.SWAMP, Biome.STONY_SHORE, Biome.DEEP_COLD_OCEAN, Biome.COLD_OCEAN, Biome.OCEAN, Biome.WARM_OCEAN, Biome.RIVER, Biome.BIRCH_FOREST);
    }
}