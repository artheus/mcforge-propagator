package se.docket.propagator.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import se.docket.propagator.CommonProxy;
import se.docket.propagator.PropagatorPipeBlockRenderer;
import se.docket.propagator.PropagatorPipeTileEntity;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(PropagatorPipeTileEntity.class, new PropagatorPipeBlockRenderer());
	}
}
