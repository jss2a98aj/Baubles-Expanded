package baubles.api.expanded;

import java.util.ArrayList;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BaubleSlotTypeBackgroundIconManager {
    
    @SideOnly(Side.CLIENT)
    private static class TypeIIconPair {
        public IIcon icon;
        public String type;
        
        public TypeIIconPair(IIcon icon, String type){
            this.icon = icon;
            this.type = type;
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static ArrayList<TypeIIconPair> typeIIconPairList = new ArrayList<TypeIIconPair>();
    
    @SideOnly(Side.CLIENT)
    private static IIcon unknownIcon;
    
    @SideOnly(Side.CLIENT)
    public static IIcon getBackgroundIconForSlotType(String type) {
    	if(type != null) {
    		for(TypeIIconPair pair : typeIIconPairList) {
                if(type.equals(pair.type)) {
                    return pair.icon;
                }
            }
    	}
        return unknownIcon;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean doesBackgroundIconForSlotTypeExist(String type) {
    	if(type != null) {
	        for(TypeIIconPair pair : typeIIconPairList) {
	            if(type.equals(pair.type)) {
	                return true;
	            }
	        }
    	}
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static boolean tryAddBackgroundIconForSlotType(IIcon icon, String type) {
        if(icon != null && type != null && !doesBackgroundIconForSlotTypeExist(type)) {
            typeIIconPairList.add(new TypeIIconPair(icon, type));
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void setBakgroundIconForUnknownSlotType(IIcon icon) {
        if(icon != null) {
            unknownIcon = icon;
        }
    }

}
