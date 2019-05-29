package net.darkhax.bingo.api.effects.spawn;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

/**
 * This effect is used to randomize the spawn of all players when the game has started.
 */
public class SpawnEffectMovePlayer extends SpawnEffect {

    /**
     * Whether or not the spawn point should be changed.
     */
    @Expose
    private boolean setSpawn;

    /**
     * A block to place below the player if there is no solid blocks at this position.
     */
    @Expose
    private Block fallbackBlock;

    /**
     * A meta value for the fallback block.
     */
    @Expose
    private int fallbackMeta;

    @Override
    public void onPlayerSpawn (EntityPlayerMP player, BlockPos pos) {

        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        if (this.setSpawn) {

            player.setSpawnChunk(pos, true, player.dimension);
        }

        if (this.fallbackBlock != null && player.world.isAirBlock(pos.down())) {

            player.world.setBlockState(pos.down(), this.fallbackBlock.getStateFromMeta(this.fallbackMeta));
        }
    }
}