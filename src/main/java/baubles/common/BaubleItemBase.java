package baubles.common;

import baubles.api.expanded.BaubleItemHelper;
import baubles.api.expanded.IBaubleExpanded;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;


/**
 * To be used to create standalone (i.e can't be used outside a bauble slot) bauble
 */
public abstract class BaubleItemBase extends Item implements IBaubleExpanded {

    public BaubleItemBase(){
        super();
        setCreativeTab(CreativeTabs.tabTools);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        return BaubleItemHelper.onBaubleRightClick(itemStackIn, worldIn, player);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean debug) {
        BaubleItemHelper.addSlotInformation(tooltip, getBaubleTypes(stack));
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int a) {
        return true;
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase player) {}

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase player) {}
}
