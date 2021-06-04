package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.particle.util.Color3B;
import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ColoredCryingObsidianBlock extends CryingObsidianBlock implements IColored
{
    private final DyeColor color;
    private final Color3B particleColor;

    public ColoredCryingObsidianBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
        this.particleColor = Color3B.fromInt(color.getColorValue());
    }

    @Override @OnlyIn(Dist.CLIENT) public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(5) == 0)
        {
            Direction direction = Direction.getRandom(rand);
            if (direction != Direction.UP)
            {
                BlockPos blockpos = pos.relative(direction);
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!stateIn.canOcclude() || !blockstate.isFaceSturdy(worldIn, blockpos, direction.getOpposite()))
                {
                    double d0 = direction.getStepX() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getStepX() * 0.6D;
                    double d1 = direction.getStepY() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getStepY() * 0.6D;
                    double d2 = direction.getStepZ() == 0 ? rand.nextDouble() :
                            0.5D + (double) direction.getStepZ() * 0.6D;
                    worldIn.addParticle(new ColorParticleData.DrippingObsidianTear(this.particleColor),
                            (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, 0.0D, 0.0D,
                            0.0D);
                }
            }
        }
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
