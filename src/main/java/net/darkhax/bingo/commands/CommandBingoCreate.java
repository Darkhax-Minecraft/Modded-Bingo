package net.darkhax.bingo.commands;

import java.util.Random;

import net.darkhax.bingo.BingoAPI;
import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.GoalTable;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoCreate extends Command {

	@Override
	public String getName() {

		return "create";
	}

	@Override
	public String getUsage(ICommandSender sender) {

		return "command.bingo.create.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		GoalTable table = args.length >= 1 ? BingoAPI.getGoalTable(args[0]) : BingoMod.DEFAULT;		
		Random random = args.length >= 2 ? new Random(args[1].hashCode()) : new Random();
		BingoMod.GAME_STATE.create(random, table);
		
		sender.sendMessage(new TextComponentTranslation("command.bingo.create.completed"));
		server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.create.announce", sender.getDisplayName()));
	}
}