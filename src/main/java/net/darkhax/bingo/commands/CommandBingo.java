package net.darkhax.bingo.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandBingo extends CommandTreeBase {

    public CommandBingo () {

        this.addSubcommand(new CommandBingoTeam());
        this.addSubcommand(new CommandBingoCreate());
        this.addSubcommand(new CommandBingoStart());
        this.addSubcommand(new CommandBingoReroll());
        this.addSubcommand(new CommandBingoStop());
        this.addSubcommand(new CommandBingoReload());
        this.addSubcommand(new CommandBingoItem());
        this.addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName () {

        return "bingo";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bingo";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {

        return this.getRequiredPermissionLevel() <= 0 || super.checkPermission(server, sender);
    }
}
