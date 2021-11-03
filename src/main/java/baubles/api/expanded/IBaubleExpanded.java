package baubles.api.expanded;

import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

/**
 * This interface should be extended by items that can be worn in bauble slots.
 * Supports multiple original and expanded slot types.
 * 
 * @author jss2a98aj
 */
public interface IBaubleExpanded extends IBauble {
	
	/**
	 * This method returns the types of bauble slots this item can go into.
	 * getBaubleType is ignored when using IBaubleExpanded.
	 * Original slots can be used by putting "AMULET" "RING" or BELT" in the array.
	 */
	public String[] getBaubleTypes (ItemStack itemstack);

}
