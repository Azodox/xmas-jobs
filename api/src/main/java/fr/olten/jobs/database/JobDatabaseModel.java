package fr.olten.jobs.database;

import dev.morphia.annotations.*;
import fr.olten.jobs.power.JobPower;
import lombok.Data;
import lombok.Getter;
import org.bson.types.ObjectId;

/**
 * @author Azodox_ (Luke)
 * 22/7/2022.
 */

@Entity(value = "jobs", discriminator = "job")
@Indexes({
        @Index(fields = @Field("id"))
})
@Data
public abstract class JobDatabaseModel {

    @Id
    private ObjectId _id;
    private @Getter final int id;
    private @Getter final String name;
    private @Getter final String description;
    private @Getter final String icon;
    private @Getter final String prefix;
    private final int powerId;

    public JobDatabaseModel(int id, String name, String description, String icon, String prefix, int powerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.prefix = prefix;
        this.powerId = powerId;
    }

    public JobPower getPower(){
        return JobPower.getPower(this.powerId);
    }
}
