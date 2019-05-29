package net.darkhax.bingo.api.effects.spawn;

import com.google.gson.annotations.Expose;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

/**
 * This effect applies a potion effect to all players when the game has been started.
 */
public class SpawnEffectPotion extends SpawnEffect {

    /**
     * The potion effect to apply.
     */
    @Expose
    private Potion effect;

    /**
     * The duration in ticks.
     */
    @Expose
    private int duration;

    /**
     * An amplifier.
     */
    @Expose
    private int amplifier;

    @Override
    public void onPlayerSpawn (EntityPlayerMP player, BlockPos pos) {

        player.addPotionEffect(new PotionEffect(this.effect, this.duration, this.amplifier));
    }
}