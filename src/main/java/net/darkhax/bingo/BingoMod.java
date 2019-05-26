package net.darkhax.bingo;

import java.io.File;
import java.io.IOException;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.api.goal.GoalTier;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.commands.CommandBingo;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bingo.data.GameState;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "bingo", name = "Bingo", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@", dependencies = "required-after:bookshelf@[2.3.573,)")
public class BingoMod {

    public static final GoalTable DEFAULT = BingoAPI.creteGoalTable("default");
    public static final GoalTable CLASSIC = BingoAPI.creteGoalTable("classic");
    public static final Team TEAM_RED = new Team(TextFormatting.RED, 0, EnumDyeColor.RED);
    public static final Team TEAM_YELLOW = new Team(TextFormatting.YELLOW, 1, EnumDyeColor.YELLOW);
    public static final Team TEAM_GREEN = new Team(TextFormatting.GREEN, 2, EnumDyeColor.GREEN);
    public static final Team TEAM_BLUE = new Team(TextFormatting.BLUE, 3, EnumDyeColor.BLUE);
    public static final Team[] TEAMS = new Team[] { TEAM_RED, TEAM_YELLOW, TEAM_GREEN, TEAM_BLUE };

    public static final GameState GAME_STATE = new GameState();
    public static final NetworkHandler NETWORK = new NetworkHandler("bingo");

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        NETWORK.register(PacketSyncGameState.class, Side.CLIENT);
        NETWORK.register(PacketSyncGoal.class, Side.CLIENT);
    }

    @EventHandler
    public void init (FMLInitializationEvent event) {

        registerEntries(DEFAULT);
        registerEntries(CLASSIC);

        BookshelfRegistry.addCommand(new CommandBingo());
    }

    @EventHandler
    public void serverStarted (FMLServerStartedEvent event) {

        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        if (bingoFile.exists()) {

            try {

                final NBTTagCompound tag = CompressedStreamTools.read(bingoFile);

                if (tag != null) {

                    BingoPersistantData.read(tag);
                }
            }

            catch (final IOException e) {

                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void serverStopping (FMLServerStoppingEvent event) {

        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        try {

            CompressedStreamTools.write(BingoPersistantData.write(), bingoFile);
        }

        catch (final IOException e) {

            e.printStackTrace();
        }
    }

    private static void registerEntries (GoalTable table) {

        final GoalTier easy = table.createTier("easy", 40);

        // Easy
        easy.createGoal("sign", new ItemStack(Items.SIGN), 20);
        easy.createGoal("itemframe", new ItemStack(Items.ITEM_FRAME), 20);
        easy.createGoal("painting", new ItemStack(Items.PAINTING), 20);
        easy.createGoal("egg", new ItemStack(Items.EGG), 15);
        easy.createGoal("gunpowder", new ItemStack(Items.GUNPOWDER), 20);
        easy.createGoal("spruce_sapling", new ItemStack(Blocks.SAPLING, 1, 1), 5);
        easy.createGoal("acacia_sapling", new ItemStack(Blocks.SAPLING, 1, 4), 5);
        easy.createGoal("beetroot_soup", new ItemStack(Items.BEETROOT_SOUP), 10);
        easy.createGoal("sugar", new ItemStack(Items.SUGAR), 10);
        easy.createGoal("flint", new ItemStack(Items.FLINT), 10);
        easy.createGoal("melon", new ItemStack(Items.MELON), 20);
        easy.createGoal("flint_and_steel", new ItemStack(Items.FLINT_AND_STEEL), 20);
        easy.createGoal("ink", new ItemStack(Items.DYE, 1, 0), 5);
        easy.createGoal("cactus_green", new ItemStack(Items.DYE, 1, 2), 10);
        easy.createGoal("cocoa", new ItemStack(Items.DYE, 1, 3), 5);
        easy.createGoal("lapis_lazuli", new ItemStack(Items.DYE, 1, 4), 5);
        easy.createGoal("cyan_dye", new ItemStack(Items.DYE, 1, 6), 1);
        easy.createGoal("gray_dye", new ItemStack(Items.DYE, 1, 8), 5);
        easy.createGoal("lime_dye", new ItemStack(Items.DYE, 1, 10), 5);
        easy.createGoal("magenta_dye", new ItemStack(Items.DYE, 1, 13), 1);
        easy.createGoal("pumpkin_seeds", new ItemStack(Items.PUMPKIN_SEEDS), 20);
        easy.createGoal("rail", new ItemStack(Blocks.RAIL), 20);
        easy.createGoal("apple", new ItemStack(Items.APPLE), 10);
        easy.createGoal("bric", new ItemStack(Items.BRICK), 10);
        easy.createGoal("spider_eye", new ItemStack(Items.SPIDER_EYE), 10);
        easy.createGoal("dead_bush", new ItemStack(Blocks.DEADBUSH), 10);
        easy.createGoal("vine", new ItemStack(Blocks.VINE), 10);
        easy.createGoal("bone", new ItemStack(Items.BONE), 10);
        easy.createGoal("glass_bottle", new ItemStack(Items.GLASS_BOTTLE), 10);
        easy.createGoal("flower_pot", new ItemStack(Items.FLOWER_POT), 20);
        easy.createGoal("fern", new ItemStack(Blocks.TALLGRASS, 1, 2), 5);

        final GoalTier medium = table.createTier("medium", 32);

        // Medium
        medium.createGoal("golden_sword", new ItemStack(Items.GOLDEN_SWORD), 10);
        medium.createGoal("clock", new ItemStack(Items.CLOCK), 20);
        medium.createGoal("powered_rail", new ItemStack(Blocks.GOLDEN_RAIL), 10);
        medium.createGoal("hopper", new ItemStack(Blocks.HOPPER), 20);
        medium.createGoal("cod_fish", new ItemStack(Items.FISH), 15);
        medium.createGoal("cake", new ItemStack(Items.CAKE), 15);
        medium.createGoal("milk_bucket", new ItemStack(Items.MILK_BUCKET), 10);
        medium.createGoal("enchanted_book", new ItemStack(Items.ENCHANTED_BOOK), 5);
        medium.createGoal("cauldron", new ItemStack(Items.CAULDRON), 10);
        medium.createGoal("chest_minecart", new ItemStack(Items.CHEST_MINECART), 10);
        medium.createGoal("tnt_minecart", new ItemStack(Items.TNT_MINECART), 10);
        medium.createGoal("firework", new ItemStack(Items.FIREWORKS), 10);
        medium.createGoal("compass", new ItemStack(Items.COMPASS), 20);
        medium.createGoal("empty_map", new ItemStack(Items.MAP), 10);
        medium.createGoal("furnace_minecart", new ItemStack(Items.FURNACE_MINECART), 10);
        medium.createGoal("emerald", new ItemStack(Items.EMERALD), 5);
        medium.createGoal("ender_pearl", new ItemStack(Items.ENDER_PEARL), 10);
        medium.createGoal("slime_ball", new ItemStack(Items.SLIME_BALL), 5);
        medium.createGoal("glistening_melon", new ItemStack(Items.SPECKLED_MELON), 15);
        medium.createGoal("cookie", new ItemStack(Items.COOKIE), 15);
        medium.createGoal("pumpkin_pie", new ItemStack(Items.PUMPKIN_PIE), 15);
        medium.createGoal("mushroom_stew", new ItemStack(Items.MUSHROOM_STEW), 10);
        medium.createGoal("golen_shovel", new ItemStack(Items.GOLDEN_SHOVEL), 10);
        medium.createGoal("fermented_spider_eye", new ItemStack(Items.FERMENTED_SPIDER_EYE), 10);
        medium.createGoal("book", new ItemStack(Items.BOOK), 15);
        medium.createGoal("book_and_quil", new ItemStack(Items.WRITABLE_BOOK), 10);

        // Hard
        final GoalTier hard = table.createTier("hard", 25);
        hard.createGoal("hopper_minecart", new ItemStack(Items.HOPPER_MINECART), 20);
        hard.createGoal("saddle", new ItemStack(Items.SADDLE), 5);
        hard.createGoal("name_tag", new ItemStack(Items.NAME_TAG), 5);
        hard.createGoal("golden_apple", new ItemStack(Items.GOLDEN_APPLE), 5);
        hard.createGoal("diamond_axe", new ItemStack(Items.DIAMOND_AXE), 10);
        hard.createGoal("diamond", new ItemStack(Items.DIAMOND), 15);
        hard.createGoal("diamond_hoe", new ItemStack(Items.DIAMOND_HOE), 15);

        if (table == DEFAULT) {

            easy.createGoal("charcoal", new ItemStack(Items.COAL, 1, 1), 20);
            easy.createGoal("glowstone_dust", new ItemStack(Items.GLOWSTONE_DUST), 5);
            easy.createGoal("tripwire_hook", new ItemStack(Blocks.TRIPWIRE_HOOK), 10);
            easy.createGoal("carrot_on_a_stick", new ItemStack(Items.CARROT_ON_A_STICK), 10);
            easy.createGoal("ladder", new ItemStack(Blocks.LADDER), 20);
            easy.createGoal("stone_slab", new ItemStack(Blocks.STONE_SLAB, 1, 0), 20);
            easy.createGoal("glass_pane", new ItemStack(Blocks.GLASS_PANE), 20);
            easy.createGoal("oak_sapling", new ItemStack(Blocks.SAPLING, 1, 0), 5);
            easy.createGoal("birch_sapling", new ItemStack(Blocks.SAPLING, 1, 2), 5);
            easy.createGoal("jungle_sapling", new ItemStack(Blocks.SAPLING, 1, 3), 5);
            easy.createGoal("dark_oak_sapling", new ItemStack(Blocks.SAPLING, 1, 5), 5);

            medium.createGoal("blaze_rod", new ItemStack(Items.BLAZE_ROD), 5);
            medium.createGoal("quartz", new ItemStack(Items.QUARTZ), 5);
            medium.createGoal("prismarine_shards", new ItemStack(Items.PRISMARINE_SHARD), 5);
            medium.createGoal("lead", new ItemStack(Items.LEAD), 5);
            medium.createGoal("spectral_arrow", new ItemStack(Items.SPECTRAL_ARROW), 10);
            medium.createGoal("golden_carrot", new ItemStack(Items.GOLDEN_CARROT), 10);
            medium.createGoal("armor_stand", new ItemStack(Items.ARMOR_STAND), 15);
            medium.createGoal("redstone_lamp", new ItemStack(Blocks.REDSTONE_LAMP), 10);
            medium.createGoal("comparator", new ItemStack(Items.COMPARATOR), 10);
            medium.createGoal("piston", new ItemStack(Blocks.PISTON), 10);
            medium.createGoal("dispenser", new ItemStack(Blocks.DISPENSER), 10);
            medium.createGoal("iron_trapdoor", new ItemStack(Blocks.IRON_TRAPDOOR), 10);
            medium.createGoal("golden_pressure_plate", new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), 10);
            medium.createGoal("noteblock", new ItemStack(Blocks.NOTEBLOCK), 10);
            medium.createGoal("dropper", new ItemStack(Blocks.DROPPER), 10);
            medium.createGoal("observer", new ItemStack(Blocks.OBSERVER), 5);
            medium.createGoal("ghast_tear", new ItemStack(Items.GHAST_TEAR), 5);

            hard.createGoal("rabbit_stew", new ItemStack(Items.RABBIT_STEW), 20);
            hard.createGoal("nether_wart_block", new ItemStack(Blocks.NETHER_WART_BLOCK), 10);
            hard.createGoal("ender_eye", new ItemStack(Items.ENDER_EYE), 10);
            hard.createGoal("pufferfish", new ItemStack(Items.FISH, 1, 3), 10);
            hard.createGoal("diamond_chest_plate", new ItemStack(Items.DIAMOND_CHESTPLATE), 10);
            hard.createGoal("diamond_horse_armor", new ItemStack(Items.DIAMOND_HORSE_ARMOR), 5);
            hard.createGoal("golden_horse_armor", new ItemStack(Items.GOLDEN_HORSE_ARMOR), 5);
            hard.createGoal("iron_horse_armor", new ItemStack(Items.IRON_HORSE_ARMOR), 5);
            hard.createGoal("jukebox", new ItemStack(Blocks.JUKEBOX), 20);
            hard.createGoal("ender_chest", new ItemStack(Blocks.ENDER_CHEST), 10);
            hard.createGoal("enchanting_table", new ItemStack(Blocks.ENCHANTING_TABLE), 10);
            hard.createGoal("anvil", new ItemStack(Blocks.ANVIL), 10);

            // Challenging
            final GoalTier challenging = table.createTier("challenging", 3);
            challenging.createGoal("grass_block", new ItemStack(Blocks.GRASS), 20);
            challenging.createGoal("beacon", new ItemStack(Blocks.BEACON), 20);
            challenging.createGoal("chorus_fruit", new ItemStack(Items.CHORUS_FRUIT), 20);
            challenging.createGoal("totem_of_undying", new ItemStack(Items.TOTEM_OF_UNDYING), 20);
            challenging.createGoal("nether_star", new ItemStack(Items.NETHER_STAR), 20);
            challenging.createGoal("dragon_skull", new ItemStack(Items.SKULL, 1, 5), 20);
            challenging.createGoal("rabbit_foot", new ItemStack(Items.RABBIT_FOOT), 20);
            challenging.createGoal("dragon_breath", new ItemStack(Items.DRAGON_BREATH), 20);
        }
    }
}