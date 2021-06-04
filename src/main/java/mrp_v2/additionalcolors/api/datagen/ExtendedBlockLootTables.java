package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;

public abstract class ExtendedBlockLootTables extends BlockLootTables
{
    public static LootTable.Builder droppingSlab(Block slab)
    {
        return net.minecraft.data.loot.BlockLootTables.createSlabItemTable(slab);
    }

    public static LootTable.Builder droppingDoor(Block door)
    {
        return net.minecraft.data.loot.BlockLootTables.createDoorTable(door);
    }
}
