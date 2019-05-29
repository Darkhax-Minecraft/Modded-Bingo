package net.darkhax.bingo.api;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.effects.collection.CollectionEffect;
import net.darkhax.bingo.api.effects.collection.CollectionEffectAnnouncement;
import net.darkhax.bingo.api.effects.collection.CollectionEffectFirework;
import net.darkhax.bingo.api.effects.collection.CollectionEffectShrink;
import net.darkhax.bingo.api.effects.ending.EndingEffectAnnounce;
import net.darkhax.bingo.api.effects.ending.EndingEffectFirework;
import net.darkhax.bingo.api.effects.ending.GameWinEffect;
import net.darkhax.bingo.api.effects.gameplay.IGameplayEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffectClearInventory;
import net.darkhax.bingo.api.effects.spawn.SpawnEffectMovePlayer;
import net.darkhax.bingo.api.effects.spawn.SpawnEffectPotion;
import net.darkhax.bingo.api.effects.starting.StartingEffect;
import net.darkhax.bingo.api.effects.starting.StartingEffectTime;
import net.darkhax.bingo.api.game.GameMode;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.data.BingoEffectTypeAdapter;
import net.darkhax.bingo.data.GameState;
import net.darkhax.bookshelf.adapters.ItemStackAdapter;
import net.darkhax.bookshelf.adapters.RegistryEntryAdapter;
import net.darkhax.bookshelf.adapters.ResourceLocationTypeAdapter;
import net.darkhax.bookshelf.dataloader.DataLoader;
import net.darkhax.bookshelf.dataloader.sources.DataProviderAddons;
import net.darkhax.bookshelf.dataloader.sources.DataProviderConfigs;
import net.darkhax.bookshelf.dataloader.sources.DataProviderModsOverridable;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * This is the central API class for interacting with the bingo mod. If you are coding custom
 * support you should attempt to limit your references to this class in order to limit the
 * impact of breaking changes.
 */
public class BingoAPI {

    /**
     * The current game state instance.
     */
    public static final GameState GAME_STATE = new GameState();

    /**
     * A constant reference to the red team.
     */
    public static final Team TEAM_RED = new Team(TextFormatting.RED, 0, EnumDyeColor.RED);

    /**
     * A constant reference to the yellow team.
     */
    public static final Team TEAM_YELLOW = new Team(TextFormatting.YELLOW, 1, EnumDyeColor.YELLOW);

    /**
     * A constant reference to the green team.
     */
    public static final Team TEAM_GREEN = new Team(TextFormatting.GREEN, 2, EnumDyeColor.GREEN);

    /**
     * A constant reference to the blue team.
     */
    public static final Team TEAM_BLUE = new Team(TextFormatting.BLUE, 3, EnumDyeColor.BLUE);

    /**
     * An array of all the teams.
     */
    public static final Team[] TEAMS = new Team[] { TEAM_RED, TEAM_YELLOW, TEAM_GREEN, TEAM_BLUE };

    /**
     * A type adapter used to handle ID switching during (de)serialization of CollectionEffect.
     */
    private static final BingoEffectTypeAdapter<CollectionEffect> collectionEffectAdapter = new BingoEffectTypeAdapter<>();

    /**
     * A type adapter used to handle ID switching during (de)serialization of EndingEffect.
     */
    private static final BingoEffectTypeAdapter<GameWinEffect> endingEffectAdapter = new BingoEffectTypeAdapter<>();

    /**
     * A type adapter used to handle ID switching during (de)serialization of GameplayEffect.
     */
    private static final BingoEffectTypeAdapter<IGameplayEffect> gameplayEffectAdapter = new BingoEffectTypeAdapter<>();

    /**
     * A type adapter used to handle ID switching during (de)serialization of SpawnEffect.
     */
    private static final BingoEffectTypeAdapter<SpawnEffect> spawnEffectAdapter = new BingoEffectTypeAdapter<>();

