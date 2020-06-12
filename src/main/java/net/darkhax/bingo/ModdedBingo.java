package net.darkhax.bingo;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.network.NetworkHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bingo")
public class ModdedBingo {

    public static final String MOD_ID = "bingo";
    public static final NetworkHelper NETWORK = new NetworkHelper(new ResourceLocation(MOD_ID, "main"), "1.0");
    public static final Logger LOG = LogManager.getLogger("Bingo");
    
    public ModdedBingo() {
    	 MinecraftForge.EVENT_BUS.register(this);
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarted);
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStopping);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
    	//NETWORK.register(PacketSyncGameState.class, Side.CLIENT);
    	
    	NETWORK.registerEnqueuedMessage(PacketSyncGoal.class, PacketSyncGoal::encode, PacketSyncGoal::new, PacketSyncGoal::handle);
        
        BingoAPI.loadData();
        //BookshelfRegistry.addCommand(new CommandBingo());
    }

    @SubscribeEvent
    public void serverStarted (FMLServerStartedEvent event) {

        // Read the bingo persistent data from nbt data if it exists, when the server is
        // started.
        final File bingoFile = new File(event.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "bingo.data");

        if (bingoFile.exists()) {

            try {

                final CompoundNBT tag = CompressedStreamTools.read(bingoFile);

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

    @SubscribeEvent
    public void serverStopping (FMLServerStoppingEvent event) {

        // Write the bingo data to the world when the server stops.
        final File bingoFile = new File(event.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "bingo.data");

        try {
            CompressedStreamTools.write(BingoPersistantData.write(), bingoFile);
        }
        catch (final IOException e) {
            LOG.error("Failed to write bingo data. This is not good.");
            LOG.catching(e);
        }
    }
}