package net.darkhax.bingo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.darkhax.bingo.api.Goal;
import net.darkhax.bingo.api.GoalTable;
import net.darkhax.bingo.api.GoalTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "bingo", name = "Bingo", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class BingoMod {

	public static final GoalTable DEFAULT = BingoAPI.creteGoalTable("default");
	public static final GoalTable CLASSIC = BingoAPI.creteGoalTable("classic");
	
	public static GameManager currentGame;
	
    @EventHandler
    public void init (FMLInitializationEvent event) {
    	
    	registerEntries(DEFAULT);
    	registerEntries(CLASSIC);
    	
    	currentGame = new GameManager(CLASSIC);
    	currentGame.reload(new Random());
    }
    
	private static void registerEntries(GoalTable table) {
    	
		GoalTier easy = table.createTier("easy", 40);
		
    	// Easy
    	easy.add(new Goal(new ItemStack(Items.SIGN), 20));
    	easy.add(new Goal(new ItemStack(Items.ITEM_FRAME), 20));
    	easy.add(new Goal(new ItemStack(Items.PAINTING), 20));
    	easy.add(new Goal(new ItemStack(Items.EGG), 15));
    	easy.add(new Goal(new ItemStack(Items.GUNPOWDER), 20));
    	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 1), 5));
    	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 4), 5));
    	easy.add(new Goal(new ItemStack(Items.BEETROOT_SOUP), 10));
    	easy.add(new Goal(new ItemStack(Items.SUGAR), 10));
    	easy.add(new Goal(new ItemStack(Items.FLINT), 10));
    	easy.add(new Goal(new ItemStack(Items.MELON), 20));
    	easy.add(new Goal(new ItemStack(Items.FLINT_AND_STEEL), 20));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 0), 5));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 2), 10));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 3), 5));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 4), 5));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 6), 1));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 8), 5));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 10), 5));
    	easy.add(new Goal(new ItemStack(Items.DYE, 1, 13), 1));
    	easy.add(new Goal(new ItemStack(Items.PUMPKIN_SEEDS), 20));
    	easy.add(new Goal(new ItemStack(Blocks.RAIL), 20));
    	easy.add(new Goal(new ItemStack(Items.APPLE), 10));
    	easy.add(new Goal(new ItemStack(Items.BRICK), 10));
    	easy.add(new Goal(new ItemStack(Items.SPIDER_EYE), 10));
    	easy.add(new Goal(new ItemStack(Blocks.DEADBUSH), 10));
    	easy.add(new Goal(new ItemStack(Blocks.VINE), 10));
    	easy.add(new Goal(new ItemStack(Items.BONE), 10));
    	easy.add(new Goal(new ItemStack(Items.GLASS_BOTTLE), 10));
    	easy.add(new Goal(new ItemStack(Items.FLOWER_POT), 20));
    	easy.add(new Goal(new ItemStack(Blocks.TALLGRASS, 1, 2), 5));
    	GoalTier medium = table.createTier("medium", 32);
    	
    	// Medium
    	medium.add(new Goal(new ItemStack(Items.GOLDEN_SWORD), 10));
    	medium.add(new Goal(new ItemStack(Items.CLOCK), 20));
    	medium.add(new Goal(new ItemStack(Blocks.GOLDEN_RAIL), 10));
    	medium.add(new Goal(new ItemStack(Blocks.HOPPER), 20));
    	medium.add(new Goal(new ItemStack(Items.FISH), 15));
    	medium.add(new Goal(new ItemStack(Items.CAKE), 15));
    	medium.add(new Goal(new ItemStack(Items.MILK_BUCKET), 10));
    	medium.add(new Goal(new ItemStack(Items.ENCHANTED_BOOK), 5));
    	medium.add(new Goal(new ItemStack(Items.CAULDRON), 10));
    	medium.add(new Goal(new ItemStack(Items.CHEST_MINECART), 10));
    	medium.add(new Goal(new ItemStack(Items.TNT_MINECART), 10));
    	medium.add(new Goal(new ItemStack(Items.FIREWORKS), 10));
    	medium.add(new Goal(new ItemStack(Items.COMPASS), 20));
    	medium.add(new Goal(new ItemStack(Items.MAP), 10));
    	medium.add(new Goal(new ItemStack(Items.FURNACE_MINECART), 10));
    	medium.add(new Goal(new ItemStack(Items.EMERALD), 5));
    	medium.add(new Goal(new ItemStack(Items.ENDER_PEARL), 10));
    	medium.add(new Goal(new ItemStack(Items.SLIME_BALL), 5));
    	medium.add(new Goal(new ItemStack(Items.SPECKLED_MELON), 15));
    	medium.add(new Goal(new ItemStack(Items.COOKIE), 15));
    	medium.add(new Goal(new ItemStack(Items.PUMPKIN_PIE), 15));
    	medium.add(new Goal(new ItemStack(Items.MUSHROOM_STEW), 10));
    	medium.add(new Goal(new ItemStack(Items.GOLDEN_SHOVEL), 10));
    	medium.add(new Goal(new ItemStack(Items.FERMENTED_SPIDER_EYE), 10));
    	medium.add(new Goal(new ItemStack(Items.BOOK), 15));
    	medium.add(new Goal(new ItemStack(Items.WRITABLE_BOOK), 10));
    	
    	// Hard
    	GoalTier hard = table.createTier("hard", 25);
    	hard.add(new Goal(new ItemStack(Items.HOPPER_MINECART), 20));
    	hard.add(new Goal(new ItemStack(Items.SADDLE), 5));
    	hard.add(new Goal(new ItemStack(Items.NAME_TAG), 5));
    	hard.add(new Goal(new ItemStack(Items.GOLDEN_APPLE), 5));
    	hard.add(new Goal(new ItemStack(Items.DIAMOND_AXE), 10));
    	hard.add(new Goal(new ItemStack(Items.DIAMOND), 15));
    	hard.add(new Goal(new ItemStack(Items.DIAMOND_HOE), 15));
    	
    	if (table == DEFAULT) {
    		
        	easy.add(new Goal(new ItemStack(Items.COAL, 1, 1), 20));
        	easy.add(new Goal(new ItemStack(Items.GLOWSTONE_DUST), 5));
        	easy.add(new Goal(new ItemStack(Blocks.TRIPWIRE_HOOK), 10));
        	easy.add(new Goal(new ItemStack(Items.CARROT_ON_A_STICK), 10));
        	easy.add(new Goal(new ItemStack(Blocks.LADDER), 20));
        	easy.add(new Goal(new ItemStack(Blocks.STONE_SLAB, 1, 0), 20));
        	easy.add(new Goal(new ItemStack(Blocks.GLASS_PANE), 20));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 1), 5));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 5), 5));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 7), 1));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 9), 5));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 11), 5));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 12), 5));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 14), 1));
        	easy.add(new Goal(new ItemStack(Items.DYE, 1, 15), 5));       	
        	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 0), 5));
        	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 2), 5));
        	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 3), 5));
        	easy.add(new Goal(new ItemStack(Blocks.SAPLING, 1, 5), 5));
        	
        	medium.add(new Goal(new ItemStack(Items.BLAZE_ROD), 5));
        	medium.add(new Goal(new ItemStack(Items.QUARTZ), 5));
        	medium.add(new Goal(new ItemStack(Items.PRISMARINE_SHARD), 5));
        	medium.add(new Goal(new ItemStack(Items.LEAD), 5));
        	medium.add(new Goal(new ItemStack(Items.SPECTRAL_ARROW), 10));
        	medium.add(new Goal(new ItemStack(Items.GOLDEN_CARROT), 10));
        	medium.add(new Goal(new ItemStack(Items.ARMOR_STAND), 15));
        	medium.add(new Goal(new ItemStack(Blocks.REDSTONE_LAMP), 10));
        	medium.add(new Goal(new ItemStack(Items.COMPARATOR), 10));
        	medium.add(new Goal(new ItemStack(Blocks.PISTON), 10));
        	medium.add(new Goal(new ItemStack(Blocks.DISPENSER), 10));
        	medium.add(new Goal(new ItemStack(Blocks.IRON_TRAPDOOR), 10));
        	medium.add(new Goal(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), 10));
        	medium.add(new Goal(new ItemStack(Blocks.JUKEBOX), 10));
        	medium.add(new Goal(new ItemStack(Blocks.DROPPER), 10));
        	medium.add(new Goal(new ItemStack(Blocks.OBSERVER), 5));
        	medium.add(new Goal(new ItemStack(Items.GHAST_TEAR), 5));
        	
        	hard.add(new Goal(new ItemStack(Items.RABBIT_STEW), 20));
        	hard.add(new Goal(new ItemStack(Blocks.NETHER_WART_BLOCK), 10));
        	hard.add(new Goal(new ItemStack(Items.ENDER_EYE), 10));
        	hard.add(new Goal(new ItemStack(Items.FISH, 1, 3), 10));
        	hard.add(new Goal(new ItemStack(Items.DIAMOND_CHESTPLATE), 10));
        	hard.add(new Goal(new ItemStack(Items.DIAMOND_HORSE_ARMOR), 5));
        	hard.add(new Goal(new ItemStack(Items.GOLDEN_HORSE_ARMOR), 5));
        	hard.add(new Goal(new ItemStack(Items.IRON_HORSE_ARMOR), 5));
        	hard.add(new Goal(new ItemStack(Blocks.JUKEBOX), 20));
        	hard.add(new Goal(new ItemStack(Blocks.ENDER_CHEST), 10));
        	hard.add(new Goal(new ItemStack(Blocks.ENCHANTING_TABLE), 10));
        	hard.add(new Goal(new ItemStack(Blocks.ANVIL), 10));
        	
        	// Challenging
        	GoalTier challenging = table.createTier("challenging", 3);
        	challenging.add(new Goal(new ItemStack(Blocks.GRASS), 20));
        	challenging.add(new Goal(new ItemStack(Blocks.BEACON), 20));
        	challenging.add(new Goal(new ItemStack(Items.CHORUS_FRUIT), 20));
        	challenging.add(new Goal(new ItemStack(Items.TOTEM_OF_UNDYING), 20));
        	challenging.add(new Goal(new ItemStack(Items.NETHER_STAR), 20));
        	challenging.add(new Goal(new ItemStack(Items.SKULL, 1, 5), 20));
        	challenging.add(new Goal(new ItemStack(Items.RABBIT_FOOT), 20));
        	challenging.add(new Goal(new ItemStack(Items.DRAGON_BREATH), 20));
    	}
	}
}