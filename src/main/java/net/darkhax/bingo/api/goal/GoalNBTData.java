package net.darkhax.bingo.api.goal;

import com.google.gson.annotations.Expose;

/**
 * Represents a tier of goals that can appear.
 */
public class GoalNBTData {

    @Expose
    private String key;

    @Expose
    private String type;

    @Expose
    private String value;

    @Expose
    private byte bvalue;

    @Expose
    private long lvalue;

    @Expose
    private short svalue;

    public String getValue () {
        return this.value;
    }

    public byte getBValue () {
        return this.bvalue;
    }

    public long getLValue () {
        return this.lvalue;
    }

    public short getSValue () {
        return this.svalue;
    }

    public String getKey () {
        return this.key;
    }

    public String getType () {
        return this.type;
    }
}
