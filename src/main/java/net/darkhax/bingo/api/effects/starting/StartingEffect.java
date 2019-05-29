package net.darkhax.bingo.api.effects.starting;

import net.minecraft.server.MinecraftServer;

/**
 * Parent class for bingo game starting effects. These are used to apply code when a game is
 * started.
 */
public abstract class StartingEffect {

    /**
     * Called when the game is started.
     *
     * @param server An instance of the server.
     */
    public abstract void onGameStarted (MinecraftServer server);
}
