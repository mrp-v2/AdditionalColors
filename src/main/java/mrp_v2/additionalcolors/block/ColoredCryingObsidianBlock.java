package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.particle.ColorParticleData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ColoredCryingObsidianBlock extends CryingObsidianBlock
{
    public final DyeColor color;

    public ColoredCryingObsidianBlock(AbstractBlock.Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override @OnlyIn(Dist.CLIENT) public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(5) == 0)
        {
            Direction direction = Direction.getRandomDirection(rand);
            if (direction != Direction.UP)
            {
                BlockPos blockpos = pos.offset(direction);
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!stateIn.isSolid() || !blockstate.isSolidSide(worldIn, blockpos, direction.getOpposite()))
                {
                    double d0 = direction.getXOffset() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getXOffset() * 0.6D;
                    double d1 = direction.getYOffset() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getYOffset() * 0.6D;
                    double d2 = direction.getZOffset() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getZOffset() * 0.6D;
                    worldIn.addParticle(new ColorParticleData.DrippingObsidianTear(this.color.getColorValue()),
                            (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, 0.0D, 0.0D,
                            0.0D);
                }
            }
        }
    }
}
