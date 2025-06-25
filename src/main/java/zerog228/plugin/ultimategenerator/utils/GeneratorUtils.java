package zerog228.plugin.ultimategenerator.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.javatuples.Pair;

import java.text.DecimalFormat;

public class GeneratorUtils {

    private static final FastNoiseLite terrainNoise = new FastNoiseLite();
    private static final FastNoiseLite riverNoise = new FastNoiseLite();
    private static final FastNoiseLite mountainNoise = new FastNoiseLite();
    private static final FastNoiseLite biomeNoise = new FastNoiseLite();
    private static final FastNoiseLite temperatureNoise = new FastNoiseLite();
    private static final FastNoiseLite caveNoise = new FastNoiseLite();
    private static final FastNoiseLite caveNoise2 = new FastNoiseLite();
    private static final FastNoiseLite caveNoisePitch = new FastNoiseLite();
    private static final FastNoiseLite caveNoisePitch2 = new FastNoiseLite();

    private static final FastNoiseLite corralNoise = new FastNoiseLite();

    private static ConfigurationSection section = CustomConfig.get().getConfigurationSection("");
    private static final int WATER_LEVEL = section.getInt("WATER_LEVEL");
    private static final int MIN_Y = section.getInt("MIN_Y");
    private static final long SEED = section.getInt("SEED");

    public static void tuneGenerators() {
        terrainNoise.SetFrequency(0.002f);
        terrainNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        terrainNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        terrainNoise.SetFractalOctaves(3);
        terrainNoise.SetFractalLacunarity(3.6f);
        terrainNoise.SetFractalGain(0.1f);
        terrainNoise.SetSeed((int) SEED);

        riverNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        riverNoise.SetFrequency(0.001f);
        riverNoise.SetFractalType(FastNoiseLite.FractalType.PingPong);
        riverNoise.SetFractalOctaves(3);
        riverNoise.SetFractalLacunarity(2.2f);
        riverNoise.SetFractalGain(2.5f);
        riverNoise.SetFractalWeightedStrength(0.5f);
        riverNoise.SetFractalPingPongStrength(2);
        riverNoise.SetSeed((int) SEED);

        mountainNoise.SetNoiseType(FastNoiseLite.NoiseType.ValueCubic);
        mountainNoise.SetFrequency(0.001f);
        mountainNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        mountainNoise.SetFractalOctaves(4);
        mountainNoise.SetFractalLacunarity(1.4f);
        mountainNoise.SetFractalGain(0.5f);
        mountainNoise.SetSeed((int) SEED);

        biomeNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        biomeNoise.SetFrequency(0.0005f);
        biomeNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        biomeNoise.SetFractalOctaves(1);
        biomeNoise.SetSeed((int) SEED);

        temperatureNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        temperatureNoise.SetFrequency(0.002f);
        temperatureNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        temperatureNoise.SetFractalOctaves(1);
        temperatureNoise.SetSeed((int) SEED/2+1);

        caveNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoise.SetFrequency(0.005f);
        caveNoise.SetFractalType(FastNoiseLite.FractalType.PingPong);
        caveNoise.SetFractalOctaves(2);
        caveNoise.SetFractalLacunarity(2.0f);
        caveNoise.SetFractalGain(1.6f);
        caveNoise.SetFractalWeightedStrength(1.0f);
        caveNoise.SetFractalPingPongStrength(2.0f);
        caveNoise.SetSeed((int) SEED);

        caveNoise2.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoise2.SetFrequency(0.005f);
        caveNoise2.SetFractalType(FastNoiseLite.FractalType.PingPong);
        caveNoise2.SetFractalOctaves(2);
        caveNoise2.SetFractalLacunarity(2.0f);
        caveNoise2.SetFractalGain(1.6f);
        caveNoise2.SetFractalWeightedStrength(1.0f);
        caveNoise2.SetFractalPingPongStrength(2.0f);
        caveNoise2.SetSeed((int) SEED/2+10);

        caveNoisePitch.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoisePitch.SetFrequency(0.005f);
        caveNoisePitch.SetFractalType(FastNoiseLite.FractalType.FBm);
        caveNoisePitch.SetFractalOctaves(1);
        caveNoisePitch.SetFractalLacunarity(2.0f);
        caveNoisePitch.SetFractalGain(0.5f);
        caveNoisePitch.SetFractalWeightedStrength(1.0f);
        caveNoisePitch.SetSeed((int) SEED/2);

        caveNoisePitch2.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoisePitch2.SetFrequency(0.005f);
        caveNoisePitch2.SetFractalType(FastNoiseLite.FractalType.FBm);
        caveNoisePitch2.SetFractalOctaves(1);
        caveNoisePitch2.SetFractalLacunarity(2.0f);
        caveNoisePitch2.SetFractalGain(0.5f);
        caveNoisePitch2.SetFractalWeightedStrength(1.0f);
        caveNoisePitch2.SetSeed((int) SEED/4+10);

        corralNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        corralNoise.SetFrequency(0.001f);
        corralNoise.SetFractalType(FastNoiseLite.FractalType.PingPong);
        corralNoise.SetFractalOctaves(5);
        corralNoise.SetFractalLacunarity(2.9f);
        corralNoise.SetFractalGain(1.8f);
        corralNoise.SetFractalPingPongStrength(2f);
        corralNoise.SetSeed((int) SEED);
    }

