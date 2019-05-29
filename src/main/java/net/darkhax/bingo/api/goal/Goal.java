package net.darkhax.bingo.api.goal;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.data.WeightedObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Goal extends WeightedObject {

    @Expose
    private Item item;
    
    @Expose
    private int meta;

    public Goal (int itemWeightIn) {
        
        super(itemWeightIn);
    }
    
    public ItemStack getTarget () {

        return new ItemStack(item, 1, meta);
    }

    @Override
    public int hashCode () {
        
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + meta;
        return result;
    }

    @Override
    public boolean equals (Object obj) {
        
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Goal other = (Goal) obj;
        if (item == null) {
            if (other.item != null)
                return false;
        }
        else if (!item.equals(other.item))
            return false;
        if (meta != other.meta)
            return false;
        return true;
    }
}