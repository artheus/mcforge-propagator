package se.docket.propagator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=Propagator.modID, name="ItemsPropagator", version="0.0.1a", dependencies="required-after:factorization")
@NetworkMod(clientSideRequired=true)
public class Propagator {
	
	public final static String modID = "docketpropagator";
	
	// Blocks
	public static Block propagatorBlock;
	public static Block propagatorPipeBlock;
	
	// The instance of your mod that Forge uses.
	@Instance(value = Propagator.modID)
	public static Propagator instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="se.docket.propagator.client.ClientProxy", serverSide="se.docket.propagator.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		propagatorBlock = new PropagatorBlock(600).setUnlocalizedName("propagatorBlock");
		propagatorPipeBlock = new PropagatorPipeBlock(601).setUnlocalizedName("propagatorPipeBlock");
		
		// Blocks
		GameRegistry.registerBlock(propagatorBlock, Propagator.modID + propagatorBlock.getUnlocalizedName());
		GameRegistry.registerBlock(propagatorPipeBlock, Propagator.modID + propagatorPipeBlock.getUnlocalizedName());
		
		// Tile entities
		GameRegistry.registerTileEntity(PropagatorTileEntity.class, "propagator");
		GameRegistry.registerTileEntity(PropagatorPipeTileEntity.class, "propagatorPipe");
		
		// Remove later
		//OreDictionary.registerOre("testDarkIron", Item.ingotIron);
		
		// Stacks for crafting
		// vanilla
		ItemStack eyeOfEnderStack = new ItemStack(Item.eyeOfEnder);
		ItemStack enderPearlStack = new ItemStack(Item.enderPearl);
		ItemStack chestStack = new ItemStack(Block.chest);
		ItemStack redstoneStack = new ItemStack(Item.redstone);
		ItemStack ironBlockStack = new ItemStack(Block.blockIron);
		
		// Shaped crafting recipes
		//    Propagator
		GameRegistry.addRecipe(
			new ItemStack(propagatorBlock),
			"yxy",
			"yzy",
			"yay",
			Character.valueOf('x'), chestStack,
			Character.valueOf('y'), redstoneStack,
			Character.valueOf('z'), eyeOfEnderStack,
			Character.valueOf('a'), ironBlockStack
		);
		//    Propagator pipe
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(propagatorPipeBlock, 4),
				new Object[]{
					" x ",
					"xyx",
					" x ",
					Character.valueOf('x'), "FZ.darkIron",
					Character.valueOf('y'), enderPearlStack
				}
		));
		
		// Language
		LanguageRegistry.addName(propagatorBlock, "Propagator");
		LanguageRegistry.addName(propagatorPipeBlock, "Propagator pipe");
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}