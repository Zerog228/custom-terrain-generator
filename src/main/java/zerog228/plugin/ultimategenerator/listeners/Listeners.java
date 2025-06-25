package zerog228.plugin.ultimategenerator.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class Listeners implements Listener {

    @EventHandler
    private void chunkGenerateEvent(ChunkLoadEvent e) {
        if (e.isNewChunk()) {

        }
    }
}
