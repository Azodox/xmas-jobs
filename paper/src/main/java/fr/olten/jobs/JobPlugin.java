package fr.olten.jobs;

import co.aikar.commands.PaperCommandManager;
import com.google.common.base.Preconditions;
import fr.olten.jobs.bossbar.BossBarCheck;
import fr.olten.jobs.bossbar.BossBarProgression;
import fr.olten.jobs.bossbar.EarnedXPBossBar;
import fr.olten.jobs.commands.TestCommand;
import fr.olten.jobs.database.*;
import fr.olten.jobs.database.task.CacheSaveTask;
import fr.olten.jobs.listener.PlayerQuitListener;
import fr.olten.jobs.listener.block.BlockBreakListener;
import fr.olten.jobs.listener.block.BlockPlaceListener;
import lombok.Getter;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Azodox_ (Luke)
 * 23/7/2022.
 */

public class JobPlugin extends JavaPlugin {

    private @Getter final Map<UUID, EarnedXPBossBar> earnedXPBars = new HashMap<>();
    private @Getter PaperJobDatabaseManager jobDatabaseManager;
    private @Getter JedisPool jedisPool;
    private @Getter CacheSaver cacheSaver;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.jedisPool = new JedisPool(new JedisPoolConfig(),
                Preconditions.checkNotNull(getConfig().getString("redis.host"), "redis.host"),
                getConfig().getInt("redis.port"),
                Preconditions.checkNotNull(getConfig().getString("redis.user"), "redis.user"),
                Preconditions.checkNotNull(getConfig().getString("redis.password"), "redis.password"));

        var mongo = new Mongo(
                getConfig().getString("mongodb.username"),
                getConfig().getString("mongodb.authDatabase"),
                getConfig().getString("mongodb.password"),
                getConfig().getString("mongodb.host"),
                getConfig().getInt("mongodb.port"));
        var datastore = new MorphiaInitializer(this.getClass(), mongo.getMongoClient(), getConfig().getString("mongodb.database"), new String[]{"fr.olten.jobs"}).getDatastore();

        this.jobDatabaseManager = new PaperJobDatabaseManager(datastore);
        this.cacheSaver = new CacheSaver(this);

        getServer().getScheduler().runTaskTimer(this, new BossBarCheck(this), 0, 20);
        getServer().getScheduler().runTaskTimer(this, new BossBarProgression(this), 0, 1);
        getServer().getScheduler().runTaskTimer(this, new CacheSaveTask(this), 0L, (long) 20 * 600);

        var manager = new PaperCommandManager(this);
        manager.registerCommand(new TestCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);

        this.getServer().getServicesManager().register(JobDatabaseManager.class, this.jobDatabaseManager, this, ServicePriority.Normal);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        this.getServer().getOnlinePlayers().forEach(cacheSaver::save);
    }
}
