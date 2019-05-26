package net.darkhax.bingo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.goal.Goal;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.api.goal.GoalTier;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants.NBT;

public class GameState {

    private GoalTable table;
    private Random random;
    private Goal[][] goals = new Goal[5][5];
    private Team[][][] completionStates = new Team[5][5][4];
    private boolean isActive = false;
    private boolean hasStarted = false;
    private Team winner = null;

    public void read (NBTTagCompound tag) {

        this.reset();

        if (tag != null) {

            this.table = BingoAPI.getGoalTable(tag.getString("GoalTable"));

            if (this.table != null) {

                final NBTTagList goalsTag = tag.getTagList("Goals", NBT.TAG_COMPOUND);

                for (int i = 0; i < goalsTag.tagCount(); i++) {

                    final NBTTagCompound goalTag = goalsTag.getCompoundTagAt(i);

                    final GoalTier tier = this.getTable().getTierByName(goalTag.getString("Tier"));
                    final Goal goal = tier.getGoalByName(goalTag.getString("Goal"));
                    this.setGoal(goalTag.getInteger("X"), goalTag.getInteger("Y"), goal);
                }

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

            this.isActive = tag.getBoolean("IsActive");
            this.hasStarted = tag.getBoolean("HasStarted");
        }
    }

    public NBTTagCompound write () {

        final NBTTagCompound tag = new NBTTagCompound();

        if (this.getTable() != null) {

            tag.setString("GoalTable", this.getTable().getName());

            final NBTTagList goals = new NBTTagList();
            tag.setTag("Goals", goals);

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final Goal goal = this.goals[x][y];
                    final NBTTagCompound goalTag = new NBTTagCompound();
                    goalTag.setInteger("X", x);
                    goalTag.setInteger("Y", y);
                    goalTag.setString("Tier", goal.getTier());
                    goalTag.setString("Goal", goal.getName());
                    goals.appendTag(goalTag);
                }
            }

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

        tag.setBoolean("IsActive", this.isActive());
        tag.setBoolean("HasStarted", this.isHasStarted());

        return tag;
    }

    public void create (Random random, GoalTable table) {

        this.reset();
        this.table = table;
        this.random = random;
        this.rollGoals(random);
        this.isActive = true;
    }

    public void start () {

        this.hasStarted = true;
    }

    public void end () {

        this.hasStarted = false;
        this.isActive = false;
        this.reset();
    }

    public void reset () {

        this.goals = new Goal[5][5];
        this.completionStates = new Team[5][5][4];
        this.hasStarted = false;
        this.isActive = false;
        this.table = null;
        this.winner = null;
    }

    public Goal getGoal (int x, int y) {

        return this.goals[x][y];
    }

    public void setGoal (int x, int y, Goal goal) {

        this.goals[x][y] = goal;
    }

    public void setGoalComplete (EntityPlayerMP player, int x, int y) {

        final Team playerTeam = BingoPersistantData.getTeam(player);

        if (player != null && this.completionStates[x][y][playerTeam.getTeamCorner()] == null) {

            this.completionStates[x][y][playerTeam.getTeamCorner()] = playerTeam;

            final Goal goal = this.getGoal(x, y);

            final ITextComponent playerName = player.getDisplayName();
            playerName.getStyle().setColor(playerTeam.getTeamColorText());

            final ITextComponent itemName = goal.getTarget().getTextComponent();
            itemName.getStyle().setColor(TextFormatting.GRAY);

            player.server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.player.obtained", playerName, itemName));
            BingoMod.NETWORK.sendToAll(new PacketSyncGoal(x, y, playerTeam.getTeamKey()));
            playerTeam.spawnFirework(player);
        }

        this.updateWinState(player.server);
    }

    public boolean hasCompletedGoal (int x, int y, Team team) {

        return this.completionStates[x][y][team.getTeamCorner()] != null;
    }

    public Team[] getCompletionStats (int x, int y) {

        return this.completionStates[x][y];
    }

    public void updateWinState (MinecraftServer server) {

        this.winner = this.checkWinState();

        if (this.winner != null && this.isHasStarted() && this.isActive()) {

            this.hasStarted = false;

            server.getPlayerList().sendMessage(new TextComponentTranslation("bingo.winner", this.winner.getTeamName()));

            for (final EntityPlayerMP player : server.getPlayerList().getPlayers()) {

                this.winner.spawnFirework(player);
                this.winner.spawnFirework(player);
                this.winner.spawnFirework(player);
            }
        }
    }

    public Team checkWinState () {

        for (final Team team : BingoMod.TEAMS) {

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

                    return team;
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

        return null;
    }

    public void onPlayerPickupItem (EntityPlayerMP player, ItemStack stack) {

        if (BingoMod.GAME_STATE.isHasStarted()) {

            for (int x = 0; x < 5; x++) {

                for (int y = 0; y < 5; y++) {

                    final Goal goal = this.getGoal(x, y);

                    if (StackUtils.areStacksSimilar(goal.getTarget(), stack)) {

                        this.setGoalComplete(player, x, y);
                    }
                }
            }
        }
    }

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

        Collections.shuffle(generatedGoals);

        int xOff = 0;
        int yOff = 0;

        for (final Goal goal : generatedGoals) {

            this.setGoal(xOff, yOff, goal);

            xOff++;
            if (xOff == 5) {

                xOff = 0;
                yOff++;
            }
        }
    }

    public GoalTable getTable () {

        return this.table;
    }

    public void setTable (GoalTable table) {

        this.table = table;
    }

    public Random getRandom () {

        return this.random;
    }

    public void setRandom (Random random) {

        this.random = random;
    }

    public Goal[][] getGoals () {

        return this.goals;
    }

    public void setGoals (Goal[][] goals) {

        this.goals = goals;
    }

    public Team[][][] getCompletionStates () {

        return this.completionStates;
    }

    public void setCompletionStates (Team[][][] completionStates) {

        this.completionStates = completionStates;
    }

    public boolean isHasStarted () {

        return this.hasStarted;
    }

    public void setHasStarted (boolean hasStarted) {

        this.hasStarted = hasStarted;
    }

    public boolean isActive () {

        return this.isActive;
    }
}