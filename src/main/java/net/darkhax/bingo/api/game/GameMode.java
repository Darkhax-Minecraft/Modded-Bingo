package net.darkhax.bingo.api.game;

import java.util.List;

import com.google.gson.annotations.Expose;

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
}