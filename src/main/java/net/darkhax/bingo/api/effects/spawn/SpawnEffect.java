package net.darkhax.bingo.api.effects.spawn;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public abstract class SpawnEffect {
    
    public abstract void onPlayerSpawn(EntityPlayerMP player, BlockPos pos);
}
