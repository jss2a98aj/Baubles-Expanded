package baubles.api.expanded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

import java.util.List;

public class BaubleItemHelper {

    /**
     * Adds compatible slot information to an item's tooltip, but only when shift is held.
     *
     * @param tooltip The tooltip list.
     * @param types The item's types.
     * @return If shift was held.
     */
    @SideOnly(Side.CLIENT)
    public static boolean addSlotInformation(List tooltip, String[] types) {
        boolean shiftHeld = GuiScreen.isShiftKeyDown();
        if(shiftHeld) {
            tooltip.add(StatCollector.translateToLocal("tooltip.compatibleslots"));
            for(int i = 0; i < types.length; i++) {
                String type = StatCollector.translateToLocal("slot." + types[i]);
                if(i < types.length - 1) type += ",";
                tooltip.add(type);
            }
        } else {
            tooltip.add(StatCollector.translateToLocal("tooltip.shiftprompt"));
        }
        return shiftHeld;
    }

}
