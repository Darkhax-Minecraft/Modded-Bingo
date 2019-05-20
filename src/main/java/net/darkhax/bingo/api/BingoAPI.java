package net.darkhax.bingo.api;

import java.util.HashMap;
import java.util.Map;

import net.darkhax.bingo.api.goal.GoalTable;

public class BingoAPI {

    private static Map<String, GoalTable> goalTables = new HashMap<>();

    public static GoalTable getGoalTable (String name) {

        return goalTables.get(name);
    }

    public static GoalTable creteGoalTable (String name) {

        final GoalTable table = new GoalTable(name);
        goalTables.put(name, table);
        return table;
    }
}