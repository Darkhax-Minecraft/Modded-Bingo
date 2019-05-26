package net.darkhax.bingo.api.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Team {

    private static final Map<String, Team> TEAMS_BY_NAME = new HashMap<>();
    private static final List<String> TEAM_NAMES = new ArrayList<>();

    private final TextFormatting teamColorText;
    private final int teamCorner;
    private final EnumDyeColor dyeColor;
    private ItemStack fireworStack;
    private final ITextComponent teamName;
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

    public TextFormatting getTeamColorText () {

        return this.teamColorText;
    }

    public int getTeamCorner () {

        return this.teamCorner;
    }

    public EnumDyeColor getDyeColor () {

        return this.dyeColor;
    }

    public ITextComponent getTeamName () {

        return this.teamName;
    }

    public String getTeamKey () {

        return this.getDyeColor().getTranslationKey();
    }

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

    public void spawnFirework (EntityPlayer player) {

        final EntityFireworkRocket rocket = new EntityFireworkRocket(player.getEntityWorld(), player.posX, player.posY, player.posZ, this.getFireworStack());
        ObfuscationReflectionHelper.setPrivateValue(EntityFireworkRocket.class, rocket, 0, "field_92055_b");
        player.getEntityWorld().spawnEntity(rocket);
        player.world.setEntityState(rocket, (byte) 17);
        rocket.setDead();
    }

    public static Team getTeamByName (String name) {

        return TEAMS_BY_NAME.get(name);
    }

    public static List<String> getTeamNames () {

        return TEAM_NAMES;
    }
}
