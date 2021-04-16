package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * This effect announces when a player or team has completed the goals required to win the
 * game.
 */
public class EndingEffectAnnounce extends GameWinEffect {

    @Override
    public void onGameCompleted (MinecraftServer server, Team winningTeam, boolean bingoWon) {
      server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.empty"));
      if (bingoWon) {
        server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.empty"));
        server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.winner", winningTeam.getTeamName()));
        server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.empty"));
      } else {
        server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.ender", winningTeam.getTeamName()));
      }
      server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.empty"));
    }
}
