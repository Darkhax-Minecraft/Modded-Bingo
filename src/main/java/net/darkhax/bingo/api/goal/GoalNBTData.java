package net.darkhax.bingo.api.goal;

import com.google.gson.annotations.Expose;

/**
 * Represents a tier of goals that can appear.
 */
public class GoalNBTData {

    @Expose
    private String key;

    @Expose
    private String value;

    public String getValue () {
        return this.value;
    }

    public String getKey () {
        return this.key;
    }
}
