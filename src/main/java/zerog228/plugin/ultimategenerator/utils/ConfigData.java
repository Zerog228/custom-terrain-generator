package zerog228.plugin.ultimategenerator.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.javatuples.Pair;
import org.javatuples.Quintet;

import java.util.*;

public class ConfigData {

    public static List<String> ground_levels;
    public static LinkedHashMap<String, Integer> levelHeights = new LinkedHashMap<>();
    public static LinkedHashMap<String, Set<String>> levelToBiomeSet = new LinkedHashMap<>();
    public static LinkedHashMap<String, LinkedHashMap<Double, List<String>>> biomeToTemp = new LinkedHashMap<>();

    public static LinkedHashMap<String, String> biomeToBiome = new LinkedHashMap<>();
    public static LinkedHashMap<String, Pair<List<String>, Integer>> biomeToDecor = new LinkedHashMap<>();
    public static LinkedHashMap<String, Pair<List<String>, Integer>> biomeToTrees = new LinkedHashMap<>();

    public static List<String> underground_levels;
    public static LinkedHashMap<String, Integer> undergroundLevelHeights = new LinkedHashMap<>();
    public static LinkedHashMap<String, Set<String>> undergroundLevelToBiomeSet = new LinkedHashMap<>();
    public static LinkedHashMap<String, LinkedHashMap<Double, List<String>>> undergroundBiomeToTemp = new LinkedHashMap<>();

    public static void getSpecializationsList(){
        ConfigurationSection section = CustomConfig.get().getConfigurationSection("ground_level");
        List<String> ground_levels = new ArrayList<>(section.getKeys(false));
        ConfigData.ground_levels = ground_levels;

        ConfigurationSection section2 = CustomConfig.get().getConfigurationSection("underground_level");
        List<String> underground_levels = new ArrayList<>(section2.getKeys(false));
        ConfigData.underground_levels = underground_levels;

        for(String level : ground_levels){
            levelHeights.put(level, section.getInt(level+".min_height"));
            ConfigurationSection level_section = CustomConfig.get().getConfigurationSection("ground_level."+level+".biomes");
            Set<String> biomes = level_section.getKeys(false);
            levelToBiomeSet.put(level, biomes);
            for(String biome : biomes){
                ConfigurationSection blocks_selection = CustomConfig.get().getConfigurationSection("ground_level."+level+".biomes."+biome);
                if(blocks_selection == null){
                    System.out.println("SECTION is NULL");
                    System.out.println(level+" - level");
                    System.out.println(biome+" - biome");
                    System.out.println("SECTION is NULL");
                }
                try{
                    List<String> trees_schems = (List<String>) blocks_selection.getList("trees");
                    Pair<List<String>, Integer> trees_and_rarity = new Pair<>(trees_schems, blocks_selection.getInt("trees_rarity"));
                    biomeToTrees.put(biome, trees_and_rarity);
                    //System.out.println(biome+" - biome, "+blocks_selection.getInt("trees_rarity")+" - rarity!");
                }catch (Exception e){
                    System.out.println("For biome \""+biome+"\" trees are unavailable!");
                }
                String minecraft_biome = blocks_selection.getString("biome");
                biomeToBiome.put(biome, minecraft_biome);
                List<String> blocks = (List<String>) blocks_selection.getList("blocks");
                double min_temperature = blocks_selection.getDouble("min_temperature");
                LinkedHashMap<Double, List<String>> tempToList = new LinkedHashMap<>();
                tempToList.put(min_temperature, blocks);
                ConfigData.biomeToTemp.put(biome, tempToList);

                List<String> decor_blocks = (List<String>) blocks_selection.getList("decorations");
                Pair<List<String>, Integer> decor_and_rarity = new Pair<>(decor_blocks, blocks_selection.getInt("decoration_rarity"));
                biomeToDecor.put(biome, decor_and_rarity);
            }
        }

        for(String level : underground_levels){
            undergroundLevelHeights.put(level, section.getInt(level+".min_height"));
            ConfigurationSection level_section = CustomConfig.get().getConfigurationSection("underground_level."+level+".biomes");
            Set<String> biomes = level_section.getKeys(false);
            undergroundLevelToBiomeSet.put(level, biomes);
            for(String biome : biomes){
                ConfigurationSection blocks_selection = CustomConfig.get().getConfigurationSection("underground_level."+level+".biomes."+biome);
                List<String> blocks = (List<String>) blocks_selection.getList("blocks");
                double min_temperature = blocks_selection.getDouble("min_temperature");
                LinkedHashMap<Double, List<String>> tempToList = new LinkedHashMap<>();
                tempToList.put(min_temperature, blocks);
                ConfigData.undergroundBiomeToTemp.put(biome, tempToList);
            }
        }
    }
}
