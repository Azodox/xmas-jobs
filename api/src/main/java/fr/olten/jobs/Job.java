package fr.olten.jobs;

import lombok.Getter;

import java.util.Optional;

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

    private @Getter final int id;

    Job(int id) {
        this.id = id;
    }

    public static Optional<Job> getJob(int id){
        for(Job job : Job.values()){
            if(job.getId() == id){
                return Optional.of(job);
            }
        }
        return Optional.empty();
    }
}
