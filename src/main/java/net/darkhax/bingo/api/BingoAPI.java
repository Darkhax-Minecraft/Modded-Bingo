package net.darkhax.bingo.api;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.effects.collection.CollectionEffect;
import net.darkhax.bingo.api.effects.collection.CollectionEffectAnnouncement;
import net.darkhax.bingo.api.effects.collection.CollectionEffectFirework;
import net.darkhax.bingo.api.effects.collection.CollectionEffectShrink;
import net.darkhax.bingo.api.effects.ending.EndingEffect;
import net.darkhax.bingo.api.effects.ending.EndingEffectAnnounce;
import net.darkhax.bingo.api.effects.ending.EndingEffectFirework;
import net.darkhax.bingo.api.effects.gameplay.IGameplayEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffectMovePlayer;
import net.darkhax.bingo.api.effects.spawn.SpawnEffectPotion;
import net.darkhax.bingo.api.effects.starting.StartingEffect;
import net.darkhax.bingo.api.effects.starting.StartingEffectTime;
import net.darkhax.bingo.api.game.GameMode;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.data.BingoEffectTypeAdapter;
import net.darkhax.bookshelf.adapters.ItemStackAdapter;
import net.darkhax.bookshelf.adapters.RegistryEntryAdapter;
import net.darkhax.bookshelf.dataloader.DataLoader;
import net.darkhax.bookshelf.dataloader.sources.DataProviderAddons;
import net.darkhax.bookshelf.dataloader.sources.DataProviderConfigs;
import net.darkhax.bookshelf.dataloader.sources.DataProviderModsOverridable;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BingoAPI {

    private static final BingoEffectTypeAdapter<CollectionEffect> collectionEffectAdapter = new BingoEffectTypeAdapter<>();
    private static final BingoEffectTypeAdapter<EndingEffect> endingEffectAdapter = new BingoEffectTypeAdapter<>();
    private static final BingoEffectTypeAdapter<IGameplayEffect> gameplayEffectAdapter = new BingoEffectTypeAdapter<>();
    private static final BingoEffectTypeAdapter<SpawnEffect> spawnEffectAdapter = new BingoEffectTypeAdapter<>();
    private static final BingoEffectTypeAdapter<StartingEffect> startingEffectAdapter = new BingoEffectTypeAdapter<>();
    
    private static Map<ResourceLocation, GameMode> gameModes = new HashMap<>();
    
    private static Map<String, GoalTable> goalTables = new HashMap<>();

    public static void registerCollectionEffect(String key, Class<? extends CollectionEffect> effect) {
        
        collectionEffectAdapter.registerEffect(key, effect);
    }
    
    public static void registerEndingEffect(String key, Class<? extends EndingEffect> effect) {
        
        endingEffectAdapter.registerEffect(key, effect);
    }
    
    public static void registerGameplayEffect(String key, Class<? extends IGameplayEffect> effect) {
        
        gameplayEffectAdapter.registerEffect(key, effect);
    }
    
    public static void registerSpawnEffect(String key, Class<? extends SpawnEffect> effect) {
        
        spawnEffectAdapter.registerEffect(key, effect);
    }

    public static void registerStartingEffect(String key, Class <? extends StartingEffect> effect) {
        
        startingEffectAdapter.registerEffect(key, effect);
    }
    
    public static GoalTable getGoalTable (String name) {

        return goalTables.get(name);
    }

    public static GoalTable creteGoalTable (String name) {

        final GoalTable table = new GoalTable(name);
        goalTables.put(name, table);
        return table;
    }
    
    public static void loadData() {
        
        gameModes.clear();
        goalTables.clear();
        
        final DataLoader loader = new DataLoader(BingoMod.LOG);
        
        loader.addDataProvider(new DataProviderModsOverridable(BingoMod.MOD_ID));
        loader.addDataProvider(new DataProviderConfigs(BingoMod.MOD_ID));
        loader.addDataProvider(new DataProviderAddons(BingoMod.MOD_ID));
        
        loader.addProcessor("goaltables", BingoAPI::processGoalTables);
        loader.addProcessor("gamemodes", BingoAPI::processGameModes);
        
        loader.loadData();
    }
    
    private static void processGoalTables (ResourceLocation id, BufferedReader fileReader) {
        
    }
    
    private static void processGameModes (ResourceLocation id, BufferedReader fileReader) {
        
    }
    
    private static Gson buildGsonInstance() {
        
        GsonBuilder builder = new GsonBuilder();
        
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.registerTypeAdapter(CollectionEffect.class, collectionEffectAdapter);
        builder.registerTypeAdapter(EndingEffect.class, endingEffectAdapter);
        builder.registerTypeAdapter(IGameplayEffect.class, gameplayEffectAdapter);
        builder.registerTypeAdapter(SpawnEffect.class, spawnEffectAdapter);
        builder.registerTypeAdapter(StartingEffect.class, startingEffectAdapter);
        
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(Block.class, new RegistryEntryAdapter<>(ForgeRegistries.BLOCKS));
        builder.registerTypeAdapter(Item.class, new RegistryEntryAdapter<>(ForgeRegistries.ITEMS));
        builder.registerTypeAdapter(Potion.class, new RegistryEntryAdapter<>(ForgeRegistries.POTIONS));
        builder.registerTypeAdapter(Biome.class, new RegistryEntryAdapter<>(ForgeRegistries.BIOMES));
        builder.registerTypeAdapter(SoundEvent.class, new RegistryEntryAdapter<>(ForgeRegistries.SOUND_EVENTS));
        builder.registerTypeAdapter(PotionType.class, new RegistryEntryAdapter<>(ForgeRegistries.POTION_TYPES));
        builder.registerTypeAdapter(Enchantment.class, new RegistryEntryAdapter<>(ForgeRegistries.ENCHANTMENTS));
        builder.registerTypeAdapter(EntityEntry.class, new RegistryEntryAdapter<>(ForgeRegistries.ENTITIES));

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
        
        registerStartingEffect("bingo:set_time", StartingEffectTime.class);
    }
}