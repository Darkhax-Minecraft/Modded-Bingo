package net.darkhax.bingo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.darkhax.bingo.api.Goal;
import net.darkhax.bingo.api.GoalTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;

public class GameManager {

    private final Goal[][] goals = new Goal[5][5];
    private final boolean[][][] completionStates = new boolean[5][5][4];
    
    private GoalTable table;
    
    public GameManager(GoalTable table) {
    	
    	this.table = table;
    }
    
    public Goal getGoal(int x, int y) {
    	
    	return goals[x][y];
    }
    
    public void setGoal(int x, int y, Goal goal) {
    	
    	goals[x][y] = goal;
    }
    
    public void setGoalComplete(EntityPlayerMP player, int x, int y, int team, boolean state) {
    	
    	if (completionStates[x][y][team] != state) {
    		
        	completionStates[x][y][team] = state;
        	
        	if (state && player != null) {
        		
        		Goal goal = this.getGoal(x, y);
        		player.server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.player.obtained", player.getDisplayName(), goal.getTarget().getTextComponent()));
        	}
    	}
    }
    
    public boolean hasCompletedGoal(int x, int y, int team) {
    	
    	return completionStates[x][y][team];
    }
    
    public boolean[] getCompletionStats(int x, int y) {
    	
    	return completionStates[x][y];
    }
    
    public int checkWinState() {
    	
    	for (int team = 0; team < 4; team++) {
    		
    		// Check vertical lines
        	for (int x = 0; x < 5; x++) {
        		
        		boolean hasFailed = false;
        		
        		for (int y = 0; y < 5; y++) {
        			
        			if (!hasCompletedGoal(x, y, team)) {
        				
        				hasFailed = true;
        				break;
        			}
        		}
        		
        		if (!hasFailed) {
        			
        			return team;
        		}
        	}
        	
    		// Check horizontal lines
        	for (int y = 0; y < 5; y++) {
        		
        		boolean hasFailed = false;
        		
        		for (int x = 0; x < 5; x++) {
        			
        			if (!hasCompletedGoal(x, y, team)) {
        				
        				hasFailed = true;
        				break;
        			}
        		}
        		
        		if (!hasFailed) {
        			
        			return team;
        		}
        	}
        	
        	// Check one horizontal line
        	if (this.hasCompletedGoal(0, 0, team) && this.hasCompletedGoal(1, 1, team) && this.hasCompletedGoal(2, 2, team) && this.hasCompletedGoal(3, 3, team) && this.hasCompletedGoal(4, 4, team)) {
        		
        		return team;
        	}
        	
        	// Check other horizontal line
        	if (this.hasCompletedGoal(0, 4, team) && this.hasCompletedGoal(1, 3, team) && this.hasCompletedGoal(2, 2, team) && this.hasCompletedGoal(3, 1, team) && this.hasCompletedGoal(4, 0, team)) {
        		
        		return team;
        	}
    	}
    	
    	return -1;
    }
    
    public void onPlayerPickupItem(EntityPlayerMP player, ItemStack stack) {
    	
    	for (int x = 0; x < 5; x++) {
    		
    		for (int y = 0; y < 5; y++) {
    			
    			final Goal goal = getGoal(x, y);
    			
    			if (ItemStack.areItemStacksEqual(goal.getTarget(), stack)) {
    				
    				setGoalComplete(player, x, y, 1, true);
    			}
    		}
    	}
    }
    
    public void reload(Random rand) {
    	  	
    	List<Goal> generatedGoals = new ArrayList<>();
    	
    	for (int i = 0; i < 25; i++) {
    		
    		Goal goal = null;
    		
    		while (goal == null) {
    			
    			
        		final Goal randGoal = table.getRandomTier(rand).getRandomGoal(rand);
        		
        		if (!generatedGoals.contains(randGoal)) {
        			
        			goal = randGoal;
        		}
        		
    		}
    		
    		generatedGoals.add(goal);
    	}
    	
    	Collections.shuffle(generatedGoals);
    	
    	int xOff = 0;
    	int yOff = 0;
    	
    	for (Goal goal : generatedGoals) {
    		
    		this.setGoal(xOff, yOff, goal);
    		
    		for (int team = 0; team < 4; team++) {
    			
    			this.setGoalComplete(null, xOff, yOff, team, false);
    		}
    		
    		xOff++;
    		if (xOff == 5) {
    			
    			xOff = 0;
    			yOff++;
    		}
    	}
    }
}