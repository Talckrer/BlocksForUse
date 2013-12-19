package Core.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import Core.ModBFU.BlocksForUse;

public class BlockOreCalindorium extends Block {

    public BlockOreCalindorium(int id) {
        super(id, Material.iron);
        setCreativeTab(BlocksForUse.tabBFUThings);
        setBlockUnbreakable();
        setStepSound(Block.soundStoneFootstep);
        setUnlocalizedName(BlockInfo.CALINDORIUM_BLOCK_UNLOCALIZED_NAME);
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3){
        return this.blockID;
    }

}

