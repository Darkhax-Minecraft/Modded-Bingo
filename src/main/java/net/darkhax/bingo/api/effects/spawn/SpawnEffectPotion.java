package net.darkhax.bingo.api.effects.spawn;

import com.google.gson.annotations.Expose;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class SpawnEffectPotion extends SpawnEffect {

    @Expose
    private Potion effect;

    @Expose
    private int duration;

    @Expose
    private int amplifier;

    @Override
    public void onPlayerSpawn (EntityPlayerMP player, BlockPos pos) {

        player.addPotionEffect(new PotionEffect(this.effect, this.duration, this.amplifier));
    }
}