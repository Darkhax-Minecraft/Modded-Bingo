package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.server.MinecraftServer;

/**
 * Parent class for game win bingo effects. These are used to apply code when a game has been
 * won.
 */
public abstract class GameWinEffect {

    /**
     * Called when the game has been won.
     *
     * @param server An instance of the game server.
     * @param winningTeam The team that won the game.
     */
    public abstract void onGameCompleted (MinecraftServer server, Team winningTeam, boolean bingoWon);
}
