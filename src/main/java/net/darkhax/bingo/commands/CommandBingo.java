package net.darkhax.bingo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;

public class CommandBingo {

	public static void initializeCommands (CommandDispatcher<CommandSource> dispatcher) {		
		dispatcher.register(
	            LiteralArgumentBuilder.<CommandSource>literal("bingo")
	            .then(CommandBingoCreate.register())
	            .then(CommandBingoTeam.register())
	            .then(CommandBingoReroll.register())
	        );
	}
   
}