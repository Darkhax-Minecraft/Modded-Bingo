package net.darkhax.bingo.api.goal;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;

/**
 * Represents a tier of goals that can appear.
 */
public class GoalTier extends WeightedObject {

    /**
     * The ID of the goal tier.
     */
    @Expose
    private String name;

    /**
     * A list of all the potential goals.
     */
    @Expose
    private List<Goal> goals;

    /**
     * Gets the name of this tier.
     *
     * @return The tier name.
     */
    public String getName () {

        return this.name;
    }

    /**
     * Gets a random goal from the tier.
     *
     * @param rand An instance of Random.
     * @return A randomly selected goal.
     */
    public Goal getRandomGoal (Random rand) {

        return WeightedObject.getRandomItem(rand, this.goals);
    }
}