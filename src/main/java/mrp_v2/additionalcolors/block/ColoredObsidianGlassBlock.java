package mrp_v2.additionalcolors.block;

import com.allkillernofiller.obsidianexpansion.block.ObsidianGlass;
import net.minecraft.item.DyeColor;

public class ColoredObsidianGlassBlock extends ObsidianGlass implements IColoredBlock
{
    private final DyeColor color;

    public ColoredObsidianGlassBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
