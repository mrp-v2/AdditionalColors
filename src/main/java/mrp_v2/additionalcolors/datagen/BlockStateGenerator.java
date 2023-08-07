package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider
{
    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
    }

    @Override protected void registerStatesAndModels()
    {
        ResourceLocation side = modLoc("block/colored_crafting_table_side");
        ModelFile model =
                models().withExistingParent(ObjectHolder.COLORED_CRAFTING_TABLE.getId().toString(), mcLoc("block/cube"))
                        .texture("particle", side).texture("north", side).texture("south", side).texture("east", side)
                        .texture("west", side).texture("up", modLoc("block/colored_crafting_table_top"))
                        .texture("down", mcLoc("block/oak_planks"));
        this.simpleBlock(ObjectHolder.COLORED_CRAFTING_TABLE.get(), model);
    }
}
