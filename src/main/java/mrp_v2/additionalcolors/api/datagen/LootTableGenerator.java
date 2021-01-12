package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;

import java.util.function.Function;

public class LootTableGenerator extends BlockLootTables
{
    @Override public void registerLootTable(Block blockIn, Function<Block, LootTable.Builder> factory)
    {
        super.registerLootTable(blockIn, factory);
    }
}
