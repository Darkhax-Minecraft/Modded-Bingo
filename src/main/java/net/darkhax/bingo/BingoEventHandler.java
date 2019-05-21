package net.darkhax.bingo;

import net.darkhax.bingo.network.PacketSyncGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber(modid = "bingo")
public class BingoEventHandler {

    @SubscribeEvent
    public static void onPlayerPickupItem (ItemPickupEvent event) {

        if (event.player instanceof EntityPlayerMP && BingoMod.GAME_STATE.isHasStarted()) {

            BingoMod.GAME_STATE.onPlayerPickupItem((EntityPlayerMP) event.player, event.getStack());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {
        
        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {
            
            BingoMod.NETWORK.sendTo(new PacketSyncGameState(BingoMod.GAME_STATE.write()), (EntityPlayerMP) event.player);
        }
    }
}