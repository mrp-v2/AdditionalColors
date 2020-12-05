package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class BlockStateGenerator extends BlockStateProvider
{
    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
    }

    @Override protected void registerStatesAndModels()
    {
        for (RegistryObject<ColoredCryingObsidianBlock> block : ObjectHolder.COLORED_CRYING_OBSIDIAN_BLOCKS.values())
        {
            this.simpleBlock(block.get());
        }
    }
}
