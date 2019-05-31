package net.darkhax.bingo.commands;

import java.util.HashMap;
import java.util.Map;

import net.darkhax.bingo.ModdedBingo;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.effects.spawn.SpawnEffect;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.data.BingoPersistantData;
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

        if (!BingoAPI.GAME_STATE.isActive()) {

            throw new CommandException("command.bingo.info.notactive");
        }

        if (BingoAPI.GAME_STATE.hasStarted() || BingoAPI.GAME_STATE.getWinner() != null) {

            throw new CommandException("command.bingo.info.alreadystarted");
        }

        BingoAPI.GAME_STATE.start(server);
        server.getPlayerList().sendMessage(new TextComponentTranslation("command.bingo.start.started", sender.getDisplayName()));
        ModdedBingo.NETWORK.sendToAll(new PacketSyncGameState(BingoAPI.GAME_STATE.write()));

        Map<Team, BlockPos> teamPositions = new HashMap<>();
       
        if (BingoAPI.GAME_STATE.shouldGroupTeams()) {
            
            for (Team team : BingoAPI.TEAMS) {
                
                teamPositions.put(team, getRandomPosition(sender.getEntityWorld(), 0));
            }
        }
        
        for (final EntityPlayerMP player : server.getPlayerList().getPlayers()) {

            BlockPos spawnPos = BingoAPI.GAME_STATE.shouldGroupTeams() ? teamPositions.get(BingoPersistantData.getTeam(player)) : this.getRandomPosition(player.world, 0);
            
            for (final SpawnEffect effect : BingoAPI.GAME_STATE.getMode().getSpawnEffect()) {

                effect.onPlayerSpawn(player, spawnPos);
            }
        }
    }

    private BlockPos getRandomPosition (World world, int depth) {

        final int x = MathsUtils.nextIntInclusive(-3_000_000, 3_000_000);
        final int z = MathsUtils.nextIntInclusive(-3_000_000, 3_000_000);

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 255, z);

        while (world.isAirBlock(pos)) {

            pos.move(EnumFacing.DOWN);

            if (pos.getY() <= 10) {

                return depth == 100 ? pos : getRandomPosition(world, depth + 1);
            }
        }

        return !world.isSideSolid(pos, EnumFacing.UP) && depth < 100 ? getRandomPosition(world, depth + 1) : pos.move(EnumFacing.UP);
    }
}