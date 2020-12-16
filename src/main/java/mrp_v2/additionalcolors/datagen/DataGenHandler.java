package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.DataGeneratorHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = AdditionalColors.ID, bus = Mod.EventBusSubscriber.Bus.MOD) public class DataGenHandler
{
    @SubscribeEvent public static void gatherData(GatherDataEvent event)
    {
        DataGeneratorHelper helper = new DataGeneratorHelper(event, AdditionalColors.ID);
        if (event.includeClient())
        {
            helper.addTextureProvider(TextureGenerator::new);
            helper.addParticleProvider(ParticleGenerator::new);
            helper.addBlockStateProvider(BlockStateGenerator::new);
            helper.addItemModelProvider(ItemModelGenerator::new);
            helper.addLanguageProvider(EN_USTranslationGenerator::new);
        }
        if (event.includeServer())
        {
            helper.addBlockTagsProvider(BlockTagGenerator::new);
            helper.addItemTagsProvider(ItemTagGenerator::new);
            helper.addRecipeProvider(RecipeGenerator::new);
            helper.addLootTables(new LootTableGenerator());
            ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerLootTables(helper);
        }
    }
}
