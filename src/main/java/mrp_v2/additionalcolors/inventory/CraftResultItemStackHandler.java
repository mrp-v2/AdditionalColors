package mrp_v2.additionalcolors.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class CraftResultItemStackHandler extends ItemStackHandler implements RecipeHolder
{
    private Recipe<?> recipe;

    public CraftResultItemStackHandler()
    {
        this(1);
    }

    public CraftResultItemStackHandler(int size)
    {
        super(size);
        Preconditions.checkArgument(size == 1, "The size of a crafting result inventory should always be 1!");
    }

    public CraftResultItemStackHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
        Preconditions.checkArgument(stacks.size() == 1, "The size of a crafting result inventory should always be 1!");
    }

    @Override public void setRecipeUsed(@Nullable Recipe<?> recipe)
    {
        this.recipe = recipe;
    }

    @Nullable @Override public Recipe<?> getRecipeUsed()
    {
        return recipe;
    }
}
