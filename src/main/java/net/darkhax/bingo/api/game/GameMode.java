package net.darkhax.bingo.api.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.effects.collection.CollectionEffect;
import net.darkhax.bingo.api.effects.ending.EndingEffect;
import net.darkhax.bingo.api.effects.gameplay.IGameplayEffect;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.api.effects.starting.StartingEffect;
import net.darkhax.bingo.api.goal.GoalTable;
import net.minecraft.util.ResourceLocation;

public class GameMode {

    @Expose
    private ResourceLocation modeId;

    @Expose
    private List<ResourceLocation> goalTables;

    @Expose
    private List<StartingEffect> startingEffects;

    @Expose
    private List<CollectionEffect> itemCollectEffects;

    @Expose
    private List<EndingEffect> endingEffects;

    @Expose
    private List<IGameplayEffect> gameplayEffects;

    private List<SpawnEffect> spawnEffect;

    public ResourceLocation getModeId () {

        return this.modeId;
    }

    public List<ResourceLocation> getGoalTables () {

        return this.goalTables;
    }

    public List<StartingEffect> getStartingEffects () {

        return this.startingEffects;
    }

    public List<CollectionEffect> getItemCollectEffects () {

        return this.itemCollectEffects;
    }

    public List<EndingEffect> getEndingEffects () {

        return this.endingEffects;
    }

    public List<IGameplayEffect> getGameplayEffects () {

        return this.gameplayEffects;
    }

    public List<SpawnEffect> getSpawnEffect () {

        return this.spawnEffect;
    }

    public GoalTable getRandomTable (Random random) {

        final List<GoalTable> tables = new ArrayList<>();

        for (final ResourceLocation tableId : this.getGoalTables()) {

            tables.add(BingoAPI.getGoalTable(tableId));
        }

        return tables.get(random.nextInt(tables.size()));
    }
}