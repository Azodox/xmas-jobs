package fr.olten.jobs.database.mining;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

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
    private @Getter final List<Float> levelMultipliers;

    private final double earnedJobXP;
    private final double earnedGlobalXP;
    private final double earnedFlc;

    public MinedBlockXP(String minecraftTag, List<Float> levelMultipliers, double earnedJobXP, double earnedGlobalXP, double earnedFlc) {
        this.minecraftTag = minecraftTag;
        this.levelMultipliers = levelMultipliers;
        this.earnedJobXP = earnedJobXP;
        this.earnedGlobalXP = earnedGlobalXP;
        this.earnedFlc = earnedFlc;
    }

    public double getEarnedJobXP(int level) {
        return levelMultipliers.size() <= level ?
                levelMultipliers.get(levelMultipliers.size() - 1) * earnedJobXP :
                levelMultipliers.get(level) * earnedJobXP;
    }

    public double getEarnedGlobalXP(int level) {
        return levelMultipliers.size() <= level ?
                levelMultipliers.get(levelMultipliers.size() - 1) * earnedGlobalXP :
                levelMultipliers.get(level) * earnedGlobalXP;

    }

    public double getEarnedFlc(int level) {
        return levelMultipliers.size() <= level ?
                levelMultipliers.get(levelMultipliers.size() - 1) * earnedFlc :
                levelMultipliers.get(level) * earnedFlc;
    }
}
