package net.darkhax.bingo.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

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
    public static Team getTeam (EntityPlayer player) {

        return PLAYER_TEAMS.getOrDefault(player.getUniqueID(), BingoAPI.TEAM_RED);
    }

    /**
     * Sets the team for a player.
     *
     * @param player The player to set the team for.
     * @param team The team they are now on.
     */
    public static void setTeam (EntityPlayer player, Team team) {

        PLAYER_TEAMS.put(player.getUniqueID(), team);
    }

    /**
     * Writes all of the data to an NBTTagCompound.
     *
     * @return A tag compound containing all the data.
     */
    public static NBTTagCompound write () {

        final NBTTagCompound saveData = new NBTTagCompound();

        final NBTTagList list = new NBTTagList();
        saveData.setTag("TeamData", list);

        for (final Entry<UUID, Team> entry : PLAYER_TEAMS.entrySet()) {

            final NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setUniqueId("PlayerUUID", entry.getKey());
            entryTag.setString("Team", entry.getValue().getDyeColor().getTranslationKey());
            list.appendTag(entryTag);
        }

        saveData.setTag("GameState", BingoAPI.GAME_STATE.write());
        return saveData;
    }

    /**
     * Reads data from an nbt tag compound.
     *
     * @param tag The tag to read from.
     */
    public static void read (NBTTagCompound tag) {

        PLAYER_TEAMS.clear();

        final NBTTagList playersData = tag.getTagList("TeamData", NBT.TAG_COMPOUND);

        for (int i = 0; i < playersData.tagCount(); i++) {

            final NBTTagCompound playerData = playersData.getCompoundTagAt(i);
            final Team team = Team.getTeamByName(playerData.getString("Team"));

            if (team != null) {

                PLAYER_TEAMS.put(playerData.getUniqueId("PlayerUUID"), team);
            }
        }

        BingoAPI.GAME_STATE.read(tag.getCompoundTag("GameState"));
    }
}
