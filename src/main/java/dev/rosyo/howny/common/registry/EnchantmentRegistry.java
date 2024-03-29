package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.enchantment.StingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Howny.MOD_ID);

    public static final RegistryObject<Enchantment> STING = ENCHANTMENTS.register("sting",
            () -> new StingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