    public static Pair<Double, Double> getGroundLevelAndTemp(int x, int y, int z){
        float mountain_noise = Math.abs(mountainNoise.GetNoise(x, z));
        float biome_noise = (biomeNoise.GetNoise(x, z));
        float river_noise = (riverNoise.GetNoise(x, z));
        float temperature_noise = (temperatureNoise.GetNoise(x, z));
        float ground_noise = ((terrainNoise.GetNoise(x, z)) - temperature_noise / 2);

        DecimalFormat format = new DecimalFormat("0.##");
        double realTemperature = Double.parseDouble(format.format(temperature_noise).replace(",", "."));

        if (biome_noise > 0) {
            ground_noise += Math.pow((mountain_noise + 0.3) * 4, 2) * Math.pow((biome_noise * 2), 2);
        } else {
            ground_noise -= Math.pow((biome_noise * 2), 2);
        }
        if (river_noise < 0 && y >= WATER_LEVEL - 1) {
            river_noise /= Math.abs(biome_noise * 2) + 0.5f + mountain_noise * 4;
            if (river_noise < -0.1) {
                ground_noise += river_noise / (1.6 + mountain_noise / 2 + ground_noise / 2);
            }
        }

        double ground_level = (MIN_Y + ground_noise * 30.0F);

        if(x == 25 && ground_level>500 && y ==10){
            System.out.println("========================");
            System.out.println(ground_level+" - ground level");
            System.out.println(ground_noise+" - ground noise");
            System.out.println(river_noise+" - river noise");
            System.out.println(biome_noise+" - biome noise");
            System.out.println(mountain_noise+" - mountain noise");
            System.out.println(temperature_noise+" - temperature noise");
            System.out.println("========================");
        }

        return new Pair<>(ground_level, realTemperature);
    }

    public static Pair<Float, Float> getCaveNoise(int x, int z){
        return new Pair<>((caveNoise.GetNoise(x, z)), caveNoise2.GetNoise(x, z));
    }

    public static Pair<Float, Float> getCaveNoisePitch(int x, int z){
        return new Pair<>((caveNoisePitch.GetNoise(x, z)), caveNoisePitch2.GetNoise(x, z));
    }

    public static Pair<Double, Float> getRiverLevel(int x, int y, int z){
        float mountain_noise = Math.abs(mountainNoise.GetNoise(x, z));
        float biome_noise = (biomeNoise.GetNoise(x, z));
        float river_noise = (riverNoise.GetNoise(x, z));
        float temperature_noise = (temperatureNoise.GetNoise(x, z));
        float ground_noise = ((terrainNoise.GetNoise(x, z)) - temperature_noise / 2);

        if (biome_noise > 0) {
            ground_noise += Math.pow((mountain_noise + 0.3) * 4, 2) * Math.pow((biome_noise * 2), 2);
        } else {
            ground_noise -= Math.pow((biome_noise * 2), 2);
        }
        if (river_noise < 0 && y >= WATER_LEVEL - 1) {
            river_noise /= Math.abs(biome_noise * 2) + 0.5f + mountain_noise * 4;
            if (river_noise < -0.1) {
                ground_noise += river_noise / (1.6 + mountain_noise / 2 + ground_noise / 2);
            }
        }
        return new Pair<>((double) MIN_Y+(ground_noise-river_noise/2)*30, river_noise);
    }
}
