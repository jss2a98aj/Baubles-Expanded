package baubles.common.container;

import baubles.api.IBauble;
import baubles.api.expanded.BaubleExpandedSlots;
import baubles.api.expanded.IBaubleExpanded;
import baubles.common.BaublesConfig;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerPlayerExpanded extends Container {
    public InventoryBaubles baubles;

    private final EntityPlayer thePlayer;

    public ContainerPlayerExpanded(InventoryPlayer playerInv, boolean par2, EntityPlayer player) {
        this.thePlayer = player;
        baubles = new InventoryBaubles(player);
        baubles.setEventHandler(this);
        if (!player.worldObj.isRemote) {
            baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }

        int i;
        int j;

        //armor
        for (i = 0; i < 4; ++i) {
            final int k = i;
            this.addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 1 - i, 8, 8 + i * 18) {
                @Override
                public int getSlotStackLimit() { return 1; }

                @Override
                public boolean isItemValid(ItemStack itemStack) {
                    if (itemStack == null) return false;
                    return itemStack.getItem().isValidArmor(itemStack, k, thePlayer);
                }

                @Override
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex() {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

        final int slotOffset = 18;
        final int slotStartX = 80;
        final int slotStartY = 8;

        //bauble slots
        for(i = 0; i < BaubleExpandedSlots.slotLimit; i++) {
        	String slotType = BaubleExpandedSlots.getSlotType(i);
        	if(BaublesConfig.showUnusedSlots || !slotType.equals(BaubleExpandedSlots.unknownType)) {
                addSlotToContainer(new SlotBauble(baubles, slotType, i, slotStartX + (slotOffset * (i / 4)), slotStartY + (slotOffset * (i % 4))));
        	}
        }

        //inventory slots
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9, slotStartY + j * slotOffset, 84 + i * 18));
            }
        }

        //hotbar slots
        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInv, i, slotStartY + i * slotOffset, 142));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack returnStack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);
        final int visibleBaubleSlots = BaublesConfig.showUnusedSlots ? BaubleExpandedSlots.slotLimit : BaubleExpandedSlots.slotsCurrentlyUsed();

        if(slot != null && slot.getHasStack()) {
            ItemStack originalStack = slot.getStack();
            returnStack = originalStack.copy();
            Item item = returnStack.getItem();

            if(item instanceof ItemArmor && !((Slot)inventorySlots.get(((ItemArmor)item).armorType)).getHasStack()) {
                int armorSlot = ((ItemArmor)item).armorType;
                if(!mergeItemStack(originalStack, armorSlot, armorSlot + 1, false)) {
                    returnStack = null;
                }
            } else if(returnStack != null && slotIndex >= 4 + visibleBaubleSlots && item instanceof IBauble && ((IBauble)item).canEquip(returnStack, thePlayer)) {
                for(int baubleSlot = 4; baubleSlot < 4 + visibleBaubleSlots; baubleSlot++) {
                    if(returnStack == null) {
                        break;
                    }
                	if(!((Slot)inventorySlots.get(baubleSlot)).getHasStack()) {
                		String[] types;
                		if(item instanceof IBaubleExpanded) {
                			types = ((IBaubleExpanded)item).getBaubleTypes(returnStack);
                		} else {
                			types = new String[] {BaubleExpandedSlots.getTypeFromBaubleType(((IBauble)item).getBaubleType(returnStack))};
                		}
                		for(String type : types) {
                			if(type.equals(BaubleExpandedSlots.getSlotType(baubleSlot - 4)) && !mergeItemStack(originalStack, baubleSlot, baubleSlot + 1, false)) {
                                returnStack = null;
                			}
                		}
                	}
                }
            } else if(returnStack != null && slotIndex >= 4 + visibleBaubleSlots && slotIndex < 31 + visibleBaubleSlots) {
                if(!mergeItemStack(originalStack, 31 + visibleBaubleSlots, 40 + visibleBaubleSlots, false)) {
                    returnStack = null;
                }
            } else if(returnStack != null && slotIndex >= 31 + visibleBaubleSlots && slotIndex < 40 + visibleBaubleSlots) {
                if(!mergeItemStack(originalStack, 4 + visibleBaubleSlots, 31 + visibleBaubleSlots, false)) {
                    returnStack = null;
                }
            } else if(returnStack != null && !mergeItemStack(originalStack, 4 + visibleBaubleSlots, 40 + visibleBaubleSlots, false, slot)) {
                returnStack = null;
            }

            if(originalStack.stackSize <= 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if(returnStack != null && originalStack.stackSize == returnStack.stackSize) {
                returnStack = null;
            }

            slot.onPickupFromSlot(player, originalStack);
        }

        return returnStack;
    }

    private void unequipBauble(ItemStack stack) {
//        if (stack.getItem() instanceof IBauble) {
//            ((IBauble)stack.getItem()).onUnequipped(stack, thePlayer);
//        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] p_75131_1_) {
        baubles.blockEvents=true;
        super.putStacksInSlots(p_75131_1_);
    }

    protected boolean mergeItemStack(ItemStack itemStack, int par2, int par3, boolean par4, Slot ss) {
        boolean flag1 = false;
        int k = par2;

        if (par4) {
            k = par3 - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (itemStack.isStackable()) {
            while (itemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
                slot = (Slot)inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == itemStack.getItem() && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, itemstack1)) {
                    int l = itemstack1.stackSize + itemStack.stackSize;
                    if (l <= itemStack.getMaxStackSize()) {
                        if (ss instanceof SlotBauble) unequipBauble(itemStack);
                        itemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < itemStack.getMaxStackSize()) {
                        if (ss instanceof SlotBauble) unequipBauble(itemStack);
                        itemStack.stackSize -= itemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = itemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (itemStack.stackSize > 0) {
            if (par4) {
                k = par3 - 1;
            } else {
                k = par2;
            }

            while (!par4 && k < par3 || par4 && k >= par2) {
                slot = (Slot)inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null) {
                    if (ss instanceof SlotBauble) unequipBauble(itemStack);
                    slot.putStack(itemStack.copy());
                    slot.onSlotChanged();
                    itemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return flag1;
    }

}
