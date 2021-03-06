package mrp_v2.additionalcolors.inventory.container;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.inventory.CraftResultItemStackHandler;
import mrp_v2.additionalcolors.item.crafting.ColoredCraftingRecipe;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.LanguageProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ColoredWorkbenchContainer extends Container
{
    public static final ITextComponent NAME = LanguageProvider
            .makeTextTranslation("container.", AdditionalColors.ID, "." + ColoredCraftingRecipe.ID, "en_us",
                    "Colored Crafting");
    private final IntReferenceHolder selectedRecipe = IntReferenceHolder.single();
    private final IWorldPosCallable worldPosCallable;
    private final World world;
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

    /**
     * For static initialization
     */
    public static void init()
    {
    }

    public static ContainerType<ColoredWorkbenchContainer> createContainerType()
    {
        return new ContainerType<>(ColoredWorkbenchContainer::new);
    }

    public ColoredWorkbenchContainer(int windowIdIn, PlayerInventory playerInventoryIn)
    {
        this(windowIdIn, playerInventoryIn, IWorldPosCallable.DUMMY);
    }

    public ColoredWorkbenchContainer(int windowIdIn, PlayerInventory playerInventoryIn,
            final IWorldPosCallable worldPosCallableIn)
    {
        super(ObjectHolder.COLORED_WORKBENCH_CONTAINER_TYPE.get(), windowIdIn);
        worldPosCallable = worldPosCallableIn;
        world = playerInventoryIn.player.world;
        inputSlot = addSlot(new SlotItemHandler(inputInventory, 0, 20, 33));
        outputSlot = addSlot(new SlotItemHandler(outputInventory, 0, 143, 33)
        {
            @Override public boolean isItemValid(@Nonnull ItemStack stack)
            {
                return false;
            }

            @Override public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
            {
                stack.onCrafting(thePlayer.world, thePlayer, stack.getCount());
                outputInventory.onCrafting(thePlayer);
                ItemStack itemStack = inputSlot.decrStackSize(1);
                if (!itemStack.isEmpty())
                {
                    updateRecipeResultSlot();
                }
                return super.onTake(thePlayer, stack);
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
        trackInt(selectedRecipe);
    }

    private void updateRecipeResultSlot()
    {
        if (!this.recipes.isEmpty() && isRecipeIndexValid(selectedRecipe.get()))
        {
            ColoredCraftingRecipe recipe = recipes.get(selectedRecipe.get());
            outputInventory.setRecipeUsed(recipe);
            //noinspection ConstantConditions - null is ok in this case
            outputSlot.putStack(recipe.getCraftingResult(null));
        } else
        {
            outputSlot.putStack(ItemStack.EMPTY);
        }
        detectAndSendChanges();
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
        return inputSlot.getHasStack() && !recipes.isEmpty();
    }

    @Override public boolean enchantItem(PlayerEntity playerIn, int id)
    {
        if (isRecipeIndexValid(id))
        {
            selectedRecipe.set(id);
            updateRecipeResultSlot();
        }
        return true;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 1)
            {
                item.onCreated(itemstack1, playerIn.world, playerIn);
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager()
                    .getRecipe(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, new Inventory(itemstack1), this.world)
                    .isPresent())
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29)
            {
                if (!this.mergeItemStack(itemstack1, 29, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
            {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            slot.onSlotChanged();
            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            this.detectAndSendChanges();
        }
        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        if (slotIn instanceof SlotItemHandler)
        {
            if (((SlotItemHandler) slotIn).getItemHandler() == this.outputInventory)
            {
                return false;
            }
        }
        return super.canMergeSlot(stack, slotIn);
    }

    @Override public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        outputSlot.putStack(ItemStack.EMPTY);
        worldPosCallable.consume((a, b) ->
        {
            clearContainer(playerIn, playerIn.world, inputInventory);
        });
    }

    protected void clearContainer(PlayerEntity playerIn, World worldIn, ItemStackHandler inventoryIn)
    {
        ItemStack tempStack;
        if (!playerIn.isAlive() ||
                playerIn instanceof ServerPlayerEntity && ((ServerPlayerEntity) playerIn).hasDisconnected())
        {
            for (int j = 0; j < inventoryIn.getSlots(); ++j)
            {
                tempStack = inventoryIn.getStackInSlot(j);
                inventoryIn.setStackInSlot(j, ItemStack.EMPTY);
                playerIn.dropItem(tempStack, false);
            }
        } else
        {
            for (int i = 0; i < inventoryIn.getSlots(); ++i)
            {
                tempStack = inventoryIn.getStackInSlot(i);
                inventoryIn.setStackInSlot(i, ItemStack.EMPTY);
                playerIn.inventory.placeItemBackInInventory(worldIn, tempStack);
            }
        }
    }

    @Override public boolean canInteractWith(PlayerEntity playerIn)
    {
        return isWithinUsableDistance(worldPosCallable, playerIn, ObjectHolder.COLORED_CRAFTING_TABLE.get());
    }

    public void onCraftMatrixChanged(ItemStackHandler inventoryIn)
    {
        ItemStack itemStack = inputSlot.getStack();
        if (itemStack.getItem() != inputItemStack.getItem())
        {
            inputItemStack = itemStack.copy();
            updateAvailableRecipes(new Inventory(inventoryIn.getStackInSlot(0)), itemStack);
        }
    }

    private void updateAvailableRecipes(IInventory inventoryIn, ItemStack stack)
    {
        recipes.clear();
        selectedRecipe.set(-1);
        outputSlot.putStack(ItemStack.EMPTY);
        if (!stack.isEmpty())
        {
            recipes =
                    world.getRecipeManager().getRecipes(ObjectHolder.COLORED_CRAFTING_RECIPE_TYPE, inventoryIn, world);
        }
    }
}
