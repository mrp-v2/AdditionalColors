package mrp_v2.additionalcolors.client;

import mrp_v2.additionalcolors.AdditionalColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(modid = AdditionalColors.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Config
{
    public static final ForgeConfigSpec clientSpec;
    public static final Config CLIENT;
    public static final String TRANSLATION_KEY = AdditionalColors.ID + ".config.gui.";

    static
    {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    private final ForgeConfigSpec.IntValue cryingObsidianParticleChance;
    private int copc;

    public Config(ForgeConfigSpec.Builder builder)
    {
        builder.comment(" Client settings", " Note that these settings only affect blocks added by this mod.")
                .push("client");
        builder.comment(" Crying Obsidian settings").push("crying obsidian");
        String copc = "cryingObsidianParticleChance";
        cryingObsidianParticleChance =
                builder.comment(" Controls how likely it is for a particle to spawn when a animate tick occurs.",
                        " The chance is calculated as follows: chance = 1/value",
                        " Therefore, lower values result in a higher chance, and higher values result in a lower chance.",
                        " A value of 0 disables the particles.").translation(TRANSLATION_KEY + copc)
                        .defineInRange(copc, 5, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.pop();
    }

    @SubscribeEvent public static void onFileChange(final ModConfig.Reloading configEvent)
    {
        LogManager.getLogger().debug(AdditionalColors.DISPLAY_NAME + " config just got changed on the file system!");
        CLIENT.updateValues();
    }

    private void updateValues()
    {
        this.copc = this.cryingObsidianParticleChance.get();
    }

    @SubscribeEvent public static void onLoad(final ModConfig.Loading configEvent)
    {
        LogManager.getLogger().debug("Loaded " + AdditionalColors.DISPLAY_NAME + " config file {}",
                configEvent.getConfig().getFileName());
        CLIENT.updateValues();
    }

    public int getCryingObsidianParticleChance()
    {
        return this.copc;
    }
}
