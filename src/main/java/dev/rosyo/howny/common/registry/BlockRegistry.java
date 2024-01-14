package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.CAULDRON;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Howny.MOD_ID);

    public static final RegistryObject<Block> HONEY_TAP = registerBlock("honey_tap",
            () -> new HoneyTapBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .noOcclusion().dynamicShape().instabreak().randomTicks()));
    public static final RegistryObject<Block> HONEY_CAULDRON = registerBlockWithoutItem("honey_cauldron",
            () -> new HoneyCauldronBlock(BlockBehaviour.Properties.copy(CAULDRON)));

    public static final RegistryObject<Block> HONEY_PUDDLE = registerBlock("honey_puddle",
            () -> new HoneyPuddleBlock(BlockBehaviour.Properties.of().noCollission().pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_ORANGE).speedFactor(0.4F).jumpFactor(0.5F).noOcclusion().sound(SoundType.HONEY_BLOCK)));

    public static final RegistryObject<Block> FLOWERING_LOG_ALTAR = registerBlock("flowering_log_altar",
            () -> new FloweringLogAltarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    .strength(6f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> HONEY_BRICKS = registerBlock("honey_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.HONEY_BLOCK)));

    public static final RegistryObject<Block> CHISELED_HONEY_BRICKS = registerBlock("chiseled_honey_bricks",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.HONEY_BLOCK)));


    /**
     * Method that registers Blocks and their respective item.
     */
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerOneCountBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerOneCountBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<Item> registerOneCountBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(1)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
