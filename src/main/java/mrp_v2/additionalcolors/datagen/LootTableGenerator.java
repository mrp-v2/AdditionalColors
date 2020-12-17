package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

public class LootTableGenerator extends BlockLootTables
{
    public LootTableGenerator()
    {
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> objPair : ObjectHolder.COLORIZED_BLOCK_MAP
                    .get(entry.getBlock()))
            {
                this.addLootTable(objPair.getLeft().get(), this::registerDropSelfLootTable);
            }
        }
    }
}
