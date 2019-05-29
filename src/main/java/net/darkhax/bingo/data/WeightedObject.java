package net.darkhax.bingo.data;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

public class WeightedObject {

    @Expose
    private final int weight;

    public WeightedObject (int itemWeightIn) {

        this.weight = itemWeightIn;
    }

    public int getWeight () {

        return this.weight;
    }

    public static <T extends WeightedObject> T getRandomItem (Random random, List<T> list) {

        int totalWeight = 0;

        int i = 0;

        for (final T entry : list) {

            totalWeight += entry.getWeight();
        }

        int weight = random.nextInt(totalWeight);

        for (final int j = list.size(); i < j; ++i) {

            final T t = list.get(i);

            weight -= t.getWeight();

            if (weight < 0) {

                return t;
            }
        }

        return null;
    }
}
