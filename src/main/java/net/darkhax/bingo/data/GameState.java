package net.darkhax.bingo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.effects.collection.CollectionEffect;
import net.darkhax.bingo.api.effects.ending.GameWinEffect;
import net.darkhax.bingo.api.effects.starting.StartingEffect;
import net.darkhax.bingo.api.game.GameMode;
import net.darkhax.bingo.api.goal.Goal;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * This class represents the current and active gamestate. On the server is is the controller
 * of the game, while on the client it is just used for rendering.
 */
public class GameState {

    /**
     * The game mode that was used when creating the game.
     */
    private GameMode mode;

    /**
     * The goal table used to populate {@link #goals}.
     */
    private GoalTable table;

    /**
     * An instance of random that is used when generating this game.
     */
    private Random random;

    /**
     * A two dimensional array of all items that must be acquired to win.
     */
    private ItemStack[][] goals = new ItemStack[5][5];

    /**
     * A three dimensional array of which teams have what goals.
     */
    private Team[][][] completionStates = new Team[5][5][4];

    /**
     * Whether or not the game is active. The game will only render if this is true.
     */
    private boolean isActive = false;

    /**
     * Whether or not the game has been started. The state will not update while this is false.
     */
    private boolean hasStarted = false;

    /**
     * Whether or not teams should be grouped together.
     */
    private boolean groupTeams = true;

    private boolean blackout = false;

    private int winCount = 1;

    private long startTime = -1L;

    private long endTime = -1L;

    /**
     * The team that has won the game. This can be null.
     */
    @Nullable
    private Team winner = null;

    /**
     * Creates a new game.
     *
     * @param random An instance of random.
     * @param mode The mode to play.
     */
    public void create (Random random, GameMode mode, boolean groupTeams, boolean blackout, int winCount) {

        this.mode = mode;
        this.table = mode.getRandomTable(random);
        this.random = random;
        this.rollGoals(random);
        this.isActive = true;
        this.hasStarted = false;
        this.completionStates = new Team[5][5][4];
        this.groupTeams = groupTeams;
        this.blackout = blackout;
        this.winCount = winCount < 1 ? 1: winCount > 12 ? 12 : winCount;
    }

    /**
     * Starts the game, and allows the state to change.
     *
     * @param server The server instance.
     */
    public void start (MinecraftServer server, long startTime) {

        this.hasStarted = true;
        this.startTime = startTime;
        for (final Team team : BingoAPI.TEAMS) {
          team.setFinishState(false);
        }
        for (final StartingEffect effect : this.mode.getStartingEffects()) {

            effect.onGameStarted(server);
        }
    }

    /**
     * Ends a game. This will hide the board, and reset all the states.
     */
    public void end () {

        this.hasStarted = false;
        this.isActive = false;
        this.mode = null;
        this.table = null;
        this.groupTeams = false;
        this.winner = null;
        this.startTime = -1L;
        this.endTime = -1L;
    }

    /**
     * Gets the goal item for a position on the bingo board.
     *
     * @param x The x pos of the item.
     * @param y The y pos of the item.
     * @return The item for the goal.
     */
    public ItemStack getGoal (int x, int y) {

        return this.goals[x][y];
    }

    /**
     * Sets the goal item for a position on the bingo board.
     *
     * @param x The x pos of the item.
     * @param y The y pos of the item.
     * @param goal The item for the goal to be.
     */
    public void setGoal (int x, int y, ItemStack goal) {

        this.goals[x][y] = goal;
    }

