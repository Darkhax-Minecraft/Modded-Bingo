package net.darkhax.bingo.api.goal;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;
import net.minecraft.util.ResourceLocation;

public class GoalTable {

    @Expose
    private ResourceLocation name;

    @Expose
    private List<GoalTier> tiers;

    public ResourceLocation getName () {

        return this.name;
    }

    public GoalTier getRandomTier (Random rand) {

        return WeightedObject.getRandomItem(rand, this.tiers);
    }
}