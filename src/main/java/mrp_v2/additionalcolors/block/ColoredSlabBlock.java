package mrp_v2.additionalcolors.block;

import net.minecraft.block.SlabBlock;
import net.minecraft.item.DyeColor;

public class ColoredSlabBlock extends SlabBlock implements IColoredBlock
{
    private final DyeColor color;

    public ColoredSlabBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
