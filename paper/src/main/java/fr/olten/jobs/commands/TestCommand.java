package fr.olten.jobs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.olten.jobs.Job;
import fr.olten.jobs.JobPlugin;
import fr.olten.jobs.database.power.JobPowerModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.valneas.account.AccountSystemApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * @author Azodox_ (Luke)
 * 2/8/2022.
 */

@CommandAlias("test")
public class TestCommand extends BaseCommand {

    private final JobPlugin jobPlugin;
    public TestCommand(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @Default
    public void onTest(Player player, int jobId){
        player.sendMessage(Component.text(jobPlugin.getJobDatabaseManager().setJob(player.getUniqueId(), Job.getJob(jobId).orElseThrow()).getModifiedCount()));
    }

    @Subcommand("add xp")
    public void onAddXp(Player player, int jobId, double xp){
        if(Job.getJob(jobId).isEmpty()){
            player.sendMessage(
                    Component.text("JOB").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                    .append(Component.text(" » ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD))
                            .append(Component.text("Impossible de trouver le job")).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
            return;
        }

        player.sendMessage(Component.text(jobPlugin.getJobDatabaseManager()
                .incrementJobProgression(player.getUniqueId(), Job.getJob(jobId).orElseThrow(), xp).getModifiedCount()));
    }

    @Subcommand("get power")
    public void onGetPower(Player player){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider == null){
            System.out.println("Provider is null");
            return;
        }

        var api = provider.getProvider();
        var accountManager = api.getAccountManager(player);
        var job = Job.getJob(accountManager.getAccount().getCurrentJobId());
        if(job.isPresent()){
            var playerJob = jobPlugin.getJobDatabaseManager().queryPlayerJob(player.getUniqueId(), job.get()).first();
            if(playerJob == null){
                System.out.println("Player job is null");
                return;
            }

            player.sendMessage(jobPlugin.getJobDatabaseManager().queryJobPower(playerJob.getPowerId()).first().name());
        }else{
            player.sendMessage(
                    Component.text("JOB").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                            .append(Component.text(" » ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD))
                            .append(Component.text("Vous n'avez pas de métier")).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
        }
    }

    @Subcommand("add power")
    public void addPower(Player player, int powerId){
        var model = new JobPowerModel(powerId, "Nullam a dui orci. Maecenas molestie, erat a dictum blandit, risus nulla", "Ut eget ipsum vel arcu blandit pretium sit amet nec mi. Pellentesque in erat");
        //TODO : save power in the database
    }
}
