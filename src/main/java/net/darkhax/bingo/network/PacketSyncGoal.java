package net.darkhax.bingo.network;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncGoal extends SerializableMessage {

    public int x;
    public int y;
    public String teamName;
    
    public PacketSyncGoal() {
        
    }
    
    public PacketSyncGoal (int x, int y, String team) {
        
        this.x = x;
        this.y = y;
        this.teamName = team;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {
        
        Minecraft.getMinecraft().addScheduledTask(() -> {
            
            Team team = Team.getTeamByName(this.teamName);
            
            if (team != null) {
                
                BingoMod.GAME_STATE.getCompletionStates()[this.x][this.y][team.getTeamCorner()] = team;
            }
        });
        return null;
    }    
}