package dev.rosyo.howny.common.imbuement;

import dev.rosyo.howny.Howny;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class Imbuements {
    public static final DeferredRegister<Imbuement> IMBUEMENTS = DeferredRegister.create(ImbuementRegistry.IMBUEMENTS, Howny.MOD_ID);


    public static void register(IEventBus eventBus) {
        IMBUEMENTS.register(eventBus);
    }

}
