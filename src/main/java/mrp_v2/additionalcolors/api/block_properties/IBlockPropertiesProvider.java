package mrp_v2.additionalcolors.api.block_properties;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;

public interface IBlockPropertiesProvider
{
    default BlockBehaviour.Properties getProperties(DyeColor color)
    {
        return getProperties();
    }
    BlockBehaviour.Properties getProperties();
}
