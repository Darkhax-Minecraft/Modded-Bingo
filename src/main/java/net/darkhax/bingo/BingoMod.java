package net.darkhax.bingo;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.api.team.Team;
import net.darkhax.bingo.commands.CommandBingo;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bingo.data.GameState;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "bingo", name = "Bingo", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@", dependencies = "required-after:bookshelf@[2.3.573,)")
public class BingoMod {

    public static final Team TEAM_RED = new Team(TextFormatting.RED, 0, EnumDyeColor.RED);
    public static final Team TEAM_YELLOW = new Team(TextFormatting.YELLOW, 1, EnumDyeColor.YELLOW);
    public static final Team TEAM_GREEN = new Team(TextFormatting.GREEN, 2, EnumDyeColor.GREEN);
    public static final Team TEAM_BLUE = new Team(TextFormatting.BLUE, 3, EnumDyeColor.BLUE);
    public static final Team[] TEAMS = new Team[] { TEAM_RED, TEAM_YELLOW, TEAM_GREEN, TEAM_BLUE };

    public static final GameState GAME_STATE = new GameState();
    public static final String MOD_ID = "bingo";
    public static final NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static final Logger LOG = LogManager.getLogger("Bingo");

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        NETWORK.register(PacketSyncGameState.class, Side.CLIENT);
        NETWORK.register(PacketSyncGoal.class, Side.CLIENT);
    }

    @EventHandler
    public void init (FMLInitializationEvent event) {

        BingoAPI.loadData();
        BookshelfRegistry.addCommand(new CommandBingo());
    }

    @EventHandler
    public void serverStarted (FMLServerStartedEvent event) {

        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        if (bingoFile.exists()) {

            try {

                final NBTTagCompound tag = CompressedStreamTools.read(bingoFile);

                if (tag != null) {

                    BingoPersistantData.read(tag);
                }
            }

            catch (final IOException e) {

                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void serverStopping (FMLServerStoppingEvent event) {

        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        try {

            CompressedStreamTools.write(BingoPersistantData.write(), bingoFile);
        }

        catch (final IOException e) {

            e.printStackTrace();
        }
    }
}