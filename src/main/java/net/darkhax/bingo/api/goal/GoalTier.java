package net.darkhax.bingo.api.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class GoalTier extends WeightedRandom.Item {

    private final String name;
    private final List<Goal> goals;
    private final Map<String, Goal> goalsByName;

    public GoalTier (String name, int weight) {

        super(weight);
        this.name = name;
        this.goals = new ArrayList<>();
        this.goalsByName = new HashMap<>();
    }

    public Goal createGoal (String name, ItemStack target, int weight) {

        final Goal goal = new Goal(this.name, name, target, weight);
        this.goals.add(goal);
        this.goalsByName.put(goal.getName(), goal);
        return goal;
    }

    public Goal getGoalByName (String name) {

        return this.goalsByName.get(name);
    }

    public List<Goal> getGoals () {

        return this.goals;
    }

    public String getName () {

        return this.name;
    }

    public Goal getRandomGoal (Random rand) {

        return WeightedRandom.getRandomItem(rand, this.goals);
    }
}