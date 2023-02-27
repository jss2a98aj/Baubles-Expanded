package baubles.common.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.google.common.io.Files;

import baubles.common.Baubles;
import baubles.common.container.InventoryBaubles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerHandler {

	private static HashMap<String, InventoryBaubles> playerBaubles = new HashMap<>();

	public static void clearPlayerBaubles(EntityPlayer player) {
		playerBaubles.remove(player.getCommandSenderName());
	}

	public static InventoryBaubles getPlayerBaubles(EntityPlayer player) {
		if (!playerBaubles.containsKey(player.getCommandSenderName())) {
			InventoryBaubles inventory = new InventoryBaubles(player);
			playerBaubles.put(player.getCommandSenderName(), inventory);
		}
		return playerBaubles.get(player.getCommandSenderName());
	}

	public static void setPlayerBaubles(EntityPlayer player, InventoryBaubles inventory) {
		playerBaubles.put(player.getCommandSenderName(), inventory);
	}

	public static void loadPlayerBaubles(EntityPlayer player, File mainFile, File backupFile) {
		if(player != null && !player.worldObj.isRemote) {
			try {
				NBTTagCompound data = null;
				boolean save = false;
				if(mainFile != null && mainFile.exists()) {
					try {
						FileInputStream fileinputstream = new FileInputStream(mainFile);
						data = CompressedStreamTools.readCompressed(fileinputstream);
						fileinputstream.close();
					} catch (Exception loadMainException) {
						loadMainException.printStackTrace();
					}
				}

				if(mainFile == null || !mainFile.exists() || data == null || data.hasNoTags()) {
					Baubles.log.warn("Data not found for "
							+ player.getCommandSenderName()
							+ ". Trying to load backup data."
					);
					if(backupFile != null && backupFile.exists()) {
						try {
							FileInputStream fileinputstream = new FileInputStream(backupFile);
							data = CompressedStreamTools.readCompressed(fileinputstream);
							fileinputstream.close();
							save = true;
						} catch(Exception loadBackupException) {
							loadBackupException.printStackTrace();
						}
					}
				}

				if(data != null) {
					InventoryBaubles inventory = new InventoryBaubles(player);
					inventory.readNBT(data);
					playerBaubles.put(player.getCommandSenderName(), inventory);
					if(save) {
						savePlayerBaubles(player, mainFile, backupFile);
					}
				}
			} catch(Exception loadException) {
				Baubles.log.fatal("Error loading baubles inventory");
				loadException.printStackTrace();
			}
		}
	}

	public static void savePlayerBaubles(EntityPlayer player, File mainFile, File backupFile) {
		if(player != null && !player.worldObj.isRemote) {
			try {
				if (mainFile != null && mainFile.exists()) {
					try {
						Files.copy(mainFile, backupFile);
					} catch (Exception saveBackupException) {
						Baubles.log.error("Could not backup old baubles file for player " + player.getCommandSenderName());
					}
				}

				try {
					if(mainFile != null) {
						InventoryBaubles inventory = getPlayerBaubles(player);
						NBTTagCompound data = new NBTTagCompound();
						inventory.saveNBT(data);

						FileOutputStream fileoutputstream = new FileOutputStream(mainFile);
						CompressedStreamTools.writeCompressed(data, fileoutputstream);
						fileoutputstream.close();
					}
				} catch(Exception saveMainException) {
					Baubles.log.error("Could not save baubles file for player " + player.getCommandSenderName());
					saveMainException.printStackTrace();
					if (mainFile.exists()) {
						try {
							mainFile.delete();
						} catch (Exception ignored) {
						}
					}
				}
			} catch(Exception saveException) {
				Baubles.log.fatal("Error saving baubles inventory");
				saveException.printStackTrace();
			}
		}
	}

}
