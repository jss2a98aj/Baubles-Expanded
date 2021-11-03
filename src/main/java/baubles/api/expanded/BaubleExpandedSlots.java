package baubles.api.expanded;

public class BaubleExpandedSlots {
	
	public static final int slotLimit = 20;
	
	private static String[] slots = new String[slotLimit];
	private static int newSlotsRemaining = slotLimit;
	
	static {
		requestAddSlot("AMULET");
		requestAddSlot("RING");
		requestAddSlot("RING");
		requestAddSlot("BELT");
	}
	
	public static boolean requestAddSlot(String type) {
		//TODO: block after GUI init
		if(newSlotsRemaining >= 1) {
			slots[slotLimit - newSlotsRemaining] = type;
			newSlotsRemaining--;
			return true;
		}
		
		return false;
	}

	public static boolean requestRemoveSlot(String type) {
		//TODO: block after GUI init
		for(int slotToCheck = 0; slotToCheck < slotLimit; slotToCheck++) {
			if(slots[slotToCheck].equals(type)) {
				for(int slotToMove = slotToCheck + 1; slotToMove < slotLimit; slotToMove++) {
					slots[slotToMove - 1] = slots[slotToMove];
				}
				slots[slotLimit - 1] = null;
				newSlotsRemaining++;
				return true;
			}
		}
		return false;
	}
	
	public static int totalCurrentSlotsForType(String type) {
		int total = 0;
		for(int slotToCheck = 0; slotToCheck < slotLimit; slotToCheck++) {
			if(slots[slotToCheck].equals(type)) {
				total++;
			}
		}
		return total;
	}
	
	public static int getTotalSlots() {
		return slotLimit - newSlotsRemaining;
	}
	
	public static String getTypeInSlot(int slot) {
		if (slot > 0 & slot < slotLimit) {
			return slots[slot];
		} else {
			return null;
		}
	}

}
