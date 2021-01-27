package net.darkhax.bingo.api.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Represents a team in the game.
 */
public class Team {

    private boolean finished = false;

    /**
     * A mapping of all teams by their name. This is used to (de)serialize them later.
     */
    private static final Map<String, Team> TEAMS_BY_NAME = new HashMap<>();

    /**
     * A list of all the team names. This is used to automate tab completion.
     */
    private static final List<String> TEAM_NAMES = new ArrayList<>();

    /**
     * The text format for the team.
     */
    private final TextFormatting teamColorText;

    /**
     * The corner of the board that this team is drawn to.
     */
    private final int teamCorner;

    /**
     * The dye color associated with the team.
     */
    private final EnumDyeColor dyeColor;

    /**
     * A firework item that has the team color.
     */
    private ItemStack fireworStack;

    /**
     * A text component of the team name.
     */
    private final ITextComponent teamName;

    /**
     * A packed RGB int containing the color code of the team.
     */
    private final int color;

    public Team (TextFormatting teamColorText, int teamCorner, EnumDyeColor dyeColor) {

        this.teamColorText = teamColorText;
        this.teamCorner = teamCorner;
        this.dyeColor = dyeColor;
        TEAMS_BY_NAME.put(dyeColor.getTranslationKey(), this);
        TEAM_NAMES.add(dyeColor.getTranslationKey());
        this.teamName = new TextComponentString(dyeColor.getTranslationKey());
        this.teamName.getStyle().setColor(this.getTeamColorText());
        this.color = ObfuscationReflectionHelper.getPrivateValue(EnumDyeColor.class, this.dyeColor, "field_193351_w");
    }

    /**
     * Gets the text formatting for this team.
     *
     * @return The text formatting for this team.
     */
    public TextFormatting getTeamColorText () {

        return this.teamColorText;
    }

    /**
     * Gets the corner the team is drawn to.
     *
     * @return The corner the team is drawn to.
     */
    public int getTeamCorner () {

        return this.teamCorner;
    }

    public boolean getFinishState () {
      return this.finished;
    }

    public void setFinishState (boolean state) {
      this.finished = state;
    }

    /**
     * Gets the dye color for the team.
     *
     * @return The dye color linked to the team.
     */
    public EnumDyeColor getDyeColor () {

        return this.dyeColor;
    }

    /**
     * A text component containing the team's name.
     *
     * @return The team's name as a text component.
     */
    public ITextComponent getTeamName () {

        return this.teamName;
    }

    /**
     * Gets the raw team name. Used for (de)serialization.
     *
     * @return The team's raw name.
     */
    public String getTeamKey () {

        return this.getDyeColor().getTranslationKey();
    }

    /**
     * Generates a firework itemstack with the nbt matching the team.
     *
     * @return A firework matching the team's info.
     */
    public ItemStack getFireworStack () {

        if (this.fireworStack == null || this.fireworStack.isEmpty()) {

            this.fireworStack = new ItemStack(Items.FIREWORKS);
            final NBTTagCompound baseTag = new NBTTagCompound();
            this.fireworStack.setTagCompound(baseTag);
            final NBTTagCompound fireworks = new NBTTagCompound();
            fireworks.setByte("Flight", (byte) 0);
            baseTag.setTag("Fireworks", fireworks);
            final NBTTagList explosions = new NBTTagList();
            fireworks.setTag("Explosions", explosions);
            final NBTTagCompound explosion = new NBTTagCompound();
            explosions.appendTag(explosion);
            explosion.setByte("Type", (byte) 0);
            explosion.setIntArray("Colors", new int[] { this.color });
        }

        return this.fireworStack;
    }

    /**
     * Spawns the team's signature firework on a player.
     *
     * @param player The player to spawn the firework on.
     */
    public void spawnFirework (EntityPlayer player) {

        final EntityFireworkRocket rocket = new EntityFireworkRocket(player.getEntityWorld(), player.posX, player.posY, player.posZ, this.getFireworStack());
        ObfuscationReflectionHelper.setPrivateValue(EntityFireworkRocket.class, rocket, 0, "field_92055_b");
        player.getEntityWorld().spawnEntity(rocket);
        player.world.setEntityState(rocket, (byte) 17);
        rocket.setDead();
    }

    /**
     * Gets a team by their name.
     *
     * @param name The name to search for.
     * @return The team that was found.
     */
    @Nullable
    public static Team getTeamByName (String name) {

        return TEAMS_BY_NAME.get(name);
    }

    /**
     * Gets a list of all valid team names.
     *
     * @return The list of valid team names.
     */
    public static List<String> getTeamNames () {

        return TEAM_NAMES;
    }
}
