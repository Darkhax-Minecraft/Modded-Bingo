package net.darkhax.bingo.api.effects.spawn;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * This effect will clear the inventory of the player before the game starts.
 */
public class SpawnEffectClearInventory extends SpawnEffect {

    @Override
    public void onPlayerSpawn (EntityPlayerMP player, BlockPos pos) {

        for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
            
            player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
        }
    }
}