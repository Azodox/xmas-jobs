package fr.olten.jobs;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Azodox_ (Luke)
 * 22/7/2022.
 */

public enum Job {

    MINER(0),
    LUMBERJACK(1),
    EXPLORER(2),
    MERCHANT(3),
    CHEF(4),
    ARTISAN(5),
    BUTCHER(6);

    private static final Map<Integer, Job> BY_ID = new HashMap<>();
    private @Getter final int id;

    static {
        for (Job job : values()) {
            BY_ID.put(job.id, job);
        }
    }

    Job(int id) {
        this.id = id;
    }

    public static Job getJob(int id){
        return BY_ID.get(id);
    }
}
