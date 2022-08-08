package fr.olten.jobs.database.task;

import fr.olten.jobs.JobPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Azodox_ (Luke)
 * 6/8/2022.
 */

public class CacheSaveTask implements Runnable {

    private final JobPlugin jobPlugin;

    public CacheSaveTask(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            jobPlugin.getCacheSaver().save(player);
        }
    }
}
