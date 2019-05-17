package net.darkhax.bingo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

@EventBusSubscriber(modid = "bingo")
public class BingoEventHandler {

	@SubscribeEvent
	public static void onPlayerPickupItem(ItemPickupEvent event) {
		
		if (event.player instanceof EntityPlayerMP) {
			
			BingoMod.currentGame.onPlayerPickupItem((EntityPlayerMP) event.player, event.getStack());
		}
	}
}
