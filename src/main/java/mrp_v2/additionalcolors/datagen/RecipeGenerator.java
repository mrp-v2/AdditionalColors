package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.RecipeProvider;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider
{
    protected RecipeGenerator(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn, modId);
    }

    public static ICriterionInstance makeHasItemCriterion(ITag<Item> tag)
    {
        return hasItem(tag);
    }

    @Override protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerRecipes(consumer);
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> objPair : ObjectHolder.COLORIZED_BLOCK_MAP
                    .get(entry.getBlock()))
            {
                ColoredBlock block = objPair.getLeft().get();
                ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(entry.getTag())
                        .addIngredient(block.getColor().getTag()).addCriterion("has_block", hasItem(entry.getTag()))
                        .build(consumer);
            }
        }
    }
}
