package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.world.storage.loot.LootTable;

import java.util.function.Function;

public class LootTableGenerator extends BlockLootTables
{
    public LootTableGenerator()
    {
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.registerLootTables(this));
    }

    public static LootTable.Builder droppingSlab(Block slab)
    {
        return net.minecraft.data.loot.BlockLootTables.droppingSlab(slab);
    }

    public static LootTable.Builder droppingDoor(Block door)
    {
        return net.minecraft.data.loot.BlockLootTables.droppingWhen(door, DoorBlock.HALF, DoubleBlockHalf.LOWER);
    }

    @Override public void registerLootTable(Block blockIn, Function<Block, LootTable.Builder> factory)
    {
        super.registerLootTable(blockIn, factory);
    }
}
