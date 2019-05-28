package net.darkhax.bingo.api.effects.spawn;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class SpawnEffectMovePlayer extends SpawnEffect {

    @Expose
    private boolean setSpawn;
    
    @Expose
    private Block fallbackBlock;
    
    @Expose
    private int fallbackMeta;
    
    @Override
    public void onPlayerSpawn (EntityPlayerMP player, BlockPos pos) {

        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        
        if (setSpawn) {
            
            player.setSpawnChunk(pos, true, player.dimension);
        }
        
        if (fallbackBlock != null && player.world.isAirBlock(pos.down())) {
            
            player.world.setBlockState(pos.down(), this.fallbackBlock.getStateFromMeta(this.fallbackMeta));
        }
    }
}