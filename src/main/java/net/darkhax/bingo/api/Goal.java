package net.darkhax.bingo.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class Goal extends WeightedRandom.Item {

	private ItemStack targetItem;
	
	public Goal(ItemStack targetItem, int weight) {
		
		super(weight);
		this.targetItem = targetItem;
		
		if (targetItem.isEmpty()) {
			
			throw new IllegalArgumentException("Item was air, this is not allowed!");
		}
	}
	
	public ItemStack getTarget() {
		
		return targetItem;
	}
	
	@Override
	public int hashCode() {
		
		return this.targetItem.hashCode();
	}
}