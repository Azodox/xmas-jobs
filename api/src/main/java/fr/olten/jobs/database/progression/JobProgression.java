package fr.olten.jobs.database.progression;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

/**
 * @author Azodox_ (Luke)
 * 28/7/2022.
 */

@Entity(value = "progression", discriminator = "progression")
public class JobProgression {

    @Id
    private ObjectId id;
    @Property
    private @Getter
    final int jobId;
    @Property
    private @Getter @Setter double xp;

    public JobProgression(int jobId, double xp) {
        this.jobId = jobId;
        this.xp = xp;
    }
}