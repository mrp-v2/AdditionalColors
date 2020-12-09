package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.RecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider
{
    protected RecipeGenerator(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn, modId);
    }

    @Override protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for (RegistryObject<ColoredBlockItem> itemObj : ObjectHolder.COLORED_CRYING_OBSIDIAN_ITEMS.values())
        {
            ColoredBlockItem item = itemObj.get();
            ShapelessRecipeBuilder.shapelessRecipe(item).addIngredient(ObjectHolder.CRYING_OBSIDIAN_TAG)
                    .addIngredient(item.getColor().getTag())
                    .addCriterion("has_block", hasItem(ObjectHolder.CRYING_OBSIDIAN_TAG)).build(consumer);
        }
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            for (RegistryObject<ColoredBlock> blockObj : ObjectHolder.COLORIZED_BLOCK_MAP.get(entry.getBlock()))
            {
                ColoredBlock block = blockObj.get();
                ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(entry.getTag())
                        .addIngredient(block.getColor().getTag()).addCriterion("has_block", hasItem(entry.getTag()))
                        .build(consumer);
            }
        }
    }
}
