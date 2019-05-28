package net.darkhax.bingo.api.effects.starting;

import com.google.gson.annotations.Expose;

import net.minecraft.server.MinecraftServer;

public class StartingEffectTime extends StartingEffect {
    
    @Expose
    private long time;
    
    @Override
    public void onGameStarted (MinecraftServer server) {
        
        for (int i = 0; i < server.worlds.length; ++i) {
            
            server.worlds[i].setWorldTime(time);
        }
    }
}