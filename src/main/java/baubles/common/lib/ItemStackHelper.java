package baubles.common.lib;

import baubles.common.BaublesConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class ItemStackHelper {

    public static boolean isSoulBound(ItemStack itemStack) {
        if(BaublesConfig.soulBoundEnchantments.length >= 0) {
            NBTTagList enchantments = itemStack.getEnchantmentTagList();
            if(enchantments != null) {
                for(int i = 0; i < enchantments.tagCount(); i++) {
                    int itemEnchantID = enchantments.getCompoundTagAt(i).getInteger("id");
                    for(int soulBoundID : BaublesConfig.soulBoundEnchantments) {
                        if(itemEnchantID == soulBoundID) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
