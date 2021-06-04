package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.RecipeProvider;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

public abstract class ExtendedRecipeProvider extends RecipeProvider
{
    protected ExtendedRecipeProvider(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn, modId);
    }

    public static ICriterionInstance makeHasItemCriterion(ITag<Item> tag)
    {
        return has(tag);
    }

    public static ICriterionInstance makeHasItemCriterion(IItemProvider item)
    {
        return has(item);
    }

    public static SingleItemRecipeBuilder coloredCraftingRecipe(Ingredient ingredientIn, IItemProvider resultIn)
    {
        return new SingleItemRecipeBuilder(ObjectHolder.COLORED_CRAFTING_RECIPE_SERIALIZER.get(), ingredientIn,
                resultIn, 1);
    }
}
