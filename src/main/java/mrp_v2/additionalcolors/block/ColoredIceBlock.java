package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.IceBlock;
import net.minecraft.item.DyeColor;

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
