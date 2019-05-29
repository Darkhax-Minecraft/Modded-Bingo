package net.darkhax.bingo.api.effects.ending;

import net.darkhax.bingo.api.team.Team;
import net.minecraft.server.MinecraftServer;

public abstract class EndingEffect {

    public abstract void onGameCompleted (MinecraftServer server, Team winningTeam);
}