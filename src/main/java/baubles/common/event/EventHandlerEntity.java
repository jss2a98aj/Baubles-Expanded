package baubles.common.event;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import baubles.api.IBauble;
import baubles.common.Baubles;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

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
		
		File mainFile, backupFile;
		final String fileExtension = "baub";
		final String fileExtensionBackup = "baubback";
		
		// look for normal files first
		mainFile = getPlayerFile(fileExtension, directory, player.getCommandSenderName());
		backupFile = getPlayerFile(fileExtensionBackup, directory, player.getCommandSenderName());
		
		// look for uuid files when normal file missing
		if (!mainFile.exists()) {
			File filep = getPlayerFile(fileExtension, directory, player.getGameProfile().getId().toString());
			if (filep.exists()) {
				try {
					Files.copy(filep, mainFile);
					Baubles.log.info("Using and converting UUID Baubles savefile for " + player.getCommandSenderName());
					filep.delete();
					File fb = getPlayerFile(fileExtensionBackup, directory, player.getGameProfile().getId().toString());
					if (fb.exists()) fb.delete();					
				} catch (IOException e) {}
			}
		}

		PlayerHandler.loadPlayerBaubles(player, mainFile, backupFile);
		EventHandlerNetwork.syncBaubles(player);
	}
	
	public File getPlayerFile(String extension, File playerDirectory, String playerName) {
        if("dat".equals(extension)) throw new IllegalArgumentException("The extension 'dat' is reserved");
        return new File(playerDirectory, playerName + "." + extension);
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
