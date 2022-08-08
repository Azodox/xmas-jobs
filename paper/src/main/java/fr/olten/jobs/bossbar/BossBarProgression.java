package fr.olten.jobs.bossbar;

import fr.olten.jobs.JobPlugin;

/**
 * @author Azodox_ (Luke)
 * 5/8/2022.
 */

public class BossBarProgression implements Runnable{

    private final JobPlugin jobPlugin;

    public BossBarProgression(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }


    @Override
    public void run() {
        if (jobPlugin.getEarnedXPBars().isEmpty()) {
            return;
        }

        jobPlugin.getEarnedXPBars().values().stream().map(EarnedXPBossBar::getBar).forEach(bar -> {
            if(bar.getProgress() + 0.01 > 1){
                bar.setProgress(bar.getProgress() + (1 - bar.getProgress()));
                return;
            }

            bar.setProgress(bar.getProgress() + 0.01f);
        });
    }
}
