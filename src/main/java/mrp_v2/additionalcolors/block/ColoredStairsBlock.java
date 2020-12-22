package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class ColoredStairsBlock extends StairsBlock implements IColored
{
    private final DyeColor color;

    public ColoredStairsBlock(Supplier<BlockState> state, Properties properties, DyeColor color)
    {
        super(state, properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }

    @Override public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    }
}
