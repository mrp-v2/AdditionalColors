package mrp_v2.additionalcolors.item.crafting;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ColoredCraftingRecipe extends SingleItemRecipe
{
    public static final String ID = "crafting_colored";

    public ColoredCraftingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result)
    {
        super(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, ObjectHolder.COLORED_CRAFTING_RECIPE_SERIALIZER.get(), id,
                group, ingredient, result);
    }

    public static RecipeType<ColoredCraftingRecipe> createRecipeType()
    {
        return new RecipeType<ColoredCraftingRecipe>()
        {
            @Override public String toString()
            {
                return ID;
            }
        };
    }

    @Override public boolean matches(Container inv, Level worldIn)
    {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override public ItemStack getToastSymbol()
    {
        return new ItemStack(ObjectHolder.COLORED_CRAFTING_TABLE.get());
    }
}
