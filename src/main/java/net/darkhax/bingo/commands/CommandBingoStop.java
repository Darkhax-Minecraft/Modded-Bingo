package net.darkhax.bingo.commands;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
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

        if (BingoAPI.GAME_STATE.isActive()) {
            
            BingoAPI.GAME_STATE.end();
            server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.stop.stopped", sender.getDisplayName()));
            BingoMod.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        }
        
        else {
            
            throw new CommandException("command.bingo.stop.nogame");
        }
    }
}