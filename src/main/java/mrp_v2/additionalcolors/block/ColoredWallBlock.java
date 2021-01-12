package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.WallBlock;
import net.minecraft.item.DyeColor;

public class ColoredWallBlock extends WallBlock implements IColored
{
    private final DyeColor color;

    public ColoredWallBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
