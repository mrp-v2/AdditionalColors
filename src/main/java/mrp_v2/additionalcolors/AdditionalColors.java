package mrp_v2.additionalcolors;

import mrp_v2.additionalcolors.client.Config;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalColors.ID) public class AdditionalColors
{
    public static final String ID = "additional" + "colors";
    public static final String DISPLAY_NAME = "Additional Colors";
    public static final String OBSIDIAN_EXPANSION_ID = "obsidian" + "expansion";
    private static boolean isObsidianExpansionPresent;

    public AdditionalColors()
    {
        isObsidianExpansionPresent = ModList.get().isLoaded(OBSIDIAN_EXPANSION_ID);
        ObjectHolder.registerListeners(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    }

    public static boolean isObsidianExpansionPresent()
    {
        return isObsidianExpansionPresent;
    }
}
