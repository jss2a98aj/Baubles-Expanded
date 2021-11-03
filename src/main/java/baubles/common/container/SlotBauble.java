package baubles.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.expanded.IBaubleExpanded;

public class SlotBauble extends Slot {

	String slotType;

	@Deprecated
    public SlotBauble(IInventory inventory, BaubleType legacyType, int slot, int x, int y) {
        super(inventory, slot, x, y);
		switch(legacyType) {
		case RING:
			slotType = "RING";
			break;
		case AMULET:
			slotType = "AMULET";
			break;
		case BELT:
			slotType = "BELT";
			break;
		default:
			slotType = "UNKNOWN";	
		}
    }
    
    public SlotBauble(IInventory inventory, String type, int slot, int x, int y) {
        super(inventory, slot, x, y);
        this.slotType = type;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
    	if (stack == null || slotType == null) {
			return false;
		}

		Item item = stack.getItem();
		if(!(item instanceof IBauble) || !((IBauble) item).canEquip(stack, ((InventoryBaubles)inventory).player.get())) {
			return false;
		}

		String[] types;
		if(item instanceof IBaubleExpanded) {
			types = ((IBaubleExpanded)item).getBaubleTypes(stack);
		} else {
			BaubleType legacyType = ((IBauble)item).getBaubleType(stack);
			String type;
			switch(legacyType) {
			case RING:
				type = "RING";
				break;
			case AMULET:
				type = "AMULET";
				break;
			case BELT:
				type = "BELT";
				break;
			default:
				type = "UNKNOWN";	
			}
			types = new String[] {type};
		}
		
		for(String type : types) {
			if(type.equals(slotType)) {
				return true;
			}
		}

		return false;
    }

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return this.getStack()!=null && ((IBauble)this.getStack().getItem()).canUnequip(this.getStack(), player);
	}

	@Override
    public int getSlotStackLimit() {
        return 1;
    }

}
