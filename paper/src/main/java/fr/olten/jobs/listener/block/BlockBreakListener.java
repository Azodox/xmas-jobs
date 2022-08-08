package fr.olten.jobs.listener.block;

import fr.olten.jobs.Job;
import fr.olten.jobs.JobPlugin;
import fr.olten.jobs.bossbar.EarnedXPBossBar;
import net.valneas.account.AccountSystemApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.regex.Pattern;


/**
 * @author Azodox_ (Luke)
 * 4/8/2022.
 */

public class BlockBreakListener implements Listener {

    private final JobPlugin jobPlugin;
    public BlockBreakListener(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        var block = event.getBlock();
        var player = event.getPlayer();
        var metadata = block.getMetadata("PLACED");

        if(!player.getGameMode().equals(GameMode.SURVIVAL)) return;
        if(block.getDrops(player.getInventory().getItemInMainHand()).size() == 0) return;
        if(metadata.stream().anyMatch(MetadataValue::asBoolean)) return;

        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider == null) return;

        var api = provider.getProvider();
        if (api.getAccountManager(player).getAccount().getCurrentJobId() != Job.MINER.getId()) return;

        var minedBlockXP = jobPlugin.getJobDatabaseManager().getMinedBlockXP(block.getType().getKey().asString());
        if(minedBlockXP == null) return;

        var jedis = jobPlugin.getJedisPool().getResource();
        var jobXpEarned = jedis.hget("player#" + player.getUniqueId(), "job#" + Job.MINER.getId() + "#xpEarned");

        if(jobXpEarned != null){
            jedis.hset("player#" + player.getUniqueId(), "job#" + Job.MINER.getId() + "#xpEarned", String.valueOf(
                    Double.parseDouble(jobXpEarned) + minedBlockXP.getEarnedJobXP()
            ));
        }else{
            jedis.hset("player#" + player.getUniqueId(), "job#" + Job.MINER.getId() + "#xpEarned", String.valueOf(minedBlockXP.getEarnedJobXP()));
        }

        var globalEarned = jedis.hget("player#" + player.getUniqueId(), "global#xpEarned");
        if(globalEarned != null){
            jedis.hset("player#" + player.getUniqueId(), "global#xpEarned", String.valueOf(
                    Double.parseDouble(globalEarned) + minedBlockXP.getEarnedGeneralXP()
            ));
        }else{
            jedis.hset("player#" + player.getUniqueId(), "global#xpEarned", String.valueOf(minedBlockXP.getEarnedGeneralXP()));
        }

        var flcEarned = jedis.hget("player#" + player.getUniqueId(), "global#flcEarned");
        if(flcEarned != null) {
            jedis.hset("player#" + player.getUniqueId(), "global#flcEarned", String.valueOf(
                    Double.parseDouble(flcEarned) + minedBlockXP.getEarnedFlc()
            ));
        } else {
            jedis.hset("player#" + player.getUniqueId(), "global#flcEarned", String.valueOf(minedBlockXP.getEarnedFlc()));
        }

        jedis.close();

        if(!jobPlugin.getEarnedXPBars().containsKey(player.getUniqueId())){
            var bar = Bukkit.createBossBar(
                    "+ " + minedBlockXP.getEarnedJobXP() + " XP pour le job de Mineur"
                            + " + " + minedBlockXP.getEarnedGeneralXP() + " XP"
                            + " + " + minedBlockXP.getEarnedFlc() + " Flc",
                    BarColor.PURPLE, BarStyle.SOLID
            );

            bar.setProgress(0.0f);
            bar.addPlayer(player);
            var bossBar = new EarnedXPBossBar(System.currentTimeMillis(), bar);
            jobPlugin.getEarnedXPBars().put(player.getUniqueId(), bossBar);
        }else{
            var bossBar = jobPlugin.getEarnedXPBars().get(player.getUniqueId());
            var bar = bossBar.getBar();
            double[] doubles = new double[] {minedBlockXP.getEarnedJobXP(), minedBlockXP.getEarnedGeneralXP(), minedBlockXP.getEarnedFlc()};

            var title = bar.getTitle();
            var pattern = Pattern.compile("([0-9]+).([0-9]+)");
            var matcher = pattern.matcher(title);
            var sb = new StringBuilder();

            int index = 0;
            while (matcher.find()) {
                var parseDouble = Double.parseDouble(matcher.group());
                matcher.appendReplacement(sb, String.valueOf(parseDouble + doubles[index]));
                index++;
            }

            matcher.appendTail(sb);
            bar.setProgress(0.0f);
            bar.setTitle(sb.toString());
            bar.addPlayer(player);

            bossBar.setBar(bar);
            bossBar.setLastUpdate(System.currentTimeMillis());
        }
    }
}
