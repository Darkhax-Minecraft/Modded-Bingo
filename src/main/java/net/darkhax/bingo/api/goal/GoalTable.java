package net.darkhax.bingo.api.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class GoalTable {

    private final String name;
    private final Map<String, GoalTier> tiersByName;
    private final List<GoalTier> tiers;

    public GoalTable (String name) {

        this.name = name;
        this.tiers = new ArrayList<>();
        this.tiersByName = new HashMap<>();
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
        this.tiersByName.put(name, tier);
        return tier;
    }
    
    public GoalTier getTierByName(String name) {
        
        return this.tiersByName.get(name);
    }
}