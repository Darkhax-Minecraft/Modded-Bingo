package net.darkhax.bingo.api;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.darkhax.bingo.ModdedBingo;
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
import net.darkhax.bookshelf.util.MCJsonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

/**
 * This is the central API class for interacting with the bingo mod. If you are coding custom
 * support you should attempt to limit your references to this class in order to limit the
 * impact of breaking changes.
 */
public class BingoAPI {

    /**
     * The current game state instance.
     */
    public static GameState GAME_STATE = new GameState();

    /**
     * A constant reference to the red team.
     */
    public static final Team TEAM_RED = new Team(TextFormatting.RED, 0, DyeColor.RED);

    /**
     * A constant reference to the yellow team.
     */
    public static final Team TEAM_YELLOW = new Team(TextFormatting.YELLOW, 1, DyeColor.YELLOW);

    /**
     * A constant reference to the green team.
     */
    public static final Team TEAM_GREEN = new Team(TextFormatting.GREEN, 2, DyeColor.GREEN);

    /**
     * A constant reference to the blue team.
     */
    public static final Team TEAM_BLUE = new Team(TextFormatting.BLUE, 3, DyeColor.BLUE);

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
    public static final Gson gson = buildGsonInstance();

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

    public static Collection<ResourceLocation> getGameModeKeys() {
        
        return gameModes.keySet();
    }
    
    public static Collection<ResourceLocation> getTableKeys() {
        
        return goalTables.keySet();
    }
    
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
     * Called from BingoDataReader during data lading
     * @param gamemode the gamemode that should be added 
     */
    public static void addGameMode(GameMode gamemode) {
    	gameModes.put(gamemode.getModeId(), gamemode);
    }
    
    public static void addGoalTable(GoalTable goaltable) {
    	goalTables.put(goaltable.getName(), goaltable);
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
        
        builder.registerTypeAdapter(BlockState.class, new JsonDeserializer<BlockState>() {
			@Override
			public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return MCJsonUtils.deserializeBlockState(json.getAsJsonObject());
			}
		});
        
        builder.registerTypeAdapter(ResourceLocation.class, new JsonDeserializer<ResourceLocation>() {
			@Override
			public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return new ResourceLocation(json.getAsString());
			}
        });
        
        builder.registerTypeAdapter(Effect.class, new JsonDeserializer<Effect>() {
			@Override
			public Effect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return MCJsonUtils.getRegistryEntry(json, "effect", ForgeRegistries.POTIONS);
			}
        });
        
        builder.registerTypeAdapter(Item.class, new JsonDeserializer<Item>() {
			@Override
			public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return JSONUtils.getItem(json, "item");
			}
        });
        
        // Vanilla Class adapters
        //TODO: implement
        /*
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(Block.class, new RegistryEntryAdapter<>(ForgeRegistries.BLOCKS));
        builder.registerTypeAdapter(Item.class, new RegistryEntryAdapter<>(ForgeRegistries.ITEMS));
        builder.registerTypeAdapter(Potion.class, new RegistryEntryAdapter<>(ForgeRegistries.POTIONS));
        builder.registerTypeAdapter(Biome.class, new RegistryEntryAdapter<>(ForgeRegistries.BIOMES));
        builder.registerTypeAdapter(SoundEvent.class, new RegistryEntryAdapter<>(ForgeRegistries.SOUND_EVENTS));
        builder.registerTypeAdapter(Potion.class, new RegistryEntryAdapter<>(ForgeRegistries.POTION_TYPES));
        builder.registerTypeAdapter(Enchantment.class, new RegistryEntryAdapter<>(ForgeRegistries.ENCHANTMENTS));
        builder.registerTypeAdapter(EntityType.class, new RegistryEntryAdapter<>(ForgeRegistries.ENTITIES));
        builder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());
        */
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