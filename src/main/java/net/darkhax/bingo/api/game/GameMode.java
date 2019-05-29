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
    private List<ResourceLocation> goalTables = new ArrayList<>();
    
    @Expose
    private List<StartingEffect> startingEffects = new ArrayList<>();
    
    @Expose
    private List<CollectionEffect> itemCollectEffects = new ArrayList<>();
    
    @Expose
    private List<EndingEffect> endingEffects = new ArrayList<>();
    
    @Expose
    private List<IGameplayEffect> gameplayEffects = new ArrayList<>();
    
    private List<SpawnEffect> spawnEffect = new ArrayList<>();

    public ResourceLocation getModeId () {
        
        return modeId;
    }

    public List<ResourceLocation> getGoalTables () {
        
        return goalTables;
    }

    public List<StartingEffect> getStartingEffects () {
        
        return startingEffects;
    }

    public List<CollectionEffect> getItemCollectEffects () {
        
        return itemCollectEffects;
    }

    public List<EndingEffect> getEndingEffects () {
        
        return endingEffects;
    }

    public List<IGameplayEffect> getGameplayEffects () {
        
        return gameplayEffects;
    }

    public List<SpawnEffect> getSpawnEffect () {
        
        return spawnEffect;
    }
    
    public GoalTable getRandomTable(Random random) {
        
        List<GoalTable> tables = new ArrayList<>();
        
        for (ResourceLocation tableId : this.getGoalTables()) {
            
            tables.add(BingoAPI.getGoalTable(tableId));
        }
        
        return tables.get(random.nextInt(tables.size()));
    }
}