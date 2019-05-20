package net.darkhax.bingo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Team {

	private static final Map<String, Team> TEAMS_BY_NAME = new HashMap<>();
	private static final List<String> TEAM_NAMES = new ArrayList<>();
	
	private final TextFormatting teamColorText;
	private final int teamCorner;
	private final EnumDyeColor dyeColor;
	private ItemStack fireworStack;
	private final ITextComponent teamName;
	
	public Team(TextFormatting teamColorText, int teamCorner, EnumDyeColor dyeColor) {
		
		this.teamColorText = teamColorText;
		this.teamCorner = teamCorner;
		this.dyeColor = dyeColor;
		TEAMS_BY_NAME.put(dyeColor.getTranslationKey(), this);
		TEAM_NAMES.add(dyeColor.getTranslationKey());
		this.teamName = new TextComponentString(dyeColor.getTranslationKey());
		this.teamName.getStyle().setColor(this.getTeamColorText());
	}

	public TextFormatting getTeamColorText() {
		return teamColorText;
	}

	public int getTeamCorner() {
		return teamCorner;
	}

	public EnumDyeColor getDyeColor() {
		return dyeColor;
	}
	
	public ITextComponent getTeamName() {
		
		return this.teamName;
	}

	public ItemStack getFireworStack() {
		
		if (this.fireworStack == null || this.fireworStack.isEmpty()) {
				
			this.fireworStack = new ItemStack(Items.FIREWORKS);
			NBTTagCompound baseTag = new NBTTagCompound();
			fireworStack.setTagCompound(baseTag);        		
			NBTTagCompound fireworks = new NBTTagCompound();
			fireworks.setByte("Flight", (byte) 0);
			baseTag.setTag("Fireworks", fireworks);        		
			NBTTagList explosions = new NBTTagList();
			fireworks.setTag("Explosions", explosions);
			NBTTagCompound explosion = new NBTTagCompound();
			explosions.appendTag(explosion);
			explosion.setByte("Type", (byte) 0);
			explosion.setIntArray("Colors", new int[] {this.dyeColor.getColorValue()});
		}
		
		return fireworStack;
	}
	
	public static Team getTeamByName(String name) {
		
		return TEAMS_BY_NAME.get(name);
	}
	
	public static List<String> getTeamNames() {
		
		return TEAM_NAMES;
	}
}
