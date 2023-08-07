package mrp_v2.additionalcolors.api.block_properties;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class BlockBasedPropertiesProvider implements IBlockPropertiesProvider
{
    private final Block base;

    public BlockBasedPropertiesProvider(Block base)
    {
        this.base = base;
    }

    @Override public BlockBehaviour.Properties getProperties()
    {
        return BlockBehaviour.Properties.copy(base);
    }
}