    /**
     * Sets a goal as being completed.
     *
     * @param player The player who completed the goal.
     * @param item The item that was obtained.
     * @param x The x pos of the goal.
     * @param y The y pos of the goal.
     */
    public void setGoalComplete (@Nonnull EntityPlayerMP player, ItemStack item, int x, int y) {

        final Team playerTeam = BingoPersistantData.getTeam(player);

        if (this.completionStates[x][y][playerTeam.getTeamCorner()] == null) {

            this.completionStates[x][y][playerTeam.getTeamCorner()] = playerTeam;

            for (final CollectionEffect effect : this.getMode().getItemCollectEffects()) {

                effect.onItemCollected(player, item, playerTeam);
            }

            ModdedBingo.NETWORK.sendToAll(new PacketSyncGoal(x, y, playerTeam.getTeamKey()));
        }

        this.updateWinState(player.server, player.world.getTotalWorldTime());
    }

    /**
     * Checks if a team has completed a goal.
     *
     * @param x The x pos of the goal.
     * @param y The y pos of the goal.
     * @param team The team to check for.
     * @return Whether or not the team has completed the goal.
     */
    public boolean hasCompletedGoal (int x, int y, Team team) {

        return this.completionStates[x][y][team.getTeamCorner()] != null;
    }

    /**
     * Get all completion states for a goal.
     *
     * @param x The x pos of the goal.
     * @param y The y pos of the goal.
     * @return An array of teams that have completed the goal.
     */
    public Team[] getCompletionStats (int x, int y) {

        return this.completionStates[x][y];
    }

    /**
     * Updates the win state for the game by checking if anyone has won the game.
     *
     * @param server An instance of the server.
     */
    public void updateWinState (MinecraftServer server, long time) {

        this.winner = this.checkWinState();

        if (this.winner != null && this.hasStarted() && this.isActive()) {

            //this.hasStarted = false;
            //this.endTime = time;
            boolean bingoWon = true;
            for (final Team team : BingoAPI.TEAMS) {
              if (team.getFinishState()) {
                bingoWon = false;
              }
            }
            winner.setFinishState(true);
            for (final GameWinEffect endEffect : this.mode.getWinEffects()) {

                endEffect.onGameCompleted(server, this.winner, bingoWon);
            }
        }
    }

    private boolean doesTeamHaveAll(Team team) {

        for (int x = 0; x < 5; x++) {

            for (int y = 0; y < 5; y++) {

                if (!this.hasCompletedGoal(x, y, team)) {

                	return false;
                }
            }
        }

        return true;
    }

    /**
     * Looks at the board data to find a team that has won the game.
     *
     * @return The team that won the game. This will be null if no team has won yet.
     */
    @Nullable
    public Team checkWinState () {

        for (final Team team : BingoAPI.TEAMS) {
          if (!team.getFinishState()) {
          	if (this.isBlackout()) {

          		if (doesTeamHaveAll(team)) {
          		    return team;
          		}
          	}

          	else {

                  int countWon = 0;

                  // Check vertical lines
                  for (int x = 0; x < 5; x++) {

                      boolean hasFailed = false;

                      for (int y = 0; y < 5; y++) {

                          if (!this.hasCompletedGoal(x, y, team)) {

                              hasFailed = true;
                              break;
                          }
                      }

                      if (!hasFailed) {

                          countWon += 1;
                      }
                  }

                  // Check horizontal lines
                  for (int y = 0; y < 5; y++) {

                      boolean hasFailed = false;

                      for (int x = 0; x < 5; x++) {

                          if (!this.hasCompletedGoal(x, y, team)) {

                              hasFailed = true;
                              break;
                          }
                      }

                      if (!hasFailed) {

                          countWon += 1;
                      }
                  }

                  // Check one horizontal line
                  if (this.hasCompletedGoal(0, 0, team) && this.hasCompletedGoal(1, 1, team) && this.hasCompletedGoal(2, 2, team) && this.hasCompletedGoal(3, 3, team) && this.hasCompletedGoal(4, 4, team)) {

                      countWon += 1;
                  }

                  // Check other horizontal line
                  if (this.hasCompletedGoal(0, 4, team) && this.hasCompletedGoal(1, 3, team) && this.hasCompletedGoal(2, 2, team) && this.hasCompletedGoal(3, 1, team) && this.hasCompletedGoal(4, 0, team)) {

                      countWon += 1;
                  }
                  if (countWon >= this.winCount) {
      			           return team;
      		        }
               }
          	}
        }

        return null;
    }

