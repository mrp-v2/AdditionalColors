package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BlockStateGenerator extends BlockStateProvider
{
    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
    }
}
