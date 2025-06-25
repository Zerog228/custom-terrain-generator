package zerog228.plugin.ultimategenerator.commands;

import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zerog228.plugin.ultimategenerator.UltimateGenerator;
import zerog228.plugin.ultimategenerator.utils.CustomConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.region;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("ultimate_config_reload") && sender instanceof Player p){
            //CustomConfig.reload();
            //loadSchematic(p);
            /*double your_temperature = Double.parseDouble(args[0]);
            List<String> ground_levels = ConfigData.ground_levels;
            LinkedHashMap<String, Integer> level_height = ConfigData.levelHeights;
            LinkedHashMap<String, Set<String>> levelToBiomeSet = ConfigData.levelToBiomeSet;
            LinkedHashMap<String, LinkedHashMap<Double, List<String>>> biomeToTemp = ConfigData.biomeToTemp;
            int height = p.getLocation().getBlockY();
            for(String level : ground_levels){
                if(height >= level_height.get(level)){
                    if(ground_levels.indexOf(level) + 1 < ground_levels.size() && height < level_height.get(ground_levels.get(ground_levels.indexOf(level) + 1))) {
                        //Current level
                        sender.sendMessage(level);
                        List<String> biomes = levelToBiomeSet.get(level).stream().toList();
                        for (String biome : biomes) {
                            double temperature = biomeToTemp.get(biome).keySet().stream().toList().get(0);
                            if(your_temperature >= temperature) {
                                if (biomes.indexOf(biome) + 1 < biomes.size() && your_temperature < biomeToTemp.get(biomes.get(biomes.indexOf(biome) + 1)).keySet().stream().toList().get(0)) {
                                    for(String block : biomeToTemp.get(biome).get(temperature)){
                                        //blocks
                                        sender.sendMessage(block);
                                    }
                                    break;
                                }
                                if(biomes.indexOf(biome) + 1 == biomes.size()){
                                    for(String block : biomeToTemp.get(biome).get(temperature)){
                                        //blocks
                                        sender.sendMessage(block);
                                    }
                                    break;
                                }
                            }
                        }
                        sender.sendMessage("================");
                        break;
                    }
                    if(ground_levels.indexOf(level) + 1 == ground_levels.size()){
                        //Current level
                        sender.sendMessage(level);
                        sender.sendMessage("================");
                        break;
                    }
                }
            }*/
            Random random = new Random();
            Location loc = p.getLocation();
            World world = loc.getWorld();
            int x, y, z;
            x = loc.getBlockX();
            y = loc.getBlockY() + 15;
            z = loc.getBlockZ();

        }
        return true;
    }

    private void loadSchematic(Player p) {

        //Location loc = p.getLocation();
        Location loc = new Location(Bukkit.getServer().getWorld("world"), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        File schematic = new File(UltimateGenerator.getPlugin().getDataFolder() + File.separator + "schematics/trees/SWAMP_TREE_2.schem");
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            ClipboardReader reader = format.getReader(new FileInputStream(schematic));
            Clipboard clipboard = reader.read();

            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(90*new Random().nextInt(4));
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            holder.setTransform(holder.getTransform().combine(transform));

            try(EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), -1)){
                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                        // configure here
                        .ignoreAirBlocks(true)
                        .build();
                Operations.complete(operation);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Something happened on pasting!");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something happened on loading!");
        }
    }
}
