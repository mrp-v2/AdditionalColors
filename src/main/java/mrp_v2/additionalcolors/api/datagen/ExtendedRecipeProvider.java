package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.RecipeProvider;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class ExtendedRecipeProvider extends RecipeProvider
{
    protected ExtendedRecipeProvider(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn, modId);
    }

    public static CriterionTriggerInstance makeHasItemCriterion(TagKey<Item> tag)
    {
        return has(tag);
    }

    public static CriterionTriggerInstance makeHasItemCriterion(ItemLike item)
    {
        return has(item);
    }

    public static SingleItemRecipeBuilder coloredCraftingRecipe(Ingredient ingredientIn, ItemLike resultIn)
    {
        return new SingleItemRecipeBuilder(ObjectHolder.COLORED_CRAFTING_RECIPE_SERIALIZER.get(), ingredientIn,
                resultIn, 1);
    }
}