    /**
     * A type adapter used to handle ID switching during (de)serialization of StartingEffect.
     */
    private static final BingoEffectTypeAdapter<StartingEffect> startingEffectAdapter = new BingoEffectTypeAdapter<>();

    /**
     * A constant reference to the Gson instance used to (de)serialize bingo game objects.
     */
    private static final Gson gson = buildGsonInstance();

    /**
     * A map of all known game modes that have been loaded. Populated in
     * {@link #processGameModes(ResourceLocation, BufferedReader)}.
     */
    private static Map<ResourceLocation, GameMode> gameModes = new HashMap<>();

    /**
     * A map of all known goal tables that have been loaded. Populated in
     * {@link #processGoalTables(ResourceLocation, BufferedReader)}.
     */
    private static Map<ResourceLocation, GoalTable> goalTables = new HashMap<>();

    /**
     * Registers a new type of collection effect with the Gson type adapter. This allows the
     * effect to be used by data driven game modes.
     *
     * @param key The identifier for your effect.
     * @param effect A class reference to your effect.
     */
    public static void registerCollectionEffect (String key, Class<? extends CollectionEffect> effect) {

        collectionEffectAdapter.registerEffect(key, effect);
    }

    /**
     * Registers a new type of game end effect with the Gson type adapter. This allows the
     * effect to be used by data driven game modes.
     *
     * @param key The identifier for your effect.
     * @param effect A class reference to your effect.
     */
    public static void registerEndingEffect (String key, Class<? extends GameWinEffect> effect) {

        endingEffectAdapter.registerEffect(key, effect);
    }

    /**
     * Registers a new type of gameplay effect with the Gson type adapter. This allows the
     * effect to be used by data driven game modes.
     *
     * @param key The identifier for your effect.
     * @param effect A class reference to your effect.
     */
    public static void registerGameplayEffect (String key, Class<? extends IGameplayEffect> effect) {

        gameplayEffectAdapter.registerEffect(key, effect);
    }

    /**
     * Registers a new type of spawn effect with the Gson type adapter. This allows the effect
     * to be used by data driven game modes.
     *
     * @param key The identifier for your effect.
     * @param effect A class reference to your effect.
     */
    public static void registerSpawnEffect (String key, Class<? extends SpawnEffect> effect) {

        spawnEffectAdapter.registerEffect(key, effect);
    }

    /**
     * Registers a new type of game starting effect with the Gson type adapter. This allows the
     * effect to be used by data driven game modes.
     *
     * @param key The identifier for your effect.
     * @param effect A class reference to your effect.
     */
    public static void registerStartingEffect (String key, Class<? extends StartingEffect> effect) {

        startingEffectAdapter.registerEffect(key, effect);
    }

    /**
     * Looks up a goal table using it's Id.
     *
     * @param name The id of the table you want.
     * @return The table that was found. This can be null.
     */
    @Nullable
    public static GoalTable getGoalTable (ResourceLocation name) {

        return goalTables.get(name);
    }

    /**
     * Looks up a game mode using it's Id.
     *
     * @param name The Id of the game mode you want.
     * @return The game mode that was found. This can be null.
     */
    @Nullable
    public static GameMode getGameMode (ResourceLocation name) {

        return gameModes.get(name);
    }

    /**
     * Loads all of the bingo game data from the relevant json files. This is automatically
     * called by
     * {@link BingoMod#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)},
     * however it can be called again to force a reload of the data.
     */
    public static void loadData () {

        gameModes.clear();
        goalTables.clear();

        final DataLoader loader = new DataLoader(BingoMod.LOG);

        loader.addDataProvider(new DataProviderModsOverridable(BingoMod.MOD_ID));
        loader.addDataProvider(new DataProviderConfigs(BingoMod.MOD_ID));
        loader.addDataProvider(new DataProviderAddons(BingoMod.MOD_ID));

        loader.addProcessor("goaltable", BingoAPI::processGoalTables);
        loader.addProcessor("gamemode", BingoAPI::processGameModes);

        loader.loadData();
    }

