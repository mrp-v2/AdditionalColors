package mrp_v2.additionalcolors;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalColors.ID) public class AdditionalColors
{
    public static final String ID = "additional" + "colors";

    public AdditionalColors()
    {
        ObjectHolder.registerListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
