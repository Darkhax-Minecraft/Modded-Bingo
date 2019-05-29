package net.darkhax.bingo.api.goal;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;

public class GoalTier extends WeightedObject {

    @Expose
    private String name;

    @Expose
    private List<Goal> goals;

    public GoalTier (int itemWeightIn) {

        super(itemWeightIn);
    }

    public String getName () {

        return this.name;
    }

    public Goal getRandomGoal (Random rand) {

        return WeightedObject.getRandomItem(rand, this.goals);
    }
}