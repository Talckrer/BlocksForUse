package Core.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.modBFU.BlocksForUse;
import Core.tileEntities.TileEntityWire;

public class BlockWire extends BlockContainer {

	public BlockWire(int id) {
		super(id, Material.iron);
		setUnlocalizedName(BlockInfo.WIRE_BLOCK_UNLOCALIZED_NAME);
		setCreativeTab(BlocksForUse.tabBFUThings);
		setBlockUnbreakable();
		setStepSound(soundMetalFootstep);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityWire(world);
	}
	
	
}
