package mrp_v2.additionalcolors.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class CraftResultItemStackHandler extends ItemStackHandler implements IRecipeHolder
{
    private IRecipe<?> recipe;

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

    @Override public void setRecipeUsed(@Nullable IRecipe<?> recipe)
    {
        this.recipe = recipe;
    }

    @Nullable @Override public IRecipe<?> getRecipeUsed()
    {
        return recipe;
    }
}
