package mrp_v2.additionalcolors.inventory.container;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.inventory.CraftResultItemStackHandler;
import mrp_v2.additionalcolors.item.crafting.ColoredCraftingRecipe;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ColoredWorkbenchContainer extends AbstractContainerMenu
{
    public static final TranslatableComponent NAME =
            new TranslatableComponent("container." + AdditionalColors.ID + "." + ColoredCraftingRecipe.ID);
    private final DataSlot selectedRecipe = DataSlot.standalone();
    private final ContainerLevelAccess worldPosCallable;
    private final Level world;
    private final CraftResultItemStackHandler outputInventory = new CraftResultItemStackHandler();
    private final Slot inputSlot;
    private final Slot outputSlot;
    private List<ColoredCraftingRecipe> recipes = new ArrayList<>();
    private ItemStack inputItemStack = ItemStack.EMPTY;
    private Runnable inventoryUpdateListener = () ->
    {
    };
    public final ItemStackHandler inputInventory = new ItemStackHandler()
    {
        @Override protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            onCraftMatrixChanged(this);
            inventoryUpdateListener.run();
        }
    };

    public static MenuType<ColoredWorkbenchContainer> createContainerType()
    {
        return new MenuType<>(ColoredWorkbenchContainer::new);
    }

    public ColoredWorkbenchContainer(int windowIdIn, Inventory playerInventoryIn)
    {
        this(windowIdIn, playerInventoryIn, ContainerLevelAccess.NULL);
    }

    public ColoredWorkbenchContainer(int windowIdIn, Inventory playerInventoryIn,
            final ContainerLevelAccess worldPosCallableIn)
    {
        super(ObjectHolder.COLORED_WORKBENCH_CONTAINER_TYPE.get(), windowIdIn);
        worldPosCallable = worldPosCallableIn;
        world = playerInventoryIn.player.level;
        inputSlot = addSlot(new SlotItemHandler(inputInventory, 0, 20, 33));
        outputSlot = addSlot(new SlotItemHandler(outputInventory, 0, 143, 33)
        {
            @Override public boolean mayPlace(@Nonnull ItemStack stack)
            {
                return false;
            }

            @Override public void onTake(Player thePlayer, ItemStack stack)
            {
                stack.onCraftedBy(thePlayer.level, thePlayer, stack.getCount());
                outputInventory.awardUsedRecipes(thePlayer);
                ItemStack itemStack = inputSlot.remove(1);
                if (!itemStack.isEmpty())
                {
                    updateRecipeResultSlot();
                }
                super.onTake(thePlayer, stack);
            }
        });
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }
        addDataSlot(selectedRecipe);
    }

    private void updateRecipeResultSlot()
    {
        if (!this.recipes.isEmpty() && isRecipeIndexValid(selectedRecipe.get()))
        {
            ColoredCraftingRecipe recipe = recipes.get(selectedRecipe.get());
            outputInventory.setRecipeUsed(recipe);
            //noinspection ConstantConditions - null is ok in this case
            outputSlot.set(recipe.assemble(null));
        } else
        {
            outputSlot.set(ItemStack.EMPTY);
        }
        broadcastChanges();
    }

    private boolean isRecipeIndexValid(int selectedRecipe)
    {
        return selectedRecipe >= 0 && selectedRecipe < recipes.size();
    }

    public List<ColoredCraftingRecipe> getRecipeList()
    {
        return recipes;
    }

    public void setInventoryUpdateListener(Runnable listenerIn)
    {
        this.inventoryUpdateListener = listenerIn;
    }

    public int getRecipeListSize()
    {
        return recipes.size();
    }

    public int getSelectedRecipe()
    {
        return selectedRecipe.get();
    }

    public boolean hasItemsInInputSlot()
    {
        return inputSlot.hasItem() && !recipes.isEmpty();
    }

    @Override public boolean clickMenuButton(Player playerIn, int id)
    {
        if (isRecipeIndexValid(id))
        {
            selectedRecipe.set(id);
            updateRecipeResultSlot();
        }
        return true;
    }

    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 1)
            {
                item.onCraftedBy(itemstack1, playerIn.level, playerIn);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 0)
            {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager()
                    .getRecipeFor(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, new SimpleContainer(itemstack1), this.world)
                    .isPresent())
            {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29)
            {
                if (!this.moveItemStackTo(itemstack1, 29, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false))
            {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            this.broadcastChanges();
        }
        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn)
    {
        if (slotIn instanceof SlotItemHandler)
        {
            if (((SlotItemHandler) slotIn).getItemHandler() == this.outputInventory)
            {
                return false;
            }
        }
        return super.canTakeItemForPickAll(stack, slotIn);
    }

    @Override public void removed(Player playerIn)
    {
        super.removed(playerIn);
        outputSlot.set(ItemStack.EMPTY);
        worldPosCallable.execute((a, b) ->
        {
            clearContainer(playerIn, playerIn.level, inputInventory);
        });
    }

    protected void clearContainer(Player playerIn, Level worldIn, ItemStackHandler inventoryIn)
    {
        ItemStack tempStack;
        if (!playerIn.isAlive() ||
                playerIn instanceof ServerPlayer && ((ServerPlayer) playerIn).hasDisconnected())
        {
            for (int j = 0; j < inventoryIn.getSlots(); ++j)
            {
                tempStack = inventoryIn.getStackInSlot(j);
                inventoryIn.setStackInSlot(j, ItemStack.EMPTY);
                playerIn.drop(tempStack, false);
            }
        } else
        {
            for (int i = 0; i < inventoryIn.getSlots(); ++i)
            {
                tempStack = inventoryIn.getStackInSlot(i);
                inventoryIn.setStackInSlot(i, ItemStack.EMPTY);
                playerIn.getInventory().placeItemBackInInventory(tempStack);
            }
        }
    }

    @Override public boolean stillValid(Player playerIn)
    {
        return stillValid(worldPosCallable, playerIn, ObjectHolder.COLORED_CRAFTING_TABLE.get());
    }

    public void onCraftMatrixChanged(ItemStackHandler inventoryIn)
    {
        ItemStack itemStack = inputSlot.getItem();
        if (itemStack.getItem() != inputItemStack.getItem())
        {
            inputItemStack = itemStack.copy();
            updateAvailableRecipes(new SimpleContainer(inventoryIn.getStackInSlot(0)), itemStack);
        }
    }

    private void updateAvailableRecipes(Container inventoryIn, ItemStack stack)
    {
        recipes.clear();
        selectedRecipe.set(-1);
        outputSlot.set(ItemStack.EMPTY);
        if (!stack.isEmpty())
        {
            recipes =
                    world.getRecipeManager().getRecipesFor(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, inventoryIn, world);
        }
    }
}
