package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.item.DyeColor;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredIceBlock extends IceBlock implements IColored
{
    private final DyeColor color;

    public ColoredIceBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
