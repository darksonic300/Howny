package dev.rosyo.howny.common.recipe;

import dev.rosyo.howny.Howny;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HownyRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Howny.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FloweringLogRecipe>> FLOWRING_LOG_SERIALIZER =
            SERIALIZERS.register("flowering_logged", () -> FloweringLogRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
