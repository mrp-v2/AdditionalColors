package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.mrplibrary.datagen.ParticleProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class ParticleGenerator extends ParticleProvider
{
    protected ParticleGenerator(DataGenerator generator, String modId)
    {
        super(generator, modId);
    }

    @Override protected void registerParticles(Consumer<ParticleBuilder> consumer)
    {
        consumer.accept(makeBuilder(new ResourceLocation(AdditionalColors.ID, "colored_dripping_obsidian_tear"))
                .addTexture(new ResourceLocation("drip_hang")));
        consumer.accept(makeBuilder(new ResourceLocation(AdditionalColors.ID, "colored_falling_obsidian_tear"))
                .addTexture(new ResourceLocation("drip_fall")));
        consumer.accept(makeBuilder(new ResourceLocation(AdditionalColors.ID, "colored_landing_obsidian_tear"))
                .addTexture(new ResourceLocation("drip_land")));
    }
}
