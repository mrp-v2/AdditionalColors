package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.item.DyeColor;

public class ColoredSoulSandBlock extends SoulSandBlock implements IColored
{
    private final DyeColor color;

    public ColoredSoulSandBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
