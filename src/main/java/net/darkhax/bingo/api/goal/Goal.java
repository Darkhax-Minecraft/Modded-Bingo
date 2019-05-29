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

    private ItemStack targetItem;

    public Goal (int itemWeightIn) {
        
        super(itemWeightIn);
    }
    
    public ItemStack getTarget () {

        return this.targetItem;
    }

    @Override
    public int hashCode () {

        return this.targetItem.hashCode();
    }
}