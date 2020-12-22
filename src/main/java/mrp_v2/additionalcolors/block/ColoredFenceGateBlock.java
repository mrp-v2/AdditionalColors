package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.item.DyeColor;

public class ColoredFenceGateBlock extends FenceGateBlock implements IColored
{
    private final DyeColor color;

    public ColoredFenceGateBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
