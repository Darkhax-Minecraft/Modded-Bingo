package net.darkhax.bingo.api.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class GoalTable {

    private final String name;
    private final List<GoalTier> tiers;

    public GoalTable (String name) {

        this.name = name;
        this.tiers = new ArrayList<>();
    }

    public String getName () {

        return this.name;
    }

    public List<GoalTier> getTiers () {

        return this.tiers;
    }

    public GoalTier getRandomTier (Random rand) {

        return WeightedRandom.getRandomItem(rand, this.tiers);
    }

    public GoalTier createTier (String name, int weight) {

        final GoalTier tier = new GoalTier(name, weight);
        this.tiers.add(tier);
        return tier;
    }
}