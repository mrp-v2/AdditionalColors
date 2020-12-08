package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
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
        for (RegistryObject<ColoredCryingObsidianBlock> blockObj : ObjectHolder.COLORED_CRYING_OBSIDIAN_BLOCKS.values())
        {
            this.simpleBlock(blockObj.get());
        }
        for (Block block : ObjectHolder.COLORIZED_BLOCK_MAP.keySet())
        {
            ResourceLocation textureLoc = modLoc("block/" + block.getRegistryName().getPath());
            BlockModelBuilder modelBuilder = models().getBuilder(block.getRegistryName().getPath())
                    .parent(models().getExistingFile(mcLoc("block/block"))).texture("all", textureLoc)
                    .texture("particle", textureLoc).element().from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, faceBuilder) -> faceBuilder.tintindex(0).texture("#all").cullface(dir)).end();
            for (RegistryObject<ColoredBlock> blockObj : ObjectHolder.COLORIZED_BLOCK_MAP.get(block))
            {
                simpleBlock(blockObj.get(), modelBuilder);
            }
        }
    }
}
