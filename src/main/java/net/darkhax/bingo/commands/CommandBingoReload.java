package net.darkhax.bingo.commands;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoReload extends Command {

    @Override
    public String getName () {

        return "reload";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.reload.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (BingoAPI.GAME_STATE.isActive()) {

            BingoAPI.GAME_STATE.end();
            server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.stop.stopped", sender.getDisplayName()));
            ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        }
        BingoAPI.loadData();
        ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.reload.reloaded", sender.getDisplayName()));
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 4;
    }

    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {

        return this.getRequiredPermissionLevel() <= 0 || super.checkPermission(server, sender);
    }
}
