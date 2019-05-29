package net.darkhax.bingo.network;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This packet is used to serialize the game state to the client.
 */
public class PacketSyncGameState extends SerializableMessage {

    public NBTTagCompound gameState;

    public PacketSyncGameState () {

    }

    public PacketSyncGameState (NBTTagCompound gameState) {

        this.gameState = gameState;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        Minecraft.getMinecraft().addScheduledTask( () -> BingoAPI.GAME_STATE.read(this.gameState));
        return null;
    }
}