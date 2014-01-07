package se.docket.propagator;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class PropagatorBlock extends BlockContainer
{
	private final Random random = new Random();
	
	Icon textureFront;
	Icon textureBlank;

	public PropagatorBlock(int id) {
		super(
			id,
			Material.iron
		);
		
		this.setStepSound(Block.soundMetalFootstep);
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setHardness(1.5F);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if (world.isRemote)
        {
            return true;
        }
        else
        {
        	IInventory iinventory = (PropagatorTileEntity)world.getBlockTileEntity(x, y, z);

            if (iinventory != null)
            {
                player.displayGUIChest(iinventory);
            }

            return true;
        }
	}
	
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
		return side == 3 ? this.textureFront : this.textureBlank;
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister reg)
    {
        this.textureBlank = reg.registerIcon(Propagator.modID + ":" + this.getUnlocalizedName() + "Blank");
        this.textureFront = reg.registerIcon(Propagator.modID + ":" + this.getUnlocalizedName() + "Front");
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PropagatorTileEntity();
	}
	
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        PropagatorTileEntity tileentity = (PropagatorTileEntity)par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentity != null)
        {
            for (int j1 = 0; j1 < tileentity.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentity.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
                    {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}
