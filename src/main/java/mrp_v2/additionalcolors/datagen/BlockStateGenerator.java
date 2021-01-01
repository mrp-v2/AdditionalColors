package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BlockStateGenerator extends BlockStateProvider
{
    public static final ResourceLocation CUBE_TINTED = makeTintedLoc("cube");
    public static final ResourceLocation CUBE_ALL_TINTED = makeTintedLoc("cube_all");
    public static final ResourceLocation SLAB_TINTED = makeTintedLoc("slab");
    public static final ResourceLocation SLAB_TOP_TINTED = makeTintedLoc("slab_top");
    public static final ResourceLocation STAIRS_TINTED = makeTintedLoc("stairs");
    public static final ResourceLocation STAIRS_INNER_TINTED = makeTintedLoc("stairs_inner");
    public static final ResourceLocation STAIRS_OUTER_TINTED = makeTintedLoc("stairs_outer");
    public static final HashMap<Direction, Pair<Integer, Integer>> FACE_ROTATION_MAP;
    private static final BiConsumer<Direction, ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder>
            tintFunction = (direction, builder) -> builder.tintindex(0);

    static
    {
        FACE_ROTATION_MAP = new HashMap<>();
        FACE_ROTATION_MAP.put(Direction.NORTH, Pair.of(0, 0));
        FACE_ROTATION_MAP.put(Direction.EAST, Pair.of(0, 90));
        FACE_ROTATION_MAP.put(Direction.SOUTH, Pair.of(0, 180));
        FACE_ROTATION_MAP.put(Direction.WEST, Pair.of(0, -90));
        FACE_ROTATION_MAP.put(Direction.UP, Pair.of(-90, 0));
        FACE_ROTATION_MAP.put(Direction.DOWN, Pair.of(90, 0));
    }

    private static ResourceLocation makeTintedLoc(String loc)
    {
        return makeLoc(loc + "_tinted");
    }

    private static ResourceLocation makeLoc(String loc)
    {
        return new ResourceLocation(AdditionalColors.ID, "block/" + loc);
    }

    public ModelFile tintedSimpleBlock(String path, ResourceLocation textureLoc)
    {
        return models().withExistingParent(path, CUBE_ALL_TINTED).texture("all", textureLoc);
    }

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
    }

    @Override protected void registerStatesAndModels()
    {
        this.registerFullBlockModels();
        this.registerSlabModels();
        this.registerStairModels();
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.registerBlockStatesAndModels(this));
    }

    private void registerFullBlockModels()
    {
        this.models().withExistingParent(CUBE_TINTED.toString(), mcLoc("block/block")).element().from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((direction, builder) -> builder.cullface(direction).texture("#" + direction.toString()).end())
                .faces(tintFunction).end();
        ModelBuilder<BlockModelBuilder> cubeAllTintedBuilder =
                this.models().withExistingParent(CUBE_ALL_TINTED.toString(), CUBE_TINTED).texture("particle", "#all");
        for (Direction direction : Direction.values())
        {
            cubeAllTintedBuilder = cubeAllTintedBuilder.texture(direction.toString(), "#all");
        }
    }

    private void registerSlabModels()
    {
        ModelBuilder<BlockModelBuilder>.ElementBuilder slabTintedBuilder =
                this.models().withExistingParent(SLAB_TINTED.toString(), mcLoc("block/block"))
                        .texture("particle", "#side").element();
        ModelBuilder<BlockModelBuilder>.ElementBuilder slabTopTintedBuilder =
                this.models().getBuilder(SLAB_TOP_TINTED.toString()).texture("particle", "#side").element();
        slabTintedBuilder.from(0, 0, 0).to(16, 8, 16).face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN)
                .end().face(Direction.UP).texture("#top").end();
        slabTopTintedBuilder.from(0, 8, 0).to(16, 16, 16).face(Direction.DOWN).texture("#bottom").end()
                .face(Direction.UP).texture("#top").cullface(Direction.UP).end();
        BiConsumer<ModelBuilder<BlockModelBuilder>.ElementBuilder, Direction> sideBuilder =
                (builder, side) -> builder.face(side).texture("#side").cullface(side).end();
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            sideBuilder.accept(slabTintedBuilder, direction);
            sideBuilder.accept(slabTopTintedBuilder, direction);
        }
        slabTintedBuilder.faces(tintFunction).end();
        slabTopTintedBuilder.faces(tintFunction).end();
    }

    private void registerStairModels()
    {
        Consumer<ModelBuilder<BlockModelBuilder>> firstElementBuilder = (builder) ->
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder elementBuilder =
                    builder.element().from(0, 0, 0).to(16, 8, 16).face(Direction.DOWN).texture("#bottom")
                            .cullface(Direction.DOWN).end().face(Direction.UP).texture("#top").end();
            for (Direction side : Direction.Plane.HORIZONTAL)
            {
                elementBuilder.face(side).texture("#side").cullface(side).end();
            }
            elementBuilder.end();
            elementBuilder.faces(tintFunction);
        };
        BiFunction<ModelBuilder<BlockModelBuilder>.ElementBuilder, Direction[], ModelBuilder<BlockModelBuilder>.ElementBuilder>
                basicCullFaceBuilder = (builder, directions) ->
        {
            for (Direction side : directions)
            {
                builder.face(side).texture("#side").cullface(side).end();
            }
            return builder;
        };
        Consumer<ModelBuilder<BlockModelBuilder>> stairsAndInnerSecondElementBuilder = (builder) ->
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder elementBuilder =
                    builder.element().from(8, 8, 0).to(16, 16, 16);
            basicCullFaceBuilder
                    .apply(elementBuilder, new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST});
            elementBuilder.face(Direction.UP).texture("#top").cullface(Direction.UP).end();
            elementBuilder.face(Direction.WEST).texture("#side").end();
            elementBuilder.faces(tintFunction);
        };
        ModelBuilder<BlockModelBuilder> stairsBuilder =
                this.models().withExistingParent(STAIRS_TINTED.toString(), mcLoc("block/block")).transforms()
                        .transform(ModelBuilder.Perspective.GUI).rotation(30, 135, 0).scale(0.625F, 0.625F, 0.625F)
                        .end().transform(ModelBuilder.Perspective.HEAD).rotation(0, -90, 0).scale(1, 1, 1).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(75, -135, 0)
                        .translation(0, 2.5F, 0).scale(0.375F, 0.375F, 0.375F).end().end().texture("particle", "#side");
        firstElementBuilder.accept(stairsBuilder);
        stairsAndInnerSecondElementBuilder.accept(stairsBuilder);
        ModelBuilder<BlockModelBuilder> stairsInnerBuilder =
                this.models().getBuilder(STAIRS_INNER_TINTED.toString()).texture("particle", "#side");
        firstElementBuilder.accept(stairsInnerBuilder);
        stairsAndInnerSecondElementBuilder.accept(stairsInnerBuilder);
        basicCullFaceBuilder.apply(stairsInnerBuilder.element().from(0, 8, 8).to(8, 16, 16).face(Direction.UP)
                        .cullface(Direction.UP).texture("#top").end().face(Direction.NORTH).texture("#side").end(),
                new Direction[]{Direction.SOUTH, Direction.WEST}).faces(tintFunction).end();
        ModelBuilder<BlockModelBuilder> stairsOuterBuilder =
                this.models().getBuilder(STAIRS_OUTER_TINTED.toString()).texture("particle", "#side");
        firstElementBuilder.accept(stairsOuterBuilder);
        stairsOuterBuilder.element().from(8, 8, 8).to(16, 16, 16).face(Direction.UP).cullface(Direction.UP)
                .texture("#top").end().face(Direction.NORTH).texture("#side").end().face(Direction.SOUTH)
                .cullface(Direction.SOUTH).texture("#side").end().face(Direction.WEST).texture("#side").end()
                .face(Direction.EAST).cullface(Direction.EAST).texture("#side").end().faces(tintFunction).end();
    }
}
