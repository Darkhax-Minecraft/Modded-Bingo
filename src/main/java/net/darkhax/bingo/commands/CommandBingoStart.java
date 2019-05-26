package net.darkhax.bingo.commands;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBingoStart extends Command {

    @Override
    public String getName () {

        return "start";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "command.bingo.start.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (!BingoMod.GAME_STATE.isActive()) {

            throw new CommandException("command.bingo.info.notactive");
        }

        if (BingoMod.GAME_STATE.isHasStarted()) {

            throw new CommandException("command.bingo.info.alreadystarted");
        }

        BingoMod.GAME_STATE.start();
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.start.started", sender.getDisplayName()));
        BingoMod.NETWORK.sendToAll(new PacketSyncGameState(BingoMod.GAME_STATE.write()));

        for (final EntityPlayerMP player : server.getPlayerList().getPlayers()) {

            this.movePlayer(player);
        }
    }

    private void movePlayer (EntityPlayer player) {

        final int x = MathsUtils.nextIntInclusive(-29_999_900, 29_999_900);
        final int y = MathsUtils.nextIntInclusive(-29_999_900, 29_999_900);

        final BlockPos.MutableBlockPos groundPos = new BlockPos.MutableBlockPos(x, 255, y);
        player.setPositionAndUpdate(x, 255, y);
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 240, 5));

        while (player.world.isAirBlock(groundPos)) {

            groundPos.move(EnumFacing.DOWN);

            if (groundPos.getY() < 1) {

                this.movePlayer(player);
                return;
            }
        }

        groundPos.move(EnumFacing.UP);
        player.setSpawnChunk(groundPos.up(), true, player.dimension);
        player.setPositionAndUpdate(groundPos.getX() + 0.5, groundPos.getY() + 0.5, groundPos.getZ() + 0.5);
    }
}