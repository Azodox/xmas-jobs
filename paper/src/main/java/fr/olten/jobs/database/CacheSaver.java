package fr.olten.jobs.database;

import fr.olten.jobs.Job;
import fr.olten.jobs.JobPlugin;
import org.bukkit.entity.Player;

/**
 * @author Azodox_ (Luke)
 * 5/8/2022.
 */

public class CacheSaver {

    private final JobPlugin jobPlugin;

    public CacheSaver(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    public void save(Player player){
        var jedis = jobPlugin.getJedisPool().getResource();

        for (Job job : Job.values()) {
            var xpEarned = jedis.hget("player#" + player.getUniqueId(), "job#" + job.getId() + "#xpEarned");
            jedis.hdel("player#" + player.getUniqueId(), "job#" + job.getId() + "#xpEarned");

            if(xpEarned != null){
                jobPlugin.getJobDatabaseManager().incrementJobProgression(player.getUniqueId(), job, Double.parseDouble(xpEarned));
            }
        }

        jobPlugin.getLogger().info("Saved earned job xp in db for " + player.getName());
        jedis.close();
    }
}
