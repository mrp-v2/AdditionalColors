package mrp_v2.additionalcolors.item.crafting;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ColoredCraftingRecipe extends SingleItemRecipe
{
    public static final String ID = "crafting_colored";

    public ColoredCraftingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result)
    {
        super(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, ObjectHolder.COLORED_CRAFTING_RECIPE_SERIALIZER.get(), id,
                group, ingredient, result);
    }

    public static IRecipeType<ColoredCraftingRecipe> createRecipeType()
    {
        return new IRecipeType<ColoredCraftingRecipe>()
        {
            @Override public String toString()
            {
                return ID;
            }
        };
    }

    @Override public boolean matches(IInventory inv, World worldIn)
    {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override public ItemStack getToastSymbol()
    {
        return new ItemStack(ObjectHolder.COLORED_CRAFTING_TABLE.get());
    }
}
