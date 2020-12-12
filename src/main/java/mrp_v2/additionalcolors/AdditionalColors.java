package mrp_v2.additionalcolors;

import mrp_v2.additionalcolors.client.Config;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalColors.ID) public class AdditionalColors
{
    public static final String ID = "additional" + "colors";
    public static final String DISPLAY_NAME = "Additional Colors";

    public AdditionalColors()
    {
        ObjectHolder.registerListeners(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    }
}
