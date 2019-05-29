package net.darkhax.bingo.network;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This packet is used to sync the state of a specific goal.
 */
public class PacketSyncGoal extends SerializableMessage {

    /**
     * The X pos on the bingo board for the goal that is updated.
     */
    public int x;

    /**
     * The Y pos on the bingo board for the goal that is updated.
     */
    public int y;

    /**
     * The name of the team that completed the goal.
     */
    public String teamName;

    public PacketSyncGoal () {

    }

    public PacketSyncGoal (int x, int y, String team) {

        this.x = x;
        this.y = y;
        this.teamName = team;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        Minecraft.getMinecraft().addScheduledTask( () -> {

            final Team team = Team.getTeamByName(this.teamName);

            if (team != null) {

                BingoAPI.GAME_STATE.getCompletionStates()[this.x][this.y][team.getTeamCorner()] = team;
            }
        });
        return null;
    }
}