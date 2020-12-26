package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.client.Config;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.particle.util.Color3B;
import mrp_v2.additionalcolors.util.IColored;
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

public class ColoredCryingObsidianBlock extends CryingObsidianBlock implements IColored
{
    private final DyeColor color;
    private final Color3B particleColor;

    public ColoredCryingObsidianBlock(DyeColor color, AbstractBlock.Properties properties)
    {
        super(properties);
        this.color = color;
        this.particleColor = Color3B.fromInt(color.getColorValue());
    }

    @Override @OnlyIn(Dist.CLIENT) public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (Config.CLIENT.getCryingObsidianParticleChance() == 0)
        {
            return;
        }
        if (rand.nextInt(Config.CLIENT.getCryingObsidianParticleChance()) == 0)
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
