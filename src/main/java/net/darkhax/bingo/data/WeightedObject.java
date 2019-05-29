package net.darkhax.bingo.data;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

/**
 * Represents a weighted object that can be (de)serialized.
 */
public class WeightedObject {

    /**
     * The weight of the object.
     */
    @Expose
    private int weight;

    /**
     * Gets the weight of the object.
     *
     * @return The weight of the object.
     */
    public int getWeight () {

        return this.weight;
    }

    /**
     * Selects a random instance from a list of weighted objects based on each of their weight.
     *
     * @param random An instance of random.
     * @param list A list of all the potential outcomes.
     * @return A random outcome selected by weight.
     */
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
