package net.darkhax.bingo.api.goal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class Goal extends WeightedRandom.Item {

    private final ItemStack targetItem;
    private final String name;
    private final String tier;

    public Goal (String table, String name, ItemStack targetItem, int weight) {

        super(weight);
        this.targetItem = targetItem;
        this.name = name;
        this.tier = table;
        
        if (targetItem.isEmpty()) {

            throw new IllegalArgumentException("Item was air, this is not allowed!");
        }
    }

    public ItemStack getTarget () {

        return this.targetItem;
    }

    public String getTier() {
        
        return this.tier;
    }
    
    public String getName() {
        
        return this.name;
    }
    
    @Override
    public int hashCode () {

        return this.name.hashCode();
    }
}