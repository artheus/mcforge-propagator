package se.docket.propagator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;

public class PropagatorTileEntity extends TileEntity  implements IInventory
{
	private ItemStack[] propagatorContents = new ItemStack[36];
	
	private List<TileEntity> inventories = new ArrayList<TileEntity>();
	private Map<Integer, ArrayList<Integer>> inventoriesByItemId = new HashMap<Integer,ArrayList<Integer>>();
	private List<Integer> emptyInventories = new ArrayList<Integer>();
	
	private List<TileEntity> pipes = new ArrayList<TileEntity>();
	
	private String field_94045_s;
	private int syncInt = 0;
	
	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return propagatorContents[i];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.propagatorContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.propagatorContents[par1].stackSize <= par2)
            {
                itemstack = this.propagatorContents[par1];
                this.propagatorContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.propagatorContents[par1].splitStack(par2);

                if (this.propagatorContents[par1].stackSize == 0)
                {
                    this.propagatorContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.propagatorContents[par1] != null)
        {
            ItemStack itemstack = this.propagatorContents[par1];
            this.propagatorContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.propagatorContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

	@Override
	public String getInvName() {
		return "Propagator";
	}

	@Override
	public boolean isInvNameLocalized() {
		return this.field_94045_s != null && this.field_94045_s.length() > 0;
	}
	
	public void func_94043_a(String par1Str)
    {
        this.field_94045_s = par1Str;
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	private TileEntity getInventoryAt(int x, int y, int z) {
		TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof PropagatorPipeTileEntity) {
			// Check for inventories around pipes as well
			// but don't return as an inventory.
			if(this.pipes.contains(te) == false) {
				pipes.add(te);
				this.checkForInventoriesAround(x, y, z);
			}
			return null;
		}
		
		if (te instanceof IInventory == false) {
			return null;
		}
		
		return te;
	}
	
	void addInventory(TileEntity inv) {
		if (inv == null) return;
		if (inv instanceof PropagatorTileEntity) return;
		if (this.inventories.contains(inv)) return;
		
		this.inventories.add(inv);
		this.checkForInventoriesAround(inv.xCoord, inv.yCoord, inv.zCoord);
	}
	
	void removeInventory(TileEntity inv) {
		this.inventories.remove(inv);
	}
	
	void removeInventory(int i) {
		this.inventories.remove(i);
	}
	
	void checkForInventoriesAround(int x, int y, int z) {
		this.addInventory(getInventoryAt(x+1, y, z));
		this.addInventory(getInventoryAt(x-1, y, z));
		this.addInventory(getInventoryAt(x, y+1, z));
		this.addInventory(getInventoryAt(x, y-1, z));
		this.addInventory(getInventoryAt(x, y, z+1));
		this.addInventory(getInventoryAt(x, y, z-1));
	}
	
	public void updateEntity()
    {
        super.updateEntity();
        
        if( this.worldObj.isRemote ) return;
        
        int i;
        
        syncInt++;
        
        if	(syncInt > 50) {
        	syncInt = 0;
        	
        	// Clear all if some are not connected anymore, or has changed
        	this.inventories.clear();
        	this.inventoriesByItemId.clear();
        	this.emptyInventories.clear();
        	this.pipes.clear();
        	
        	// Decorate inventory list again
        	checkForInventoriesAround(this.xCoord, this.yCoord, this.zCoord);
        	
        	// Decorate item id > inventory list, and empty inventories list
        	for (i = 0; i < this.inventories.size(); i++) {
        		IInventory inv = (IInventory) this.inventories.get(i);
        		
        		int slotsFilled = 0;
        		
        		for( int slot = 0; slot < inv.getSizeInventory(); slot++ ) {
        			if (inv.getStackInSlot(slot) == null || inv.getStackInSlot(slot).stackSize == 0) continue;
        			
        			slotsFilled++;
        			
        			int itemId = inv.getStackInSlot(slot).itemID;
        			
        			if (this.inventoriesByItemId.get(itemId) == null) {
        				this.inventoriesByItemId.put(itemId, new ArrayList<Integer>());
        			}
        			
        			this.inventoriesByItemId.get(itemId).add(i);
        		}
        		
        		if (slotsFilled == 0) {
        			this.emptyInventories.add(i);
        		}
        	}
        }
        
        // Put stuff from local inventory to connected inventory
        for (i = 0; i < this.getSizeInventory(); i++) {
        	if (this.getStackInSlot(i) == null) continue;
        	
        	ItemStack stack = this.getStackInSlot(i);
        	
        	boolean indexExists;
        	
        	if (this.inventoriesByItemId.get(stack.itemID) == null) {
        		// Take first empty inventory and put the item there
        		if (this.emptyInventories.isEmpty() ) continue;
        		
        		IInventory inv = (IInventory) this.inventories.get(this.emptyInventories.get(0));
        		inv.setInventorySlotContents(0, new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage()));
        		this.decrStackSize(i, stack.stackSize);
        		
        		// Add inventory to sorting list
        		this.inventoriesByItemId.put(stack.itemID, new ArrayList<Integer>());
        		this.inventoriesByItemId.get(stack.itemID).add(this.emptyInventories.get(0));
        		this.emptyInventories.remove(0);
        	} else {
        		// Try to put the item stack in one or multiple of them
        		for(Entry<Integer, ArrayList<Integer>> entry : this.inventoriesByItemId.entrySet()) {
        			ArrayList<Integer> invList = entry.getValue();
        			
        			for (int iref = 0; iref < invList.size(); iref++) {
        				if( this.getStackInSlot(i) == null || this.getStackInSlot(i).stackSize == 0) continue;
        				
        				IInventory inv;
        				
        				inv = (IInventory) this.inventories.get(invList.get(iref));
	        		
        				ItemStack itemstack = this.getStackInSlot(i).copy();
	        			ItemStack itemstack1 = insertStack(inv, this.decrStackSize(i, this.getStackInSlot(i).stackSize));
	
	        			if (itemstack1 == null || itemstack1.stackSize == 0)
	        			{
	        				inv.onInventoryChanged();
	        				break;
	        			}
	
	        			this.setInventorySlotContents(i, itemstack);
        			}
        		}
        	}
        }
    }
	
	public static ItemStack insertStack(IInventory inventory, ItemStack itemStack)
    {
        int k = inventory.getSizeInventory();

        for (int slot = 0; slot < k && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
        	itemStack = putItemInSlot(inventory, itemStack, slot);
        }

        if (itemStack != null && itemStack.stackSize == 0)
        {
        	itemStack = null;
        }

        return itemStack;
    }
	
	private static ItemStack putItemInSlot(IInventory inventory, ItemStack itemStack, int slot)
    {
        ItemStack itemstack1 = inventory.getStackInSlot(slot);

        if (inventory.isStackValidForSlot(slot, itemStack))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
            	inventory.setInventorySlotContents(slot, itemStack);
            	itemStack = null;
                flag = true;
            }
            else if (areItemStacksEqualItem(itemstack1, itemStack))
            {
                int k = itemStack.getMaxStackSize() - itemstack1.stackSize;
                int l = Math.min(itemStack.stackSize, k);
                itemStack.stackSize -= l;
                itemstack1.stackSize += l;
                flag = l > 0;
            }

            if (flag)
            {
                if (inventory instanceof TileEntityHopper)
                {
                    ((TileEntityHopper)inventory).setTransferCooldown(8);
                }

                inventory.onInventoryChanged();
            }
        }

        return itemStack;
    }
	
	private static boolean areItemStacksEqualItem(ItemStack itemStack, ItemStack test)
    {
        return itemStack.itemID != test.itemID ? false :
        	(itemStack.getItemDamage() != test.getItemDamage() ? false :
        		(itemStack.stackSize > itemStack.getMaxStackSize() ? false :
        			ItemStack.areItemStackTagsEqual(itemStack, test)));
    }
	
	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.propagatorContents = new ItemStack[this.getSizeInventory()];

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.field_94045_s = par1NBTTagCompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.propagatorContents.length)
            {
                this.propagatorContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.propagatorContents.length; ++i)
        {
            if (this.propagatorContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.propagatorContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

        if (this.isInvNameLocalized())
        {
            par1NBTTagCompound.setString("CustomName", this.field_94045_s);
        }
    }

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
}
