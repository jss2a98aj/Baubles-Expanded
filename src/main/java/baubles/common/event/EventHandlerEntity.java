package baubles.common.event;

import java.io.File;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import baubles.api.IBauble;
import baubles.common.Baubles;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.google.common.io.Files;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerEntity {

	// player directory
	private File playerDirectory;
	
	@SubscribeEvent
	public void playerTick(PlayerEvent.LivingUpdateEvent event) {

		// player events
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			
			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			for (int a = 0; a < baubles.getSizeInventory(); a++) {
				if (baubles.getStackInSlot(a) != null
						&& baubles.getStackInSlot(a).getItem() instanceof IBauble) {
					((IBauble) baubles.getStackInSlot(a).getItem()).onWornTick(
							baubles.getStackInSlot(a), player);
				}
			}

		}

	}

	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if (event.entity instanceof EntityPlayer
				&& !event.entity.worldObj.isRemote
				&& !event.entity.worldObj.getGameRules()
						.getGameRuleBooleanValue("keepInventory")) {
			PlayerHandler.getPlayerBaubles(event.entityPlayer).dropItemsAt(
					event.drops,event.entityPlayer);
		}

	}

	@SubscribeEvent
	public void playerLoad(PlayerEvent.LoadFromFile event) {
		playerLoadDo(event.entityPlayer, event.playerDirectory, event.entityPlayer.capabilities.isCreativeMode);
		playerDirectory = event.playerDirectory;
	}
	
	private void playerLoadDo(EntityPlayer player, File directory, Boolean gamemode) {
		PlayerHandler.clearPlayerBaubles(player);
		
		File file1, file2;
		String fileName, fileNameBackup;
		fileName = "baub";
		fileNameBackup = "baubback";
		
		// look for normal files first
		file1 = getPlayerFile(fileName, directory, player.getCommandSenderName());
		file2 = getPlayerFile(fileNameBackup, directory, player.getCommandSenderName());
		
		// look for uuid files when normal file missing
		if (!file1.exists()) {
			File filep = getPlayerFileUUID(fileName, directory, player.getGameProfile().getId().toString());
			if (filep.exists()) {
				try {
					Files.copy(filep, file1);					
					Baubles.log.info("Using and converting UUID Baubles savefile for "+player.getCommandSenderName());
					filep.delete();
					File fb = getPlayerFileUUID(fileNameBackup, directory, player.getGameProfile().getId().toString());
					if (fb.exists()) fb.delete();					
				} catch (IOException e) {}
			}
		}
		
		PlayerHandler.loadPlayerBaubles(player, file1, file2);
		EventHandlerNetwork.syncBaubles(player);
	}
	
	public File getPlayerFile(String suffix, File playerDirectory, String playername)
    {
        if ("dat".equals(suffix)) throw new IllegalArgumentException("The suffix 'dat' is reserved");
        return new File(playerDirectory, playername+"."+suffix);
    }
	
	public File getPlayerFileUUID(String suffix, File playerDirectory, String playerUUID)
    {
        if ("dat".equals(suffix)) throw new IllegalArgumentException("The suffix 'dat' is reserved");
        return new File(playerDirectory, playerUUID+"."+suffix);
    }

	@SubscribeEvent
	public void playerSave(PlayerEvent.SaveToFile event) {
		playerSaveDo(event.entityPlayer, event.playerDirectory, event.entityPlayer.capabilities.isCreativeMode);
	}
	
	private void playerSaveDo(EntityPlayer player, File directory, Boolean gamemode) {
		PlayerHandler.savePlayerBaubles(player, 
				getPlayerFile("baub", directory, player.getCommandSenderName()), 
				getPlayerFile("baubback", directory, player.getCommandSenderName()));
	}

}
