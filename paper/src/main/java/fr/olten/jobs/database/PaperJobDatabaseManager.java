package fr.olten.jobs.database;

import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.olten.jobs.Job;
import fr.olten.jobs.database.mining.MinedBlockXP;
import fr.olten.jobs.database.power.JobPowerModel;
import fr.olten.jobs.database.progression.JobProgression;
import fr.olten.jobs.database.progression.JobProgressionModel;
import net.valneas.account.AccountSystemApi;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * @author Azodox_ (Luke)
 * 23/7/2022.
 */

public class PaperJobDatabaseManager implements JobDatabaseManager {

    private final Datastore datastore;

    public PaperJobDatabaseManager(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Query<PaperJobDatabaseModel> queryJob(@NotNull Job job) {
        return datastore.find(PaperJobDatabaseModel.class).filter(Filters.eq("id", job.getId()));
    }

    @Override
    public Query<PaperJobDatabaseModel> queryPlayerJob(@NotNull UUID uuid, @NotNull Job job) {
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            var api = provider.getProvider();
            var accountManager = api.getAccountManager(Bukkit.getPlayer(uuid));
            var currentJobId = accountManager.getAccount().getCurrentJobId();

            return datastore.find(PaperJobDatabaseModel.class).filter(Filters.eq("id", currentJobId));
        }
        return null;
    }

    @Override
    public Query<JobPowerModel> queryJobPower(int powerId) {
        return datastore.find(JobPowerModel.class).filter(Filters.eq("id", powerId));
    }

    @Override
    public void saveJobPower(JobPowerModel jobPower) {
        this.datastore.save(jobPower);
    }

    @Override
    public void saveBlockXP(MinedBlockXP blockXP) {
        this.datastore.save(blockXP);
    }

    @Override
    public MinedBlockXP getMinedBlockXP(String minecraftTag) {
        return datastore.find(MinedBlockXP.class).filter(Filters.eq("minecraftTag", minecraftTag)).first();
    }

    @Override
    public UpdateResult setJob(@NotNull UUID uuid, @NotNull Job job) {
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            var api = provider.getProvider();
            var accountManager = api.getAccountManager(Bukkit.getPlayer(uuid));
            accountManager.getAccountQuery().update(UpdateOperators.set("last-job-change", System.currentTimeMillis())).execute();
            return accountManager.getAccountQuery().update(UpdateOperators.set("current-job", job.getId())).execute();
        }
        return UpdateResult.unacknowledged();
    }

    @Override
    public UpdateResult incrementJobProgression(@NotNull UUID uuid, @NotNull Job job, double xp) {
        var query = datastore.find(JobProgressionModel.class).filter(Filters.eq("uuid", uuid));
        if(query.count() == 0){
            datastore.save(new JobProgressionModel(uuid, List.of(new JobProgression(job.getId(), xp))));
            return UpdateResult.acknowledged(1, 1L, null);
        }

        var jobProgressionModel = query.first();

        if(jobProgressionModel == null){
            return UpdateResult.unacknowledged();
        }

        if (jobProgressionModel.getJobProgressionList().stream().noneMatch(jobProgression -> jobProgression.getJobId() == job.getId())) {
            return query
                    .update(UpdateOperators.addToSet("jobProgressionList", new JobProgression(job.getId(), xp))).execute();
        }

        jobProgressionModel.getJobProgressionList().forEach(jobProgression -> {
            if(jobProgression.getJobId() == job.getId()){
                jobProgression.setXp(jobProgression.getXp() + xp);
            }
        });

        datastore.merge(jobProgressionModel);
        return UpdateResult.acknowledged(1, 1L, null);
    }
}
