package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ColoredObsidianGlassBlock extends GlassBlock implements IColored
{
    private final DyeColor color;

    public ColoredObsidianGlassBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

    @Override public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return adjacentBlockState.getBlock().asItem().isIn(ItemTags.getCollection()
                .getTagByID(new ResourceLocation(AdditionalColors.ID, "crying_obsidian_glass"))) ||
                super.isSideInvisible(state, adjacentBlockState, side);
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
