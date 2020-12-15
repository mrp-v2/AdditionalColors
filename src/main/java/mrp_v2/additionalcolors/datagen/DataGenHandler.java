package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.datagen.texture.TextureGenerator;
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
            event.getGenerator().addProvider(
                    new TextureGenerator(event.getGenerator(), event.getExistingFileHelper(), AdditionalColors.ID));
            helper.addBlockStateProvider(BlockStateGenerator::new);
            helper.addItemModelProvider(ItemModelGenerator::new);
            helper.addLanguageProvider(EN_USTranslationGenerator::new);
        }
        if (event.includeServer())
        {
            helper.addBlockTagsProvider(BlockTagGenerator::new);
            helper.addItemTagsProvider(ItemTagGenerator::new);
            helper.addRecipeProvider(RecipeGenerator::new);
        }
    }
}
