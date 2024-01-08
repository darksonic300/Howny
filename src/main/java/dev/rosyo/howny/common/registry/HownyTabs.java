package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HownyTabs {
    public static final DeferredRegister<CreativeModeTab> HOWNY_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Howny.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = HOWNY_TABS.register("howny_main_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Howny.MOD_ID + ".howny_tab"))
            .icon(() -> new ItemStack(ItemRegistry.HONEY_COOKIE.get())).build());

    public static void register(IEventBus eventBus){
        HOWNY_TABS.register(eventBus);
    }
}
