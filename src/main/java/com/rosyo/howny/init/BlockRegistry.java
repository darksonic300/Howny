package com.rosyo.howny.init;

import com.rosyo.howny.Howny;
import com.rosyo.howny.blocks.HoneyCauldron;
import com.rosyo.howny.blocks.HoneyExtractorBlock;
import com.rosyo.howny.blocks.HoneyTapBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.CAULDRON;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Howny.MODID);

    public static final RegistryObject<Block> HONEY_TAP = registerBlock("honey_tap",
            () -> new HoneyTapBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .noOcclusion().dynamicShape().instabreak()));
    public static final RegistryObject<Block> HONEY_CAULDRON = registerBlock("honey_cauldron",
            () -> new HoneyCauldron(BlockBehaviour.Properties.copy(CAULDRON)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}