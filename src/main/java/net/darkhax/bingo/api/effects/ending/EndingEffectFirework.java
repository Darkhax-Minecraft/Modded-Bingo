package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

/**
 * This effect will spawn a firework on all players when the game has been won. The firework is
 * of the winning team's color.
 */
public class EndingEffectFirework extends GameWinEffect {

    @Override
    public void onGameCompleted (MinecraftServer server, Team winningTeam) {

        for (final ServerPlayerEntity player : server.getPlayerList().getPlayers()) {

            winningTeam.spawnFirework(player);
        }
    }
}