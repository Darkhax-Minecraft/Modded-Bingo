package net.darkhax.bingo;

import net.darkhax.bingo.api.BingoAPI;
import net.darkhax.bingo.network.PacketSyncGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber(modid = "bingo")
public class BingoEventHandler {

    @SubscribeEvent
    public static void onPlayerPickupItem (ItemPickupEvent event) {

        if (event.player instanceof EntityPlayerMP && BingoAPI.GAME_STATE.hasStarted()) {

            BingoAPI.GAME_STATE.onPlayerPickupItem((EntityPlayerMP) event.player, event.getOriginalEntity().getItem());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {

        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {

            BingoMod.NETWORK.sendTo(new PacketSyncGameState(BingoAPI.GAME_STATE.write()), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick (TickEvent.PlayerTickEvent event) {

        // Only check once a second
        if (event.player.ticksExisted % 20 == 0 && event.player instanceof EntityPlayerMP) {

            for (final ItemStack stack : event.player.inventory.mainInventory) {

                if (!stack.isEmpty()) {

                    BingoAPI.GAME_STATE.onPlayerPickupItem((EntityPlayerMP) event.player, stack);
                }
            }
        }
    }
}