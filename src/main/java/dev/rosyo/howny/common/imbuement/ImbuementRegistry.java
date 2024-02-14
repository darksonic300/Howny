package dev.rosyo.howny.common.imbuement;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class ImbuementRegistry {

    static { init(); }

    public static final IForgeRegistry<Imbuement> IMBUEMENTS = RegistryManager.ACTIVE.getRegistry(Keys.IMBUEMENTS);

    public static final class Keys {
        public static final ResourceKey<Registry<Imbuement>> IMBUEMENTS = key("imbuement");

        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(new ResourceLocation(name));
        }
        private static void init() {}
    }

    /**
     * This function is just to make sure static initializers in other classes have run and setup their registries before we query them.
     */
    private static void init() {
        ImbuementRegistry.Keys.init();
    }
}
