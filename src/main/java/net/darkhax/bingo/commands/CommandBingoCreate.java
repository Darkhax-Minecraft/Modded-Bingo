package net.darkhax.bingo.commands;

import java.util.Random;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.goal.GoalTable;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoCreate extends Command {

    @Override
    public String getName () {

        return "create";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.create.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        final GoalTable table = args.length >= 1 ? BingoAPI.getGoalTable(new ResourceLocation(args[0])) : BingoAPI.getGoalTable(new ResourceLocation("bingo:default"));
        final Random random = args.length >= 2 ? new Random(args[1].hashCode()) : new Random();
        BingoMod.GAME_STATE.create(random, table);

        sender.sendMessage(new TextComponentTranslation("command.bingo.create.completed"));
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.create.announce", sender.getDisplayName()));
        BingoMod.NETWORK.sendToAll(new PacketSyncGameState(BingoMod.GAME_STATE.write()));
    }
}