package mrp_v2.additionalcolors.client.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.client.gui.screen.ColoredWorkbenchScreen;
import mrp_v2.additionalcolors.client.particle.ObsidianTearParticleFactory;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleEngine;
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
        ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
        particleManager.register(ObjectHolder.COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredDrippingObsidianTearFactory::new);
        particleManager.register(ObjectHolder.COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredFallingObsidianTearFactory::new);
        particleManager.register(ObjectHolder.COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE.get(),
                ObsidianTearParticleFactory.ColoredLandingObsidianTearFactory::new);
    }

    @SubscribeEvent public static void clientSetup(final FMLClientSetupEvent event)
    {
        MenuScreens.register(ObjectHolder.COLORED_WORKBENCH_CONTAINER_TYPE.get(), ColoredWorkbenchScreen::new);
    }
}
