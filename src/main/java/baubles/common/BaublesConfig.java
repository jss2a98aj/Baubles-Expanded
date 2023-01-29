package baubles.common;

import java.util.ArrayList;

import baubles.api.expanded.BaubleExpandedSlots;
import net.minecraftforge.common.config.Configuration;

public class BaublesConfig {

	public static boolean hideDebugItem = true;

    public static int[] soulBoundEnchantments = new int[] {};
    public static boolean showUnusedSlots = false;
    public static boolean manualSlotSelection = false;

    public static String[] overrideSlotTypes = new String[] {
        BaubleExpandedSlots.amuletType,
        BaubleExpandedSlots.ringType,
        BaubleExpandedSlots.ringType,
        BaubleExpandedSlots.beltType
    };

    static final String categoryDebug = "debug";
    static final String categoryGeneral = "general";
    static final String categoryMenu = "menu";
    static final String categoryOverride = "override";

    public static void loadConfig(Configuration config) {

        ArrayList<String> currentlyRegisteredTypes = BaubleExpandedSlots.getCurrentlyRegisteredTypes();
        String[] currentSlotAssignments = BaubleExpandedSlots.getCurrentSlotAssignments();

        //categoryDebug
        hideDebugItem = config.getBoolean("hideDebugItem", categoryDebug, hideDebugItem, "Hides the Bauble debug item from the creative menu.\n");

        //categoryGeneral
        soulBoundEnchantments = config.get(categoryGeneral, "soulBoundEnchantments", soulBoundEnchantments,
            "IDs of enchantments that should be treated as soul bound when on items in a bauble slot."
        ).getIntList();

        //categoryMenu
        showUnusedSlots = config.getBoolean("showUnusedSlots", categoryMenu, showUnusedSlots, "Display unused Bauble slots.\n");
        manualSlotSelection = config.getBoolean("manualSlotSelection", categoryMenu, manualSlotSelection,
            "Manually override slot assignments.\n!Bauble slot types must be configured manually with this option enabled!\n"
        );

        //categoryOverride
        config.getStringList("defualtSlotTypes", categoryOverride, new String[] {},
            "Baubles and its addons assigned the folowing types to the bauble slots.\n!This config option automatically changes to reflect what Baubles and its addons assigned each time the game is launched!"
        );
        config.getCategory(categoryOverride).get("defualtSlotTypes").set(currentSlotAssignments);

        overrideSlotTypes = config.getStringList("slotTypeOverrides", categoryOverride, overrideSlotTypes,
            "Slot assignments to use if manualSlotSelection is enabled.\nAny assignents after the first " +
            BaubleExpandedSlots.slotLimit + " will be ignored.\n!Adding, moving, or removing slots of the "
            + BaubleExpandedSlots.amuletType + ", " + BaubleExpandedSlots.ringType + ", or " + BaubleExpandedSlots.beltType +
            " types will reduce compatibility with mods made for original Baubles versions!\n",
            currentlyRegisteredTypes.toArray(new String[currentlyRegisteredTypes.size()])
        );

        if(manualSlotSelection) {
            BaubleExpandedSlots.overrideSlots(overrideSlotTypes);
        }

        if(config.hasChanged()) {
            config.save();
        }
    }

}
