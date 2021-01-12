package mrp_v2.additionalcolors.api.block_properties;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.DyeColor;

public interface IBlockPropertiesProvider
{
    default AbstractBlock.Properties getProperties(DyeColor color)
    {
        return getProperties();
    }
    AbstractBlock.Properties getProperties();
}
