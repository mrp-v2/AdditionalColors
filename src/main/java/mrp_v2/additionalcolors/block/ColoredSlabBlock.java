package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.DyeColor;

public class ColoredSlabBlock extends SlabBlock implements IColored
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
