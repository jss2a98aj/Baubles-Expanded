package baubles.common.event;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class EventHandlerNetwork {

	@SubscribeEvent
	public void playerLoggedInEvent (PlayerEvent.PlayerLoggedInEvent event)    {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER)        {
			syncBaubles(event.player);
		}
	}

	public static void syncBaubles(EntityPlayer player) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
		for (int i = 0; i < baubles.getSizeInventory(); i++) {
			baubles.syncSlotToClients(i);
		}
	}


}
