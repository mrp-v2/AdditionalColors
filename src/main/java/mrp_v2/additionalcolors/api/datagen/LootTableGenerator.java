package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.Function;

public class LootTableGenerator extends BlockLootTables
{
    @Override public void add(Block blockIn, Function<Block, LootTable.Builder> factory)
    {
        super.add(blockIn, factory);
    }
}
