package fr.olten.jobs.database;

import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.jobs.Job;
import fr.olten.jobs.JobPlugin;
import net.valneas.account.AccountSystemApi;
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

        var globalXpEarned = jedis.hget("player#" + player.getUniqueId(), "global#xpEarned");
        var globalFlcEarned = jedis.hget("player#" + player.getUniqueId(), "global#flcEarned");

        if(globalXpEarned == null || globalFlcEarned == null){
            return;
        }

        jedis.hdel("player#" + player.getUniqueId(), "global#xpEarned");
        jedis.hdel("player#" + player.getUniqueId(), "global#flcEarned");

        var provider = jobPlugin.getServer().getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            var api = provider.getProvider();
            var accountManager = api.getAccountManager(player);
            var query = accountManager.getAccountQuery();
            query.update(UpdateOperators.set("xp", Double.parseDouble(globalXpEarned))).execute();
            query.update(UpdateOperators.set("money", Double.parseDouble(globalFlcEarned))).execute();
        }

        for (Job job : Job.values()) {
            var xpEarned = jedis.hget("player#" + player.getUniqueId(), "job#" + job.getId() + "#xpEarned");
            jedis.hdel("player#" + player.getUniqueId(), "job#" + job.getId() + "#xpEarned");

            if(xpEarned != null){
                jobPlugin.getJobDatabaseManager().incrementJobProgression(player.getUniqueId(), job, Double.parseDouble(xpEarned));
            }
        }

        jobPlugin.getLogger().info("Saved cache in db for " + player.getName());
        jedis.close();
    }
}
