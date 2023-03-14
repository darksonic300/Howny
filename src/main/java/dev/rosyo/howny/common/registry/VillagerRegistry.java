package dev.rosyo.howny.common.registry;

import com.google.common.collect.ImmutableSet;
import dev.rosyo.howny.Howny;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class VillagerRegistry {
    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Howny.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Howny.MOD_ID);

    public static final RegistryObject<PoiType> BEE_KEEPER_POI = POI_TYPES.register("bee_keeper_poi",
            () -> new PoiType(getAllStates(BlockRegistry.APIARY_BLOCK.get()), 2, 16));

    public static final RegistryObject<VillagerProfession> BEE_KEEPER =
            VILLAGER_PROFESSION.register("bee_keeper_profession",
                    () -> new VillagerProfession(
                            "bee_keeper_profession",
                            x -> x.get() == BEE_KEEPER_POI.get(), x -> x.get() == BEE_KEEPER_POI.get(),
                            ImmutableSet.of(), ImmutableSet.of(),
                            SoundEvents.BEEHIVE_ENTER));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSION.register(eventBus);
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
