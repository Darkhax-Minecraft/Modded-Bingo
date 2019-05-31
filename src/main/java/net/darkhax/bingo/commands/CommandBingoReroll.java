package net.darkhax.bingo.commands;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandBingoReroll extends Command {

    @Override
    public String getName () {

        return "reroll";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.reroll.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (!BingoAPI.GAME_STATE.isActive()) {

            throw new CommandException("command.bingo.info.notactive");
        }

        BingoAPI.GAME_STATE.rollGoals(BingoAPI.GAME_STATE.getRandom());
        ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));
    }
}