package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.DyeColor;

public class ColoredRotatedPillarBlock extends RotatedPillarBlock implements IColored
{
    private final DyeColor color;

    public ColoredRotatedPillarBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
