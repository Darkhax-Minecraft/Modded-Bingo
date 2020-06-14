package net.darkhax.bingo.network;

import java.util.function.Supplier;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.data.GameState;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * This packet is used to serialize the game state to the client.
 */
public class PacketSyncGameState {
	
	private final GameState gamestate;
	
	public PacketSyncGameState() {
		this.gamestate = BingoAPI.GAME_STATE;
	}
	
	public PacketSyncGameState(PacketBuffer buffer) {
		this.gamestate = new GameState();
		gamestate.read(buffer);
	}
	
	public void encode(PacketBuffer buffer) {
		gamestate.write(buffer);
    }
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		BingoAPI.GAME_STATE = gamestate;
	}
    
}