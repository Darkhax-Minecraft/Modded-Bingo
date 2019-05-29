package net.darkhax.bingo;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.commands.CommandBingo;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
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

        // Read the bingo persistent data from nbt data if it exists, when the server is
        // started.
        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        if (bingoFile.exists()) {

            try {

                final NBTTagCompound tag = CompressedStreamTools.read(bingoFile);

                if (tag != null) {

                    BingoPersistantData.read(tag);
                }
            }

            catch (final IOException e) {

                LOG.warn("Failed to read bingo data. This is not good.");
                LOG.catching(e);
            }
        }
        
        else {
            
            // reset the game state
            BingoAPI.GAME_STATE.read(null);
        }
    }

    @EventHandler
    public void serverStopping (FMLServerStoppingEvent event) {

        // Write the bingo data to the world when the server stops.
        final File bingoFile = new File(DimensionManager.getCurrentSaveRootDirectory(), "bingo.data");

        try {

            CompressedStreamTools.write(BingoPersistantData.write(), bingoFile);
        }

        catch (final IOException e) {

            LOG.error("Failed to write bingo data. This is not good.");
            LOG.catching(e);
        }
    }
}