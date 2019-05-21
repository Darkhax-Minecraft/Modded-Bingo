package net.darkhax.bingo.network;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncGameState extends SerializableMessage {

    public NBTTagCompound gameState;
    
    public PacketSyncGameState() {
        
    }
    
    public PacketSyncGameState(NBTTagCompound gameState) {
        
        this.gameState = gameState;
    }
    
    @Override
    public IMessage handleMessage (MessageContext context) {
        
        Minecraft.getMinecraft().addScheduledTask(() -> BingoMod.GAME_STATE.read(this.gameState));
        return null;
    }    
}