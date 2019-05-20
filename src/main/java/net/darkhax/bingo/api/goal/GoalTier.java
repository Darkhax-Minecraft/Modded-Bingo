package net.darkhax.bingo.api.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class GoalTier extends WeightedRandom.Item {

    private final String name;
    private final List<Goal> goals;

    public GoalTier (String name, int weight) {

        super(weight);
        this.name = name;
        this.goals = new ArrayList<>();
    }

    public void add (Goal goal) {

        this.goals.add(goal);
    }

    public List<Goal> getGoals () {

        return this.goals;
    }

    public Goal getRandomGoal (Random rand) {

        return WeightedRandom.getRandomItem(rand, this.goals);
    }
}