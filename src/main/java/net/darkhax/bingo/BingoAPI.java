package net.darkhax.bingo;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.darkhax.bingo.api.GoalTable;

public class BingoAPI {

	private static final float[][] TEAM_COLORS = new float[][] {getColorArray(Color.RED), getColorArray(Color.GREEN), getColorArray(Color.YELLOW), getColorArray(Color.BLUE)};
	
	private static Map<String, GoalTable> goalTables = new HashMap<>();
	
	public static GoalTable getGoalTable(String name) {
		
		return goalTables.get(name);
	}
	
	public static GoalTable creteGoalTable(String name) {
		
		final GoalTable table = new GoalTable(name);
		goalTables.put(name, table);
		return table;
	}
	
	public static float[] getTeamColors(int team) {
		
		return TEAM_COLORS[team];
	}
	
	private static float[] getColorArray(Color color) {
		
		return new float[] {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
	}
}