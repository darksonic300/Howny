package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.entity.Bear;
import dev.rosyo.howny.common.entity.HoneyGolem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Howny.MOD_ID);

    public static final RegistryObject<EntityType<HoneyGolem>> HONEY_GOLEM = register("honey_golem",
            EntityType.Builder.<HoneyGolem>of(HoneyGolem::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(HoneyGolem::new)
                    .sized(0.5F, 0.8F));

    public static final RegistryObject<EntityType<Bear>> BEAR = register("bear",
            EntityType.Builder.<Bear>of(Bear::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Bear::new)
                    .sized(1.5F, 1.5F));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(registryname, () -> entityTypeBuilder.build(registryname));
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HONEY_GOLEM.get(), HoneyGolem.createAttributes().build());
        event.put(BEAR.get(), Bear.createAttributes().build());
    }
}
