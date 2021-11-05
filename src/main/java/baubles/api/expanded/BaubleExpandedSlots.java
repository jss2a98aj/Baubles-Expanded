package baubles.api.expanded;

import baubles.api.BaubleType;

public class BaubleExpandedSlots {
	//The total number of slots that can be added.
	public static final int slotLimit = 20;

	//All "default" types will have slot backgrounds registered automatically.
	public static final String ringType = "ring";
	public static final String amuletType = "amulet";
	public static final String beltType = "belt";
	public static final String headType = "head";
	public static final String bodyType = "body";
	public static final String charmType = "charm";
	public static final String gauntletType = "gauntlet";
	public static final String capeType = "cape";

	public static final String[] defaultTypes = {
		ringType,
		amuletType,
		beltType,
		headType,
		bodyType,
		charmType,
		gauntletType,
		capeType
	};
	
	private static String[] slots = new String[slotLimit];
	private static int newSlotsRemaining = slotLimit;

	
	/**
	* Attempts to add a slot, but will not if there are no slots left.
	* 
	* @param type  The type of the slot being added
	* @return      If adding a slot was successful
	*/
	public static boolean tryAddSlot(String type) {
		if(newSlotsRemaining >= 1) {
			slots[slotLimit - newSlotsRemaining] = type;
			newSlotsRemaining--;
			return true;
		}

		return false;
	}

	/**
	* Remove a slot of the specified type if one could be found.
	* 
	* @param type  The type of the slot being removed
	* @return      If removing a slot was successful
	*/
	public static boolean tryRemoveSlot(String type) {
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

	
	/**
	* Returns the currently number of slots matching the specified type.
	* 
	* @param type  The type of slot being counted
	* @return      The current total number of matching slots found
	*/
	public static int totalCurrentSlotsOfType(String type) {
		int total = 0;
		for(int slotToCheck = 0; slotToCheck < slotLimit; slotToCheck++) {
			if(slots[slotToCheck].equals(type)) {
				total++;
			}
		}
		return total;
	}

	/**
	* Returns the number of slots that are currently used.
	* 
	* @return      The total number of slots currently used
	*/
	public static int getTotalSlots() {
		return slotLimit - newSlotsRemaining;
	}

	
	/**
	* Returns the type of the specified slot or null if none or out of range.
	* 
	* @param slot  The slot to check
	* @return      The type in the specified slot or "unknown"
	*/
	public static String getSlotType(int slot) {
		if (slot >= 0 & slot < slotLimit) {
			return slots[slot];
		} else {
			return "unknown";
		}
	}

	/**
	* Returns the type based on the specified BaubleType.
	* 
	* @param type  The BaubleType get a matching type from
	* @return      The type matching the BaubleType or "unknown"
	*/
	public static String getTypeStringFromBaubleType(BaubleType type) {
		switch(type) {
		case RING:
			return ringType;
		case AMULET:
			return amuletType;
		case BELT:
			return beltType;
		}
		return "unknown";
	}

}
