package net.darkhax.bingo.commands;

import java.util.List;

import javax.annotation.Nullable;

import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoTeam extends Command {

    @Override
    public String getName () {

        return "team";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.team.usage";
    }

    // [player]
    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        return Team.getTeamNames();
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 1) {

            final Team team = Team.getTeamByName(args[0].toLowerCase());

            if (team != null && sender instanceof EntityPlayer) {

                server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.team.change", sender.getName(), team.getTeamName()));
                BingoPersistantData.setTeam((EntityPlayer) sender, team);
            }

            else {

                throw new WrongUsageException("command.bingo.team.unknown", args[0]);
            }
        }

        else {

            throw new WrongUsageException("command.bingo.team.usage");
        }
    }
}