package fr.olten.jobs.database;

import com.mongodb.client.result.UpdateResult;
import dev.morphia.query.Query;
import fr.olten.jobs.Job;
import fr.olten.jobs.database.mining.MinedBlockXP;
import fr.olten.jobs.database.power.JobPowerModel;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Class created to be able to deal with jobs and the database.<br>
 * <br>With this one, you can retrieve a player's progression on a job for example.
 * @author Azodox_ (Luke)
 * 23/7/2022.
 */

public interface JobDatabaseManager {

    /**
     * Query the database for a job.
     * @param job The job to query for.
     * @return A {@link JobDatabaseModel} that allows you to retrieve configurable fields of the job (e.g. name, description, etc.)
     * @see JobDatabaseManager#queryPlayerJob(UUID, Job) How to query a player's progression on a job.
     */
    Query<? extends JobDatabaseModel> queryJob(@NotNull Job job);

    /**
     * Query the database for player's information on a job.<br>
     * <br>
     * Note: If you want to check a player's progression on his <b>current</b> job, use {@link Job#getJob(int)} to retrieve a job
     * using its id. You can probably retrieve your player's current job's id by using an account system.
     * @param uuid The player's UUID.
     * @param job The player's job progression to retrieve of.
     * @return A {@link JobDatabaseModel} that allows you to retrieve configurable fields of the job (e.g. name, description, etc.)
     */
    Query<? extends JobDatabaseModel> queryPlayerJob(@NotNull UUID uuid, @NotNull Job job);

    Query<JobPowerModel> queryJobPower(int powerId);
    void saveJobPower(JobPowerModel jobPower);
    void saveBlockXP(MinedBlockXP blockXP);
    MinedBlockXP getMinedBlockXP(String minecraftTag);

    /**
     * Change a player's current job. You can't put a null value on the second parameter
     * because the player is not allowed to not have a job.
     * @param uuid The player's UUID.
     * @param job The new player's job (can't be null)
     * @return The result of the update.
     */
    UpdateResult setJob(@NotNull UUID uuid, @NotNull Job job);

    /**
     * Increment a player's progression on a job.<br>
     * <br>Method to decrement a player's progression on a job doesn't exist because the player cannot go backwards on their
     * progression.
     * <br>It's unlogical to be able to lose progression on a job.
     * @param uuid The player's UUID.
     * @param job The job to increment the player's progression on.
     * @param xp The amount of xp to increment the player's progression.
     * @return The result of the update.
     */
    UpdateResult incrementJobProgression(@NotNull UUID uuid, @NotNull Job job, double xp);
}
