package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.awt.image.BufferedImage;

public abstract class ExistingBaseBlockData<T extends Block & IColored> extends ColoredBlockData<T>
{
    protected final Block baseBlock;
    protected final double levelAdjustment;

    protected ExistingBaseBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        this(baseBlock, 0.75d, blockTagsToAddTo, itemTagsToAddTo);
    }

    protected ExistingBaseBlockData(Block baseBlock, double levelAdjustment, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlock.getRegistryName().getPath(), blockTagsToAddTo, itemTagsToAddTo);
        this.baseBlock = baseBlock;
        this.levelAdjustment = levelAdjustment;
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        generator.promiseGeneration(
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath()));
    }

    @Override public boolean requiresTinting()
    {
        return true;
    }

    @Override public ResourceLocation getBaseBlockLoc()
    {
        return baseBlock.getRegistryName();
    }

    @Override public ResourceLocation getBaseItemLoc()
    {
        return baseBlock.asItem().getRegistryName();
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage texture = generator.getTexture(new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                "block/" + baseBlock.getRegistryName().getPath()));
        TextureProvider.makeGrayscale(texture);
        TextureProvider.adjustLevels(texture, levelAdjustment);
        generator.finish(texture, null,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath()), consumer);
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            T block = blockObject.get();
            generator.withExistingParent(block.getRegistryName().getPath(),
                    generator.modLoc("block/" + baseBlock.getRegistryName().getPath()));
        }
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            T block = blockObject.get();
            generator.addLootTable(block, generator::registerDropSelfLootTable);
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ResourceLocation textureLoc = generator.modLoc("block/" + baseBlock.getRegistryName().getPath());
        generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                .parent(generator.models().getExistingFile(generator.mcLoc("block/block"))).texture("all", textureLoc)
                .texture("particle", textureLoc).element().from(0, 0, 0).to(16, 16, 16)
                .allFaces((face, faceBuilder) -> faceBuilder.tintindex(0).texture("#all").cullface(face).end()).end();
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            generator.simpleBlock(blockObject.get(), generator.models()
                    .getExistingFile(generator.modLoc("block/" + baseBlock.getRegistryName().getPath())));
        }
    }

    @Override public Block getBaseBlock()
    {
        return baseBlock;
    }

    @Override public boolean doesBaseBlockAlwaysExist()
    {
        return true;
    }
}
