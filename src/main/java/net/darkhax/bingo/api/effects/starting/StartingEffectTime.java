package net.darkhax.bingo.api.effects.starting;

import com.google.gson.annotations.Expose;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

/**
 * This effect will change the world time when the game starts.
 */
public class StartingEffectTime extends StartingEffect {

    /**
     * The time to set the world to.
     */
    @Expose
    private long time;

    @Override
    public void onGameStarted (MinecraftServer server) {

        for (final WorldServer world : server.worlds) {

            world.setWorldTime(this.time);
        }
    }
}