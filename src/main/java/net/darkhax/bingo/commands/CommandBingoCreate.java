package net.darkhax.bingo.commands;
/*
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.game.GameMode;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
        final boolean groupTeams = args.length >= 2 ? parseBoolean(args[1]) : true;
        final boolean blackout = args.length >= 3 ? parseBoolean(args[2]) : false;
        final Random random = args.length >= 4 ? new Random(args[3].hashCode()) : new Random();
        
        if (gameMode != null) {
            
        	if (BingoAPI.GAME_STATE.hasStarted() || BingoAPI.GAME_STATE.isActive()) {
        		
                BingoAPI.GAME_STATE.end();
                server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.stop.stopped", sender.getDisplayName()));
                ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        	}
        	
            BingoAPI.GAME_STATE.create(random, gameMode, groupTeams, blackout);

            server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.create.announce" + (blackout ? ".blackout" : ""), sender.getDisplayName()));
            ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        }           
        
        else {
            
            throw new CommandException("command.bingo.create.unknown");
        }
    }
    
    @Override
    public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        if (args.length == 1) {

            return getListOfStringsMatchingLastWord(args, BingoAPI.getGameModeKeys());
        }
        
        return super.getTabCompletions(server, sender, args, targetPos);
    }
    
    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {

        return this.getRequiredPermissionLevel() <= 0 || super.checkPermission(server, sender);
    }
}
*/