    /**
     * Processes a goal table file that has been loaded using the data loader.
     *
     * @param id The suggested ID for the object.
     * @param fileReader The contents of the file as a buffered reader.
     */
    private static void processGoalTables (ResourceLocation id, BufferedReader fileReader) {

        final GoalTable table = gson.fromJson(fileReader, GoalTable.class);

        if (table != null && table.getName() != null) {

            BingoMod.LOG.info("Successfully loaded Goal Table: " + table.getName().toString());
            goalTables.put(table.getName(), table);
        }

        // TODO handle the error cases
    }

    /**
     * Processes a game mode file that has been loaded using the data loader.
     *
     * @param id The suggested ID for the object.
     * @param fileReader The contents of the file as a buffered reader.
     */
    private static void processGameModes (ResourceLocation id, BufferedReader fileReader) {

        final GameMode mode = gson.fromJson(fileReader, GameMode.class);

        if (mode != null && mode.getModeId() != null) {

            BingoMod.LOG.info("Successfully loaded Game Mode: " + mode.getModeId().toString());
            gameModes.put(mode.getModeId(), mode);
        }

        // TODO handle the error cases
    }

    /**
     * Builds a Gson instance with all the type adapters and other properties used to properly
     * serialize bingo game files.
     *
     * @return
     */
    private static Gson buildGsonInstance () {

        final GsonBuilder builder = new GsonBuilder();

        builder.excludeFieldsWithoutExposeAnnotation();

        // Bingo effect adapters
        builder.registerTypeAdapter(CollectionEffect.class, collectionEffectAdapter);
        builder.registerTypeAdapter(GameWinEffect.class, endingEffectAdapter);
        builder.registerTypeAdapter(IGameplayEffect.class, gameplayEffectAdapter);
        builder.registerTypeAdapter(SpawnEffect.class, spawnEffectAdapter);
        builder.registerTypeAdapter(StartingEffect.class, startingEffectAdapter);

        // Vanilla Class adapters
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(Block.class, new RegistryEntryAdapter<>(ForgeRegistries.BLOCKS));
        builder.registerTypeAdapter(Item.class, new RegistryEntryAdapter<>(ForgeRegistries.ITEMS));
        builder.registerTypeAdapter(Potion.class, new RegistryEntryAdapter<>(ForgeRegistries.POTIONS));
        builder.registerTypeAdapter(Biome.class, new RegistryEntryAdapter<>(ForgeRegistries.BIOMES));
        builder.registerTypeAdapter(SoundEvent.class, new RegistryEntryAdapter<>(ForgeRegistries.SOUND_EVENTS));
        builder.registerTypeAdapter(PotionType.class, new RegistryEntryAdapter<>(ForgeRegistries.POTION_TYPES));
        builder.registerTypeAdapter(Enchantment.class, new RegistryEntryAdapter<>(ForgeRegistries.ENCHANTMENTS));
        builder.registerTypeAdapter(EntityEntry.class, new RegistryEntryAdapter<>(ForgeRegistries.ENTITIES));
        builder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());

        return builder.create();
    }

    static {

        registerCollectionEffect("bingo:firework", CollectionEffectFirework.class);
        registerCollectionEffect("bingo:announcement", CollectionEffectAnnouncement.class);
        registerCollectionEffect("bingo:shrink_item", CollectionEffectShrink.class);

        registerEndingEffect("bingo:announcement", EndingEffectAnnounce.class);
        registerEndingEffect("bingo:firework", EndingEffectFirework.class);

        registerSpawnEffect("bingo:randomize_spawn", SpawnEffectMovePlayer.class);
        registerSpawnEffect("bingo:potion_effect", SpawnEffectPotion.class);
        registerSpawnEffect("bingo:clear_inventory", SpawnEffectClearInventory.class);

        registerStartingEffect("bingo:set_time", StartingEffectTime.class);
    }
}