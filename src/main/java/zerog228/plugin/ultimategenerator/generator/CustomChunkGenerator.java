package zerog228.plugin.ultimategenerator.generator;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.javatuples.*;
import org.jetbrains.annotations.NotNull;
import zerog228.plugin.ultimategenerator.utils.ConfigData;
import zerog228.plugin.ultimategenerator.utils.CustomConfig;
import zerog228.plugin.ultimategenerator.utils.FastNoiseLite;
import zerog228.plugin.ultimategenerator.utils.GeneratorUtils;

import java.util.*;

public class CustomChunkGenerator extends ChunkGenerator {

    private final FastNoiseLite corralNoise = new FastNoiseLite();

    ConfigurationSection section = CustomConfig.get().getConfigurationSection("");
    private final int MAX_Y = section.getInt("MAX_Y");
    private final int WATER_LEVEL = section.getInt("WATER_LEVEL");
    private final long SEED = section.getInt("SEED");

    public CustomChunkGenerator() {
        corralNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        corralNoise.SetFrequency(0.001f);
        corralNoise.SetFractalType(FastNoiseLite.FractalType.PingPong);
        corralNoise.SetFractalOctaves(5);
        corralNoise.SetFractalLacunarity(2.9f);
        corralNoise.SetFractalGain(1.8f);
        corralNoise.SetFractalPingPongStrength(2f);
        corralNoise.SetSeed((int) SEED);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for(int y = chunkData.getMinHeight(); y < MAX_Y && y < chunkData.getMaxHeight(); y++) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    Pair<Double, Float> riverInfo = GeneratorUtils.getRiverLevel(x + (chunkX * 16), y, z + (chunkZ * 16));
                    double riverLevel = riverInfo.getValue0();
                    float river_noise = riverInfo.getValue1();

                    Pair<Double, Double> ground_temp = GeneratorUtils.getGroundLevelAndTemp(x + (chunkX * 16), y, z + (chunkZ * 16));
                    double ground_level = ground_temp.getValue0();
                    double realTemperature = ground_temp.getValue1();

                    Pair<Float, Float> cave_noises = GeneratorUtils.getCaveNoise(x + (chunkX * 16), z + (chunkZ * 16));
                    float cave_noise = cave_noises.getValue0();
                    float cave_noise2 = cave_noises.getValue1();

                    Pair<Float, Float> cave_noise_pitches = GeneratorUtils.getCaveNoisePitch(x + (chunkX * 16), z + (chunkZ * 16));
                    float cave_noise_pitch = cave_noise_pitches.getValue0();
                    float cave_noise_pitch2 = cave_noise_pitches.getValue1();

                    double cave_level = (30 * cave_noise_pitch) - 20;
                    double cave_level2 = (30 * cave_noise_pitch2) - 20;

                    List<String> ground_levels = ConfigData.ground_levels;
                    LinkedHashMap<String, Integer> level_height = ConfigData.levelHeights;
                    LinkedHashMap<String, Set<String>> levelToBiomeSet = ConfigData.levelToBiomeSet;
                    LinkedHashMap<String, LinkedHashMap<Double, List<String>>> biomeToTemp = ConfigData.biomeToTemp;

                    List<String> underground_levels = ConfigData.underground_levels;
                    LinkedHashMap<String, Integer> undergroundLevel_height = ConfigData.undergroundLevelHeights;
                    LinkedHashMap<String, Set<String>> undergroundLevelToBiomeSet = ConfigData.undergroundLevelToBiomeSet;
                    LinkedHashMap<String, LinkedHashMap<Double, List<String>>> undergroundBiomeToTemp = ConfigData.undergroundBiomeToTemp;

                    if(y < ground_level){
                        if(y + 1 > ground_level){
                            if(y > WATER_LEVEL || y == WATER_LEVEL){
                                if(river_noise >=-0.16 && chunkData.getType(x, y, z) == Material.AIR){
                                    for(String level : ground_levels){
                                        if(y >= level_height.get(level)){
                                            if(ground_levels.indexOf(level) + 1 < ground_levels.size() && y < level_height.get(ground_levels.get(ground_levels.indexOf(level) + 1))) {
                                                //Current level
                                                List<String> biomes = levelToBiomeSet.get(level).stream().toList();
                                                for (String biome : biomes) {
                                                    double temperature = biomeToTemp.get(biome).keySet().stream().toList().get(0);
                                                    if(realTemperature >= temperature) {
                                                        if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < biomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                            //blocks
                                                            placeBlock(chunkData, x, y, z, biome, temperature, biomeToTemp);
                                                            break;
                                                        }
                                                        if(biomes.indexOf(biome) + 1 == biomes.size()){
                                                            //blocks
                                                            placeBlock(chunkData, x, y, z, biome, temperature, biomeToTemp);
                                                            break;
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                            if(ground_levels.indexOf(level) + 1 == ground_levels.size()){
                                                //Current level
                                                List<String> biomes = levelToBiomeSet.get(level).stream().toList();
                                                for (String biome : biomes) {
                                                    double temperature = biomeToTemp.get(biome).keySet().stream().toList().get(0);
                                                    if(realTemperature >= temperature) {
                                                        if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < biomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                            //blocks
                                                            placeBlock(chunkData, x, y, z, biome, temperature, biomeToTemp);
                                                            break;
                                                        }
                                                        if(biomes.indexOf(biome) + 1 == biomes.size()){
                                                            //blocks
                                                            placeBlock(chunkData, x, y, z, biome, temperature, biomeToTemp);
                                                            break;
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }else {
                                    //river bank
                                    chunkData.setBlock(x, y, z, Material.SAND);
                                    chunkData.setBlock(x, y-1, z, Material.SAND);
                                }
                            }else if(y == WATER_LEVEL - 1){
                                //beach
                                chunkData.setBlock(x, y, z, Material.SAND);
                            } else {
                                //ocean underwater
                                chunkData.setBlock(x, y, z, Material.GRAVEL);
                            }
                        } else if(y + section.getInt("UNDERGROUND_LEVEL_THICKNESS") > ground_level && y > WATER_LEVEL && river_noise >=-0.16){
                            for(String level : underground_levels){
                                if(y >= undergroundLevel_height.get(level)){
                                    if(underground_levels.indexOf(level) + 1 < underground_levels.size() && y < undergroundLevel_height.get(underground_levels.get(underground_levels.indexOf(level) + 1))) {
                                        //Current level
                                        List<String> biomes = undergroundLevelToBiomeSet.get(level).stream().toList();
                                        for (String biome : biomes) {
                                            double temperature = undergroundBiomeToTemp.get(biome).keySet().stream().toList().get(0);
                                            if(realTemperature >= temperature) {
                                                if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < undergroundBiomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                    //blocks
                                                    chunkData.setBlock(x, y, z, Material.getMaterial(undergroundBiomeToTemp.get(biome).get(temperature).get(new Random().nextInt(undergroundBiomeToTemp.get(biome).get(temperature).size()))));
                                                    break;
                                                }
                                                if(biomes.indexOf(biome) + 1 == biomes.size()){
                                                    chunkData.setBlock(x, y, z, Material.getMaterial(undergroundBiomeToTemp.get(biome).get(temperature).get(new Random().nextInt(undergroundBiomeToTemp.get(biome).get(temperature).size()))));
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    if(underground_levels.indexOf(level) + 1 == underground_levels.size()){
                                        //Current level
                                        List<String> biomes = undergroundLevelToBiomeSet.get(level).stream().toList();
                                        for (String biome : biomes) {
                                            double temperature = undergroundBiomeToTemp.get(biome).keySet().stream().toList().get(0);
                                            if(realTemperature >= temperature) {
                                                if (biomes.indexOf(biome) + 1 < biomes.size() && realTemperature < undergroundBiomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                                    //blocks
                                                    chunkData.setBlock(x, y, z, Material.getMaterial(undergroundBiomeToTemp.get(biome).get(temperature).get(new Random().nextInt(undergroundBiomeToTemp.get(biome).get(temperature).size()))));
                                                    break;
                                                }
                                                if(biomes.indexOf(biome) + 1 == biomes.size()){
                                                    chunkData.setBlock(x, y, z, Material.getMaterial(undergroundBiomeToTemp.get(biome).get(temperature).get(new Random().nextInt(undergroundBiomeToTemp.get(biome).get(temperature).size()))));
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        else{
                            if(y>=0){
                                chunkData.setBlock(x, y, z, Material.STONE);
                                if (ground_level < WATER_LEVEL){
                                    chunkData.setBlock(x, y, z, Material.STONE);
                                }
                            }else {
                                chunkData.setBlock(x, y, z, Material.DEEPSLATE);
                            }
                        }
                    }else {
                        if(y <= WATER_LEVEL){
                            chunkData.setBlock(x, y, z, Material.WATER);
                        }else if(river_noise < -0.2 && y < riverLevel - 2){
                            chunkData.setBlock(x, y-1, z, Material.WATER);
                        }
                    }
                    if(y < cave_level && y+Math.abs(cave_noise)*15 >= cave_level && cave_noise < -0.3 && y < ground_level){
                        if(y + 7 > ground_level && y < WATER_LEVEL){
                            chunkData.setBlock(x, y, z, Material.WATER);
                        }else {
                            if(y < -50){
                                chunkData.setBlock(x, y, z, Material.LAVA);
                            }else {
                                chunkData.setBlock(x, y, z, Material.CAVE_AIR);
                            }
                        }
                    }

                    if(y < cave_level2 && y+Math.abs(cave_noise2)*15 >= cave_level2 && cave_noise2 < -0.3 && y < ground_level){
                        if(y + 7 > ground_level && y < WATER_LEVEL){
                            chunkData.setBlock(x, y, z, Material.WATER);
                        }else {
                            if(y < -40){
                                chunkData.setBlock(x, y, z, Material.LAVA);
                            }else {
                                chunkData.setBlock(x, y, z, Material.CAVE_AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void placeBlock(ChunkData chunkData, int x, int y, int z, String biome, double temperature, LinkedHashMap<String, LinkedHashMap<Double,
            List<String>>> biomeToTemp){
        try {
            chunkData.setBlock(x, y, z, Material.getMaterial(biomeToTemp.get(biome).get(temperature).get(new Random().nextInt(biomeToTemp.get(biome).get(temperature).size()))));
        }catch (Exception e){
            System.out.println("Material is null!!!");
            System.out.println(biome+" - biome");
            System.out.println("Material is null!!!");
        }
        List<String> decor = ConfigData.biomeToDecor.get(biome).getValue0();
        if(decor == null){
            System.out.println("decor is null!!!");
            System.out.println(biome+" - biome");
            System.out.println("decor is null!!!");
        }
        //TODO Помечать чанк кастомным биомом

        int decor_rarity = ConfigData.biomeToDecor.get(biome).getValue1();
        Material decor_block = Material.valueOf(decor.get(new Random().nextInt(decor.size())));
        if(0 == new Random().nextInt(decor_rarity)) {
            chunkData.setBlock(x, y + 1, z, decor_block);
            if (chunkData.getBlockData(x, y+1, z) instanceof Directional d && d.getFaces().contains(BlockFace.UP) && d.getFaces().contains(BlockFace.DOWN)) {
                d.setFacing(BlockFace.UP);
                chunkData.setBlock(x, y+1, z, d);
            }
            if(chunkData.getBlockData(x, y+1, z) instanceof FaceAttachable a){
                a.setAttachedFace(FaceAttachable.AttachedFace.FLOOR);
                chunkData.setBlock(x, y+1, z, a);
            }
            if(chunkData.getBlockData(x, y+1, z) instanceof Waterlogged w){
                w.setWaterlogged(false);
                chunkData.setBlock(x, y+1, z, w);
            }
        }
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(new CustomChunkPopulator());
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        for (int y = chunkData.getMinHeight(); y < MAX_Y && y < chunkData.getMaxHeight(); y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if(y>0){
                        if(x == 8 && z == 8 && 0 == new Random().nextInt(500) && y < 44 && y > 5){
                            chunkData.setBlock(x, y, z, Material.BLACKSTONE);
                        }
                    }else {
                        if(x == 8 && z == 8 && 0 == new Random().nextInt(200)){
                            chunkData.setBlock(x, y, z, Material.BLACKSTONE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }


    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}