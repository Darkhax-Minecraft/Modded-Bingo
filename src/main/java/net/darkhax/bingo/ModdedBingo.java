package net.darkhax.bingo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.commands.CommandBingo;
import net.darkhax.bingo.data.BingoDataReader;
import net.darkhax.bingo.data.BingoPersistantData;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.darkhax.bingo.network.PacketSyncGoal;
import net.darkhax.bookshelf.network.NetworkHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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
    	 //TODO: needed?
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarted);
    	 FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStopping);
    }
    
    private void setup(final FMLCommonSetupEvent event) {    	
    	NETWORK.registerEnqueuedMessage(PacketSyncGoal.class, PacketSyncGoal::encode, PacketSyncGoal::new, PacketSyncGoal::handle);
    	NETWORK.registerEnqueuedMessage(PacketSyncGameState.class, PacketSyncGameState::encode, PacketSyncGameState::new, PacketSyncGameState::handle);
    }
    
    @SubscribeEvent
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    	event.getServer().getResourceManager().addReloadListener(new BingoDataReader());
    }
    
    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
    	CommandBingo.initializeCommands(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public void serverStarted (FMLServerStartedEvent event) {

        // Read the bingo persistent data from nbt data if it exists, when the server is
        // started.
        final File bingoFile = new File(event.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "bingo.data");

        if (bingoFile.exists()) {
            try{
            	byte[] data = Files.readAllBytes(bingoFile.toPath());
            	BingoPersistantData.read(new PacketBuffer(Unpooled.wrappedBuffer(data)));
            }
            catch (final IOException e) {
                LOG.warn("Failed to read bingo data. This is not good.");
                LOG.catching(e);
            }
        }
        
        else {
            
            // reset the game state
            BingoAPI.GAME_STATE.read((PacketBuffer)null);
        }
    }

    @SubscribeEvent
    public void serverStopping (FMLServerStoppingEvent event) {

        // Write the bingo data to the world when the server stops.
        final File bingoFile = new File(event.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "bingo.data");

        try(FileOutputStream fos = new FileOutputStream(bingoFile)) {
        	ByteBuf byteBuf = Unpooled.buffer();
        	BingoPersistantData.write(new PacketBuffer(byteBuf));
        	fos.write(byteBuf.array());
        }
        catch (final IOException e) {
            LOG.error("Failed to write bingo data. This is not good.");
            LOG.catching(e);
        }
    }
}