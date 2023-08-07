package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredBlock extends Block implements IColored
{
    private final DyeColor color;

    public ColoredBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor()
    {
        return this.color;
    }
}
