package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.inventory.container.ColoredWorkbenchContainer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredCraftingTableBlock extends Block
{
    public static final String ID = "colored_crafting_table";

    public ColoredCraftingTableBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
            InteractionHand handIn, BlockHitResult hit)
    {
        if (worldIn.isClientSide)
        {
            return InteractionResult.SUCCESS;
        } else
        {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable @Override public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        return new SimpleMenuProvider((id, inventory, player) -> new ColoredWorkbenchContainer(id, inventory,
                ContainerLevelAccess.create(worldIn, pos)), ColoredWorkbenchContainer.NAME);
    }
}
