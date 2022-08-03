package fr.olten.jobs.database.progression;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

/**
 * @author Azodox_ (Luke)
 * 27/7/2022.
 */

@Entity(value = "jobs_progressions", discriminator = "progressions")
@Indexes({
        @Index(fields = @Field("uuid"), options = @IndexOptions(unique = true)),
})
public class JobProgressionModel {

    @Id
    private ObjectId id;
    @Property
    private @Getter final UUID uuid;
    private final @Getter List<JobProgression> jobProgressionList;

    public JobProgressionModel(UUID uuid, List<JobProgression> jobProgressionList) {
        this.uuid = uuid;
        this.jobProgressionList = jobProgressionList;
    }
}
