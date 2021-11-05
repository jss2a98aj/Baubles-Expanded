package baubles.api.expanded;

import java.util.ArrayList;

import baubles.api.BaubleType;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;

public class BaubleExpandedSlots {
    //The total number of slots that can be added.
    public static final int slotLimit = 20;

    //Fallback for if a slot type cannot be found
    public static final String unknown = "unknown";
    //All "default" types will be registered automatically.
    public static final String ringType = "ring";
    public static final String amuletType = "amulet";
    public static final String beltType = "belt";
    public static final String headType = "head";
    public static final String bodyType = "body";
    public static final String charmType = "charm";
    public static final String gauntletType = "gauntlet";
    public static final String capeType = "cape";

    private static String[] slots = new String[slotLimit];
    private static ArrayList<String> registeredTypes = new ArrayList<String>();
    static {
        registeredTypes.add(ringType);
        registeredTypes.add(amuletType);
        registeredTypes.add(beltType);
        registeredTypes.add(headType);
        registeredTypes.add(bodyType);
        registeredTypes.add(charmType);
        registeredTypes.add(gauntletType);
        registeredTypes.add(capeType);
    }
    private static int newSlotsRemaining = slotLimit;

    //TODO: Sort slots, locked after post init
    //TODO: Lock removing slots added during pre init
    
    /**
     * Tries to add a new slot type if the loader state is currently
     * in Pre Initialization and the slot type has not already been registered.
     * 
     * @return      If adding a type  was successful
     */
    public static boolean tryRegisterType(String type) {
        if(type != null && type.length() > 0 && Loader.instance().getLoaderState() == LoaderState.PREINITIALIZATION && !registeredTypes.contains(type)) {
            registeredTypes.add(type);
            return true;
        } else {
            return false;
        }
        
    }
    
    public static int indexOfTypeInRegisteredTypes(String type) {
    	return registeredTypes.indexOf(type);
    }
    
    
    /**
     * Returns if the type is registered or not.
     * 
     * @return      If the type is registered or not
     */
    public static boolean isTypeRegistered(String type) {
    	return registeredTypes.contains(type);
    }
    
    /**
     * Returns the currently registered bauble types.
     * If Pre Initialization is done this list is effectively final.
     * 
     * @return      The currently registered bauble types
     */
    public static ArrayList<String> getCurrentlyRegisteredTypes() {
        return registeredTypes;
    }
    
    /**
    * Returns if a new slot was added successfully.
    * Does not add a slot type if the type is unregistered or no slots are free.
    * 
    * @param type  The type of the slot to be added
    * @return      If adding a slot was successful or not
    */
    public static boolean tryAddSlot(String type) {
        if(newSlotsRemaining >= 1 && registeredTypes.contains(type)) {
            slots[slotLimit - newSlotsRemaining] = type;
            newSlotsRemaining--;
            return true;
        } else {
            return false;
        }
    }

    /**
    * Remove a slot of the specified type if one can be found.
    * 
    * @param type  The type of the slot to be removed
    * @return      If removing a slot was successful or not
    */
    public static boolean tryRemoveSlot(String type) {
        if(Loader.instance().getLoaderState() == LoaderState.PREINITIALIZATION && newSlotsRemaining < slotLimit)
        for(int slotToCheck = 0; slotToCheck < slotsCurrentlyUsed(); slotToCheck++) {
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
    * Returns the current number of slots with a matching type.
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
    * Returns the number of bauble slots that are currently used.
    * 
    * @return      The number of bauble slots currently used
    */
    public static int slotsCurrentlyUsed() {
        return slotLimit - newSlotsRemaining;
    }
    
    /**
    * Returns the number of bauble slots that are currently unused.
    * 
    * @return      The number of bauble slots currently unused
    */
    public static int slotsCurrentlyUnused() {
        return newSlotsRemaining;
    }

    /**
    * Returns the type of the specified slot or null if none or out of range.
    * 
    * @param slot  The slot to check
    * @return      The type of the specified slot or "unknown"
    */
    public static String getSlotType(int slot) {
        if (slot >= 0 & slot < slotLimit) {
            return slots[slot];
        } else {
            return unknown;
        }
    }

    /**
    * Returns the type based on the specified BaubleType.
    * 
    * @param type  The BaubleType to get a matching type from
    * @return      The type matching the BaubleType or "unknown"
    */
    public static String getTypeFromBaubleType(BaubleType type) {
        switch(type) {
        case RING:
            return ringType;
        case AMULET:
            return amuletType;
        case BELT:
            return beltType;
        default:
            return unknown;  
        }
    }

}
