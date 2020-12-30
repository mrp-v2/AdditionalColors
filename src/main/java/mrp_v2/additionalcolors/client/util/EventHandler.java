package mrp_v2.additionalcolors.client.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdditionalColors.ID)
public class EventHandler
{
    @SubscribeEvent public static void clientSetup(FMLClientSetupEvent event)
    {
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.clientSetup(event));
    }
}
