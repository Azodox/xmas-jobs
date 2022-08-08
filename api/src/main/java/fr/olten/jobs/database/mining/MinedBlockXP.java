package fr.olten.jobs.database.mining;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

/**
 * @author Azodox_ (Luke)
 * 4/8/2022.
 */

@Entity(value = "jobs_mined_blocks", discriminator = "mined_block")
@Indexes({
        @Index(fields = @Field("minecraft_tag"))
})
public class MinedBlockXP {

    @Id
    private ObjectId _id;

    @Property("minecraft_tag")
    private @Getter final String minecraftTag;

    private @Getter final double earnedJobXP;
    private @Getter final double earnedGeneralXP;
    private @Getter final double earnedFlc;

    public MinedBlockXP(String minecraftTag, double earnedJobXP, double earnedGeneralXP, double earnedFlc) {
        this.minecraftTag = minecraftTag;
        this.earnedJobXP = earnedJobXP;
        this.earnedGeneralXP = earnedGeneralXP;
        this.earnedFlc = earnedFlc;
    }
}
