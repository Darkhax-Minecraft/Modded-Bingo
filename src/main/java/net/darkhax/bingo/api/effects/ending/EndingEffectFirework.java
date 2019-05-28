package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class EndingEffectFirework extends EndingEffect {
    
    @Override
    public void onGameCompleted (MinecraftServer server, Team winningTeam) {
        
        for (final EntityPlayerMP player : server.getPlayerList().getPlayers()) {

            winningTeam.spawnFirework(player);
        }
    } 
}