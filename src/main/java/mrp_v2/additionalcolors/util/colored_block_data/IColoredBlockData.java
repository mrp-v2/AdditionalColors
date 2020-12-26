package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public interface IColoredBlockData<T extends Block & IColored>
{
    void makeTextureGenerationPromises(TextureGenerator generator);
    Map<DyeColor, RegistryObject<T>> register();
    boolean requiresTinting();
    void forEachBlock(Consumer<T> consumer);
    ResourceLocation getBaseBlockLoc();
    ResourceLocation getBaseItemLoc();
    void clientSetup(FMLClientSetupEvent event);
    void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer);
    void registerItemModels(ItemModelGenerator generator);
    void registerLootTables(LootTableGenerator generator);
    void registerBlockStatesAndModels(BlockStateGenerator generator);
    void registerRecipes(Consumer<IFinishedRecipe> consumer);
    void registerBlockTags(BlockTagGenerator generator);
    void registerItemTags(ItemTagGenerator generator);
    void generateTranslations(EN_USTranslationGenerator generator);
    Block getBaseBlock();
    boolean doesBaseBlockAlwaysExist();
    DyeColor[] getColors();
    @Nullable ItemGroup getItemGroup();
}
