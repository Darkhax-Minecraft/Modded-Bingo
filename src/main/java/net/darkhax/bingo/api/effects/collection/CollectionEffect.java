package net.darkhax.bingo.api.effects.collection;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Parent class for collection bingo effects. These are used to apply code when a player
 * obtains an item that will complete a goal on the bingo board.
 */
public abstract class CollectionEffect {

    /**
     * Called when a player picks up an item that will complete a goal on the bingo board.
     *
     * @param player The player who picked up the item.
     * @param item The item that the player picked up.
     * @param team The team that the player is on.
     */
    public abstract void onItemCollected (ServerPlayerEntity player, ItemStack item, Team team);
}