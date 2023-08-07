package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public abstract class ExtendedBlockLootTables extends BlockLootTables
{
    public static LootTable.Builder droppingSlab(Block slab)
    {
        return net.minecraft.data.loot.BlockLoot.createSlabItemTable(slab);
    }

    public static LootTable.Builder droppingDoor(Block door)
    {
        return net.minecraft.data.loot.BlockLoot.createDoorTable(door);
    }
}
