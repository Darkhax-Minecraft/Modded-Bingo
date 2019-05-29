package net.darkhax.bingo.commands;

import net.darkhax.bingo.BingoMod;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bookshelf.command.Command;
import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

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

        BingoMod.GAME_STATE.start(server);
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.start.started", sender.getDisplayName()));
        BingoMod.NETWORK.sendToAll(new PacketSyncGameState(BingoMod.GAME_STATE.write()));

        for (final EntityPlayerMP player : server.getPlayerList().getPlayers()) {

            for (final SpawnEffect effect : BingoMod.GAME_STATE.getMode().getSpawnEffect()) {

                effect.onPlayerSpawn(player, this.getRandomPosition(player.world));
            }
        }
    }

    private BlockPos getRandomPosition (World world) {

        final int x = MathsUtils.nextIntInclusive(-29_999_900, 29_999_900);
        final int z = MathsUtils.nextIntInclusive(-29_999_900, 29_999_900);

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 255, z);

        while (world.isAirBlock(pos)) {

            pos.move(EnumFacing.DOWN);

            if (pos.getY() <= 10) {

                return pos;
            }
        }

        return pos.move(EnumFacing.UP);
    }
}