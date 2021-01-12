package mrp_v2.additionalcolors.client.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.client.gui.screen.ColoredWorkbenchScreen;
import mrp_v2.additionalcolors.client.particle.ObsidianTearParticleFactory;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdditionalColors.ID)
public class EventHandler
{
    @SubscribeEvent public static void registerParticles(final ParticleFactoryRegisterEvent event)
    {
        ParticleManager particleManager = Minecraft.getInstance().particles;
        particleManager.registerFactory(ObjectHolder.COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredDrippingObsidianTearFactory::new);
        particleManager.registerFactory(ObjectHolder.COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredFallingObsidianTearFactory::new);
        particleManager.registerFactory(ObjectHolder.COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredLandingObsidianTearFactory::new);
    }

    @SubscribeEvent public static void clientSetup(final FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(ObjectHolder.COLORED_WORKBENCH_CONTAINER_TYPE.get(), ColoredWorkbenchScreen::new);
    }
}
