// A pipe block which can be used to connect inventories
// used e.g. in hidden corners etc.

package se.docket.propagator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PropagatorPipeBlock extends Block implements ITileEntityProvider {

	public PropagatorPipeBlock(int id) {
		super(
			id,
			Material.iron
		);
		
		this.setStepSound(Block.soundMetalFootstep);
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setHardness(0.5F);
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PropagatorPipeTileEntity();
	}
	
	@Override
	public void registerIcons(IconRegister iconReg) {
		this.blockIcon = iconReg.registerIcon(Propagator.modID + ":" + this.getUnlocalizedName());
	}
}
