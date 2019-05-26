package net.darkhax.bingo.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.team.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class BingoPersistantData {

    private static final Map<UUID, Team> PLAYER_TEAMS = new HashMap<>();

    public static Team getTeam (EntityPlayer player) {

        return PLAYER_TEAMS.getOrDefault(player.getUniqueID(), BingoMod.TEAM_RED);
    }

    public static void setTeam (EntityPlayer player, Team team) {

        PLAYER_TEAMS.put(player.getUniqueID(), team);
    }

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

        saveData.setTag("GameState", BingoMod.GAME_STATE.write());
        return saveData;
    }

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

        BingoMod.GAME_STATE.read(tag.getCompoundTag("GameState"));
    }
}
