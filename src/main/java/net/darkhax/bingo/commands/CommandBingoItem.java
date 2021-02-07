package net.darkhax.bingo.commands;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandBingoItem extends Command {

    @Override
    public String getName () {

        return "item";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.item.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        final int x = args.length >= 2 ? parseInt(args[0]) : 0;
        final int y = args.length >= 2 ? parseInt(args[1]) : 0;

        if (BingoAPI.GAME_STATE.isActive()) {

          if (x >= 1 && x <= 5 && y >= 1 && y <= 5) {
            final ItemStack goal = BingoAPI.GAME_STATE.getDGoal(x-1,y-1);
            final ITextComponent itemName = goal.getTextComponent();
            itemName.getStyle().setColor(TextFormatting.GRAY);
            sender.sendMessage(new TextComponentTranslation("command.bingo.item.display", itemName));
          } else {
            throw new CommandException("command.bingo.item.invalid");
          }
        } else {
          throw new CommandException("command.bingo.item.stopped");
        }
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
