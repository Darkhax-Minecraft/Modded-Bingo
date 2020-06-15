package net.darkhax.bingo.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

/**
 * This class is responsible for handling all save data that is persistent in the world.
 */
public class BingoPersistantData {

    /**
     * A map of player UUIDs to their teams.
     */
    private static final Map<UUID, Team> PLAYER_TEAMS = new HashMap<>();

    /**
     * Gets the team a player is on. Players without a team default to the red team.
     *
     * @param player The player to lookup.
     * @return The team the player is on.
     */
    public static Team getTeam (PlayerEntity player) {

        return PLAYER_TEAMS.getOrDefault(player.getUniqueID(), BingoAPI.TEAM_RED);
    }

    /**
     * Sets the team for a player.
     *
     * @param player The player to set the team for.
     * @param team The team they are now on.
     */
    public static void setTeam (PlayerEntity player, Team team) {

        PLAYER_TEAMS.put(player.getUniqueID(), team);
    }
    
    /**
     * Writes all of the data to the provided PacketBuffer
     * 
     * @param buffer The PacketBuffer to write the game state to.
     */
    public static void write(PacketBuffer buffer) {
    	buffer.writeVarInt(PLAYER_TEAMS.entrySet().size());
    	for (final Entry<UUID, Team> entry : PLAYER_TEAMS.entrySet()) {
    		buffer.writeUniqueId(entry.getKey());
    		buffer.writeString(entry.getValue().getDyeColor().getTranslationKey());
        }
    	BingoAPI.GAME_STATE.write(buffer);
    }
    
    /**
     * Reads data from a PacketBuffer.
     * 
     * @param buffer The PacketBuffer to read from.
     */
    public static void read(PacketBuffer buffer) {
    	PLAYER_TEAMS.clear();
    	int numPlayer = buffer.readVarInt();
    	for(int i = 0; i < numPlayer; i++) {
    		final UUID uuid = buffer.readUniqueId();
    		final Team team = Team.getTeamByName(buffer.readString(30));
    		if(team != null) {
    			PLAYER_TEAMS.put(uuid, team);
    		}
    	}
    	BingoAPI.GAME_STATE.read(buffer);
    }

}
