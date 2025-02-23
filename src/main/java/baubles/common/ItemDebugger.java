package baubles.common;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleExpandedSlots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemDebugger extends BaubleItemBase {

    private IIcon[] icons;

	public ItemDebugger() {
		super();
		this.setHasSubtypes(true);
        if (BaublesConfig.hideDebugItem){
		    setCreativeTab(null);
        }
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir) {
		icons = new IIcon[BaubleExpandedSlots.getCurrentlyRegisteredTypes().size()];
		for(int i = 0; i < icons.length; i++) {
		  icons[i] = ir.registerIcon("baubles:empty_bauble_slot_" + BaubleExpandedSlots.getCurrentlyRegisteredTypes().get(i));
		}
	}

    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta >= icons.length ? 0 : meta];
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < icons.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String[] getBaubleTypes(ItemStack itemStack) {
	  String type;
	  int meta = itemStack.getItemDamage();
	  if(meta <= 0 || meta > icons.length) {
		 type = BaubleExpandedSlots.unknownType;
	  } else {
		 type = BaubleExpandedSlots.getCurrentlyRegisteredTypes().get(meta);
	  }
	  return new String[] {type};
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
	  return null;
	}

	public IIcon getBackgroundIconForSlotType(String type) {
		if(type != null && BaubleExpandedSlots.isTypeRegistered(type)) {
			return icons[BaubleExpandedSlots.getIndexOfTypeInRegisteredTypes(type)];
		} else {
			return icons[0];
		}
	}

}
