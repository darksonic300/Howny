package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.entity.FloweringLogAltarEntity;
import dev.rosyo.howny.server.packets.HownyMessages;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Howny.MOD_ID);

    public static final RegistryObject<BlockEntityType<FloweringLogAltarEntity>> FLOWERING_LOG =
            BLOCK_ENTITIES.register("flowering_log_altar", () ->
                    BlockEntityType.Builder.of(FloweringLogAltarEntity::new,
                            BlockRegistry.FLOWERING_LOG.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}