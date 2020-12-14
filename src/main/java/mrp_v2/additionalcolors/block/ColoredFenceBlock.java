package mrp_v2.additionalcolors.block;

import net.minecraft.block.FenceBlock;
import net.minecraft.item.DyeColor;

public class ColoredFenceBlock extends FenceBlock implements IColoredBlock
{
    private final DyeColor color;

    public ColoredFenceBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
