package fr.olten.jobs.database.power;

import dev.morphia.annotations.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.types.ObjectId;

/**
 * @author Azodox_ (Luke)
 * 3/8/2022.
 */

@Entity(value = "powers", discriminator = "power")
@Indexes({
        @Index(fields = @Field("id"), options = @IndexOptions(unique = true))
})
public class JobPowerModel {

    @Id
    private ObjectId _id;
    private @Getter final int id;
    private final String name;
    private @Getter final String description;

    public JobPowerModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Component name(){
        return MiniMessage.miniMessage().deserialize(this.name);
    }
}
