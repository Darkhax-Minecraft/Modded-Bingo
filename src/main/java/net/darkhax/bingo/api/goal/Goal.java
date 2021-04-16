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


    @Expose
    private List<GoalNBTData> dnbt;

    public ItemStack getTarget (boolean display) {

        final ItemStack stack = new ItemStack(this.item, 1, this.meta);

        if ((this.nbt != null) || (display && this.dnbt != null)) {
          NBTTagCompound nbtCompound = new NBTTagCompound();
          stack.setTagCompound(nbtCompound);
          if (this.nbt != null) {
            for (final GoalNBTData data : this.nbt) {
              this.addNbtToCompound(nbtCompound, data);
            }
          }
          if (display && this.dnbt != null) {
            for (final GoalNBTData data : this.dnbt) {
              this.addNbtToCompound(nbtCompound, data);
            }
          }
        }

        return stack;
    }

    public void addNbtToCompound (NBTTagCompound nbtCompound, GoalNBTData data) {
      if (data.getType() != null) {
        switch (data.getType()) {
          case "b":
            nbtCompound.setByte(data.getKey(),data.getBValue());
            break;
          case "l":
            nbtCompound.setLong(data.getKey(),data.getLValue());
            break;
          case "s":
            nbtCompound.setShort(data.getKey(),data.getSValue());
            break;
          default:
            nbtCompound.setString(data.getKey(),data.getValue());
        }
      } else {
        nbtCompound.setString(data.getKey(),data.getValue());
      }
    }

    @Override
    public int hashCode () {

        final int prime = 31;
        int result = 1;
        result = prime * result + (this.item == null ? 0 : this.item.hashCode());
        result = prime * result + this.meta;
        if (this.nbt != null) {
          for (final GoalNBTData data : this.nbt) {
            result = prime * result + (data.getValue().hashCode() + 3*data.getKey().hashCode());
          }
        }
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
        if (this.nbt != null) {
          if (other.nbt != null) {
            if (this.nbt.equals(other.nbt)) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          if (other.nbt != null) {
            return false;
          } else {
            return true;
          }
        }
    }
}
