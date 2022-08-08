package fr.olten.jobs.listener.block;

import fr.olten.jobs.JobPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;


/**
 * @author Azodox_ (Luke)
 * 4/8/2022.
 */

public class BlockPlaceListener implements Listener {

    private final JobPlugin jobPlugin;

    public BlockPlaceListener(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        event.getBlock().setMetadata("PLACED", new FixedMetadataValue(jobPlugin, true));
    }
}
