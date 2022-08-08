package fr.olten.jobs.bossbar;

import lombok.Data;
import org.bukkit.boss.BossBar;

/**
 * @author Azodox_ (Luke)
 * 5/8/2022.
 */

@Data
public class EarnedXPBossBar {

    private long lastUpdate;
    private BossBar bar;

    public EarnedXPBossBar(long lastUpdate, BossBar bar) {
        this.lastUpdate = lastUpdate;
        this.bar = bar;
    }
}
