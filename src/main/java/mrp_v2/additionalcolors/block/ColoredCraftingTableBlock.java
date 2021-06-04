package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.inventory.container.ColoredWorkbenchContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ColoredCraftingTableBlock extends Block
{
    public static final String ID = "colored_crafting_table";

    public ColoredCraftingTableBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
            Hand handIn, BlockRayTraceResult hit)
    {
        if (worldIn.isClientSide)
        {
            return ActionResultType.SUCCESS;
        } else
        {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return ActionResultType.CONSUME;
        }
    }

    @Nullable @Override public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos)
    {
        return new SimpleNamedContainerProvider((id, inventory, player) -> new ColoredWorkbenchContainer(id, inventory,
                IWorldPosCallable.create(worldIn, pos)), ColoredWorkbenchContainer.NAME);
    }
}
