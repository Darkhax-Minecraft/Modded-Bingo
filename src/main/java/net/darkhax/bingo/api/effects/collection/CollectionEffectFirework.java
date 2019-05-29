package net.darkhax.bingo.api.effects.collection;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * This effect spawns a firework on a player when they collect an item that completes something
 * on the bingo board.
 */
public class CollectionEffectFirework extends CollectionEffect {

    @Override
    public void onItemCollected (EntityPlayerMP player, ItemStack item, Team team) {

        team.spawnFirework(player);
    }
}