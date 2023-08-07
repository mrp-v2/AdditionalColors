package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredStairsBlock extends StairBlock implements IColored
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

    @Override public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
    }
}
