package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.RecipeProvider;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

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
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.registerRecipes(consumer));
    }
}
