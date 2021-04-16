package net.darkhax.bingo.api.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.effects.collection.CollectionEffect;
import net.darkhax.bingo.api.effects.ending.GameWinEffect;
import net.darkhax.bingo.api.effects.gameplay.IGameplayEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.api.effects.starting.StartingEffect;
import net.darkhax.bingo.api.goal.GoalTable;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a game mode that players can play.
 */
public class GameMode {

    /**
     * The id of the game mode.
     */
    @Expose
    private ResourceLocation modeId;

    /**
     * A list of all possible goal tables. One will be randomly selected.
     */
    @Expose
    private List<ResourceLocation> goalTables;

    /**
     * Effects to apply when the game starts.
     */
    @Expose
    private List<StartingEffect> startingEffects;

    /**
     * Effects to apply when a goal is completed.
     */
    @Expose
    private List<CollectionEffect> itemCollectEffects;

    /**
     * Effects to apply when the game is won.
     */
    @Expose
    private List<GameWinEffect> winEffects;

    /**
     * Effects to apply when the game is happening.
     */
    @Expose
    private List<IGameplayEffect> gameplayEffects;

    /**
     * Effects to apply when the players are spawned into the game.
     */
    @Expose
    private List<SpawnEffect> spawnEffects;

    /**
     * Gets the ID of the game mode. Used to (de)serialize the game mode, and specify it with
     * commands.
     *
     * @return The ID of the game mode.
     */
    public ResourceLocation getModeId () {

        return this.modeId;
    }

    /**
     * Gets all the possible goal tables.
     *
     * @return All possible goal tables.
     */
    public List<ResourceLocation> getGoalTables () {

        return this.goalTables;
    }

    /**
     * Gets all the starting effects.
     *
     * @return All the starting effects.
     */
    public List<StartingEffect> getStartingEffects () {

        return this.startingEffects;
    }

    /**
     * Gets all the item collection effects.
     *
     * @return All the item collection effects.
     */
    public List<CollectionEffect> getItemCollectEffects () {

        return this.itemCollectEffects;
    }

    /**
     * Gets all the game win effects.
     *
     * @return All the game win effects.
     */
    public List<GameWinEffect> getWinEffects () {

        return this.winEffects;
    }

    /**
     * Gets all the game play effects.
     *
     * @return All the game play effects.
     */
    public List<IGameplayEffect> getGameplayEffects () {

        return this.gameplayEffects;
    }

    /**
     * Gets all the spawn effects.
     *
     * @return All the spawn effects.
     */
    public List<SpawnEffect> getSpawnEffect () {

        return this.spawnEffects;
    }

    /**
     * Gets a random goal table by looking up a random ID from {@link #getGoalTables()}.
     *
     * @param random An instance of random.
     * @return The selected goal table.
     */
    @Nullable
    public GoalTable getRandomTable (Random random) {

        final List<GoalTable> tables = new ArrayList<>();

        for (final ResourceLocation tableId : this.getGoalTables()) {

            tables.add(BingoAPI.getGoalTable(tableId));
        }

        return tables.get(random.nextInt(tables.size()));
    }
}
