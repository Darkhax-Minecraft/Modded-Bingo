package net.darkhax.bingo.commands;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoStop extends Command {

    @Override
    public String getName () {

        return "stop";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.stop.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        BingoAPI.GAME_STATE.end();
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.stop.stopped", sender.getDisplayName()));
    }
}