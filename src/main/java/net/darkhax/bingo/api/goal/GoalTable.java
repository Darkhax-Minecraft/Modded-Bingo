package net.darkhax.bingo.api.goal;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a table of goals that players may have to collect.
 */
public class GoalTable {

    /**
     * The id for the table.
     */
    @Expose
    private ResourceLocation name;

    /**
     * The tiers of goals that can be selected from.
     */
    @Expose
    private List<GoalTier> tiers;

    public ResourceLocation getName () {

        return this.name;
    }

    /**
     * Selects a random tier from the list.
     *
     * @param rand An instance of random.
     * @return A random goal tier.
     */
    public GoalTier getRandomTier (Random rand) {

        return WeightedObject.getRandomItem(rand, this.tiers);
    }
}