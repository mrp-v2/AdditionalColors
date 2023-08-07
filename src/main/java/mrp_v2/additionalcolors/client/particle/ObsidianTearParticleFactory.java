package mrp_v2.additionalcolors.client.particle;

import mrp_v2.additionalcolors.particle.ColorParticleData;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT) public abstract class ObsidianTearParticleFactory implements ParticleProvider<ColorParticleData>
{
    private final SpriteSet spriteSet;

    protected ObsidianTearParticleFactory(SpriteSet spriteSet)
    {
        this.spriteSet = spriteSet;
    }

    protected void configureParticle(DripParticle particle, ColorParticleData data)
    {
        particle.isGlowing = true;
        particle.setColor(data.getRedF(), data.getGreenF(), data.getBlueF());
        particle.pickSprite(this.spriteSet);
    }

    @OnlyIn(Dist.CLIENT) public static class ColoredDrippingObsidianTearFactory extends ObsidianTearParticleFactory
    {
        public ColoredDrippingObsidianTearFactory(SpriteSet spriteSet)
        {
            super(spriteSet);
        }

        @Nullable @Override
        public Particle createParticle(ColorParticleData typeIn, ClientLevel worldIn, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed)
        {
            DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(worldIn, x, y, z, Fluids.EMPTY,
                    new ColorParticleData.FallingObsidianTear(typeIn.getColor()));
            particle.gravity *= 0.01F;
            particle.lifetime = 100;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT) public static class ColoredFallingObsidianTearFactory extends ObsidianTearParticleFactory
    {
        public ColoredFallingObsidianTearFactory(SpriteSet spriteSet)
        {
            super(spriteSet);
        }

        @Nullable @Override
        public Particle createParticle(ColorParticleData typeIn, ClientLevel worldIn, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed)
        {
            DripParticle particle = new DripParticle.FallAndLandParticle(worldIn, x, y, z, Fluids.EMPTY,
                    new ColorParticleData.LandingObsidianTear(typeIn.getColor()));
            particle.gravity = 0.01F;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT) public static class ColoredLandingObsidianTearFactory extends ObsidianTearParticleFactory
    {
        public ColoredLandingObsidianTearFactory(SpriteSet spriteSet)
        {
            super(spriteSet);
        }

        @Nullable @Override
        public Particle createParticle(ColorParticleData typeIn, ClientLevel worldIn, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed)
        {
            DripParticle particle = new DripParticle.DripLandParticle(worldIn, x, y, z, Fluids.EMPTY);
            particle.lifetime = (int) (28 / (Math.random() * 0.8D + 0.2D));
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}
