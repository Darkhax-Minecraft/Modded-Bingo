package net.darkhax.bingo.api.goal;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Represents a goal that players may have to complete.
 */
public class Goal extends WeightedObject {

    /**
     * The goal item that must be collected.
     */
    @Expose
    private Item item;

    /**
     * The meta for the item.
     */
    @Expose
    private int meta;

    public ItemStack getTarget () {

        return new ItemStack(this.item, 1, this.meta);
    }

    @Override
    public int hashCode () {

        final int prime = 31;
        int result = 1;
        result = prime * result + (this.item == null ? 0 : this.item.hashCode());
        result = prime * result + this.meta;
        return result;
    }

    @Override
    public boolean equals (Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Goal other = (Goal) obj;
        if (this.item == null) {
            if (other.item != null) {
                return false;
            }
        }
        else if (!this.item.equals(other.item)) {
            return false;
        }
        if (this.meta != other.meta) {
            return false;
        }
        return true;
    }
}