package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class EndingEffectAnnounce extends EndingEffect {

    @Override
    public void onGameCompleted (MinecraftServer server, Team winningTeam) {

        server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.winner", winningTeam.getTeamName()));
    }
}