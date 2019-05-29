package net.darkhax.bingo.commands;

import java.util.Random;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.game.GameMode;
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

        final GameMode gameMode = args.length >= 1 ? BingoAPI.getGameMode(new ResourceLocation(args[0])) : BingoAPI.getGameMode(new ResourceLocation("bingo:default"));
        final Random random = args.length >= 2 ? new Random(args[1].hashCode()) : new Random();
        
        if (gameMode != null) {
            
            BingoAPI.GAME_STATE.create(random, gameMode);

            server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.create.announce", sender.getDisplayName()));
            BingoMod.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        }
            
        
        else {
            
            throw new CommandException("command.bingo.create.unknown");
        }
    }
}