    /**
     * Handles a player acquiring a new item.
     *
     * @param player The player to check for.
     * @param stack The item they acquired.
     */
    public void onPlayerPickupItem (EntityPlayerMP player, ItemStack stack) {

        if (BingoAPI.GAME_STATE.hasStarted()) {

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final ItemStack goal = this.getGoal(x, y);

                    if (goal != null && !goal.isEmpty() && StackUtils.areStacksSimilarWithPartialNBT(stack, goal)) {

                        this.setGoalComplete(player, stack, x, y);
                    }
                }
            }
        }
    }

    /**
     * Randomizes the goals for the board.
     *
     * @param rand An instance of random.
     */
    public void rollGoals (Random rand) {

        final List<Goal> generatedGoals = new ArrayList<>();

        for (int i = 0; i < 25; i++) {

            Goal goal = null;

            while (goal == null) {

                final Goal randGoal = this.table.getRandomTier(rand).getRandomGoal(rand);

                if (!generatedGoals.contains(randGoal)) {

                    goal = randGoal;
                }

            }

            generatedGoals.add(goal);
        }

        Collections.shuffle(generatedGoals, this.random);

        int xOff = 0;
        int yOff = 0;

        for (final Goal goal : generatedGoals) {

            this.setGoal(xOff, yOff, goal.getTarget());

            xOff++;
            if (xOff == 5) {

                xOff = 0;
                yOff++;
            }
        }
    }

    /**
     * Gets the goal table. This can be null if the game hasn't started.
     *
     * @return The goal table being used.
     */
    @Nullable
    public GoalTable getTable () {

        return this.table;
    }

    /**
     * Gets the random instance for the game. This can be null if the game hasn't started.
     *
     * @return The random instance for the goal.
     */
    @Nullable
    public Random getRandom () {

        return this.random;
    }

    /**
     * Gets all the items used as goals.
     *
     * @return All the goal items.
     */
    public ItemStack[][] getGoals () {

        return this.goals;
    }

    /**
     * Gets all the completion states.
     *
     * @return All the completion states.
     */
    public Team[][][] getCompletionStates () {

        return this.completionStates;
    }

    /**
     * Checks if the game has started or not.
     *
     * @return Whether or not the game has started.
     */
    public boolean hasStarted () {

        return this.hasStarted;
    }

    /**
     * Checks if the game is active or not.
     *
     * @return Whether or not the game is active.
     */
    public boolean isActive () {

        return this.isActive;
    }

    /**
     * Gets the team that has won the game. This will be null if there is no winner.
     *
     * @return The team that won the game.
     */
    @Nullable
    public Team getWinner () {

        return this.winner;
    }

    /**
     * Gets the current game mode. This can be null if the game hasn't started yet.
     *
     * @return The current game mode.
     */
    @Nullable
    public GameMode getMode () {

        return this.mode;
    }

    public long getStartTime() {

    	return this.startTime;
    }

    public long getEndTime() {

    	return this.endTime;
    }

    /**
     * Reads the game state from an NBT tag.
     *
     * @param tag The tag to read the game state from.
     */
    public void read (NBTTagCompound tag) {

        this.isActive = false;
        this.hasStarted = false;
        this.mode = null;
        this.table = null;
        this.winner = null;
        this.random = null;
        this.goals = new ItemStack[5][5];
        this.completionStates = new Team[5][5][4];
        this.groupTeams = false;
        this.blackout = false;
        this.startTime = -1L;
        this.endTime = -1L;
        this.winCount = 1;

        if (tag != null) {

            // Read basic game data
            this.mode = BingoAPI.getGameMode(new ResourceLocation(tag.getString("GameMode")));
            this.table = BingoAPI.getGoalTable(new ResourceLocation(tag.getString("GoalTable")));
            this.isActive = tag.getBoolean("IsActive");
            this.hasStarted = tag.getBoolean("HasStarted");
            this.groupTeams = tag.getBoolean("GroupTeams");
            this.blackout = tag.getBoolean("Blackout");
            this.startTime = tag.getLong("StartTime");
            this.endTime = tag.getLong("EndTime");
            this.winner = Team.getTeamByName(tag.getString("Winner"));
            this.winCount = tag.getInteger("WinCount");

            if (this.table != null) {

                // Read the goal items
                final NBTTagList goalsTag = tag.getTagList("Goals", NBT.TAG_COMPOUND);

                for (int i = 0; i < goalsTag.tagCount(); i++) {

                    final NBTTagCompound goalTag = goalsTag.getCompoundTagAt(i);
                    this.setGoal(goalTag.getInteger("X"), goalTag.getInteger("Y"), new ItemStack(goalTag.getCompoundTag("ItemStack")));
                }

                // Read the completion states
                final NBTTagList completionTags = tag.getTagList("Completion", NBT.TAG_COMPOUND);

                for (int i = 0; i < completionTags.tagCount(); i++) {

                    final NBTTagCompound completionTag = completionTags.getCompoundTagAt(i);

                    final int x = completionTag.getInteger("X");
                    final int y = completionTag.getInteger("Y");

                    final NBTTagList teamsTag = completionTag.getTagList("Teams", NBT.TAG_STRING);

                    for (int j = 0; j < teamsTag.tagCount(); j++) {

                        final Team team = Team.getTeamByName(teamsTag.getStringTagAt(j));
                        this.completionStates[x][y][team.getTeamCorner()] = team;
                    }
                }
            }
        }
    }

    /**
     * Writes the current game state to an nbt tag.
     *
     * @return The tag that contains the state data.
     */
    public NBTTagCompound write () {

        final NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("IsActive", this.isActive());
        tag.setBoolean("HasStarted", this.hasStarted());
        tag.setBoolean("GroupTeams", this.shouldGroupTeams());
        tag.setBoolean("Blackout", this.blackout);
        tag.setLong("StartTime", this.startTime);
        tag.setLong("EndTime", this.endTime);
        tag.setInteger("WinCount", this.winCount);

        if (this.winner != null) {

        	tag.setString("Winner", this.winner.getTeamKey());
        }

        if (this.getTable() != null && this.mode != null) {

            // Write basic game data
            tag.setString("GameMode", this.mode.getModeId().toString());
            tag.setString("GoalTable", this.getTable().getName().toString());

            // Write the goal items
            final NBTTagList goalTags = new NBTTagList();
            tag.setTag("Goals", goalTags);

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final ItemStack goal = this.goals[x][y];
                    final NBTTagCompound goalTag = new NBTTagCompound();
                    goalTag.setInteger("X", x);
                    goalTag.setInteger("Y", y);
                    goalTag.setTag("ItemStack", goal.writeToNBT(new NBTTagCompound()));
                    goalTags.appendTag(goalTag);
                }
            }

            // Write the completion states
            final NBTTagList completionTag = new NBTTagList();
            tag.setTag("Completion", completionTag);

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final NBTTagCompound teamCompletionTag = new NBTTagCompound();
                    teamCompletionTag.setInteger("X", x);
                    teamCompletionTag.setInteger("Y", y);

                    final NBTTagList teamsTag = new NBTTagList();
                    teamCompletionTag.setTag("Teams", teamsTag);
                    for (final Team completedTeam : this.completionStates[x][y]) {

                        if (completedTeam != null) {

                            teamsTag.appendTag(new NBTTagString(completedTeam.getTeamKey()));
                        }
                    }

                    completionTag.appendTag(teamCompletionTag);
                }
            }
        }

        return tag;
    }

    public boolean isHasStarted () {

        return hasStarted;
    }

    public boolean shouldGroupTeams () {

        return groupTeams;
    }

    public boolean isBlackout() {

    	return this.blackout;
    }
}
