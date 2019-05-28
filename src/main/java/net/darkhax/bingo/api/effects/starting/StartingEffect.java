package net.darkhax.bingo.api.effects.starting;

import net.minecraft.server.MinecraftServer;

public abstract class StartingEffect {
    
    public abstract void onGameStarted(MinecraftServer server);
}
