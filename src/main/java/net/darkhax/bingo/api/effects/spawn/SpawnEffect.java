package net.darkhax.bingo.api.effects.spawn;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Parent class for bingo player spawn effects. These are used to apply code when the player
 * spawns into the game.
 */
public abstract class SpawnEffect {

    /**
     * Called when the player initially spawns into the game.
     *
     * @param player The player that is spawning.
     * @param pos The randomized position for the player to spawn at.
     */
    public abstract void onPlayerSpawn (ServerPlayerEntity player, BlockPos pos);
}
