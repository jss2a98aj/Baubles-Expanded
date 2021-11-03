package baubles.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.expanded.BaubleExpandedSlots;
import baubles.common.lib.PlayerHandler;

public class ContainerPlayerExpanded extends Container {
    public IInventory craftResult = new InventoryCraftResult();
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
            });
        }

        final int slotOffset = 18;
        final int slotStartX = 80;
        final int slotStartY = 8;
        final int baubleSlots = BaubleExpandedSlots.getTotalSlots();
        
        //bauble slots
        for(i = 0; i < baubleSlots; i++) {
        	addSlotToContainer(new SlotBauble(baubles, BaubleExpandedSlots.getTypeInSlot(i), i, slotStartX + (slotOffset * (i / 4)), slotStartY + (slotOffset * (i % 4))));
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
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);
        final int baubleSlots = BaubleExpandedSlots.getTotalSlots();

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            Item item = itemstack.getItem();
            
            if (slotIndex >= 5 && slotIndex < 4) {
                if (!this.mergeItemStack(itemstack1, 4 + baubleSlots, 40 + baubleSlots, false)) {
                    return null;
                }
            } else if (item instanceof ItemArmor && !((Slot)inventorySlots.get(((ItemArmor)item).armorType)).getHasStack()) {
                int j = ((ItemArmor)item).armorType;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            //TODO: rewrite bauble shift clicking
            } else if (item instanceof IBauble && 
            		((IBauble)item).getBaubleType(itemstack)==BaubleType.AMULET &&
    				((IBauble)item).canEquip(itemstack, thePlayer) &&
            		!((Slot)this.inventorySlots.get(4)).getHasStack())
            {
                int j = 4;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotIndex>11 && item instanceof IBauble && 
            		((IBauble)item).getBaubleType(itemstack)==BaubleType.RING &&
    				((IBauble)item).canEquip(itemstack, thePlayer) &&
            		!((Slot)this.inventorySlots.get(5)).getHasStack()) {
                int j = 5;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotIndex>11 && item instanceof IBauble && 
            		((IBauble)item).getBaubleType(itemstack)==BaubleType.RING &&
    				((IBauble)item).canEquip(itemstack, thePlayer) &&
            		!((Slot)this.inventorySlots.get(6)).getHasStack()) {
                int j = 6;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (item instanceof IBauble && 
            		((IBauble)item).getBaubleType(itemstack)==BaubleType.BELT &&
    				((IBauble)item).canEquip(itemstack, thePlayer) &&
            		!((Slot)this.inventorySlots.get(7)).getHasStack()) {
                int j = 7;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotIndex >= 4 + baubleSlots && slotIndex < 31 + baubleSlots) {
                if (!this.mergeItemStack(itemstack1, 31 + baubleSlots, 40 + baubleSlots, false)) {
                    return null;
                }
            } else if (slotIndex >= 31 + baubleSlots && slotIndex < 40 + baubleSlots) {
                if (!this.mergeItemStack(itemstack1, 4 + baubleSlots, 31 + baubleSlots, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 4 + baubleSlots, 40 + baubleSlots, false, slot)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
    
    private void unequipBauble(ItemStack stack) {
//    	if (stack.getItem() instanceof IBauble) {
//    		((IBauble)stack.getItem()).onUnequipped(stack, thePlayer);
//    	}
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
                slot = (Slot)this.inventorySlots.get(k);
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
                slot = (Slot)this.inventorySlots.get(k);
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

    @Override
    public boolean func_94530_a(ItemStack itemStack, Slot slot) {
        return slot.inventory != this.craftResult && super.func_94530_a(itemStack, slot);
    }

}
