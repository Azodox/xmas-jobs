package fr.olten.jobs.bossbar;

import fr.olten.jobs.JobPlugin;

/**
 * @author Azodox_ (Luke)
 * 5/8/2022.
 */

public class BossBarCheck implements Runnable{

    private final JobPlugin jobPlugin;

    public BossBarCheck(JobPlugin jobPlugin) {
        this.jobPlugin = jobPlugin;
    }

    @Override
    public void run() {
        if (jobPlugin.getEarnedXPBars().isEmpty()) {
            return;
        }

        var iterator = jobPlugin.getEarnedXPBars().values().iterator();
        while (iterator.hasNext()) {
            var bar = iterator.next();
            if(System.currentTimeMillis() - bar.getLastUpdate() > 5000){
                bar.getBar().removeAll();
                iterator.remove();
            }
        }
    }
}
