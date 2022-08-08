package fr.olten.jobs.listener;

import fr.olten.jobs.JobPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * @author Azodox_ (Luke)
 * 5/8/2022.
 */

public class PlayerQuitListener implements Listener {

    private final JobPlugin jobPlugin;
    public PlayerQuitListener(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        jobPlugin.getCacheSaver().save(event.getPlayer());
    }
}
