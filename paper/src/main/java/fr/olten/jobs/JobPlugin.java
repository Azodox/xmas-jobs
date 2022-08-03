package fr.olten.jobs;

import co.aikar.commands.PaperCommandManager;
import fr.olten.jobs.commands.TestCommand;
import fr.olten.jobs.database.Mongo;
import fr.olten.jobs.database.MorphiaInitializer;
import fr.olten.jobs.database.PaperJobDatabaseManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Azodox_ (Luke)
 * 23/7/2022.
 */

public class JobPlugin extends JavaPlugin {

    private @Getter PaperJobDatabaseManager jobDatabaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        var mongo = new Mongo(
                getConfig().getString("mongodb.username"),
                getConfig().getString("mongodb.authDatabase"),
                getConfig().getString("mongodb.password"),
                getConfig().getString("mongodb.host"),
                getConfig().getInt("mongodb.port"));
        var datastore = new MorphiaInitializer(this.getClass(), mongo.getMongoClient(), getConfig().getString("mongodb.database"), new String[]{"fr.olten.jobs"}).getDatastore();

        this.jobDatabaseManager = new PaperJobDatabaseManager(datastore);

        var manager = new PaperCommandManager(this);
        manager.registerCommand(new TestCommand(this));

        getLogger().info("Enabled!");
    }
}
