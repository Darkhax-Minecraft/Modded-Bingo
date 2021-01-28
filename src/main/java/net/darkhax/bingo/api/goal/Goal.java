package net.darkhax.bingo.api.goal;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;

import net.darkhax.bingo.ModdedBingo;

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

    /**
     * The nbt for the item.
     */
    @Expose
    private List<GoalNBTData> nbt;

    public ItemStack getTarget () {

        final ItemStack stack = new ItemStack(this.item, 1, this.meta);
        /*
        if (this.nbt != null) {
          CompoundNBT nbtCompound = new CompoundNBT();
          stack.setTag(nbtCompound);

          for (final GoalNBTData data : this.nbt) {
            //nbtCompound.setString(data.getKey(),data.getValue());
            nbtCompound.putString(data.getKey(),data.getValue());
            ModdedBingo.LOG.info("Goal NBT: " + data.getKey() + "  "+data.getValue());
          }
        }
        */
        return stack;
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
