package mrp_v2.additionalcolors.api.block_properties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class BlockBasedPropertiesProvider implements IBlockPropertiesProvider
{
    private final Block base;

    public BlockBasedPropertiesProvider(Block base)
    {
        this.base = base;
    }

    @Override public AbstractBlock.Properties getProperties()
    {
        return AbstractBlock.Properties.copy(base);
    }
}
