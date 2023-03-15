package dev.rosyo.howny;

import com.mojang.logging.LogUtils;
import dev.rosyo.howny.common.item.ModCreativeModeTabs;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.EnchantmentRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import dev.rosyo.howny.common.registry.VillagerRegistry;
import dev.rosyo.howny.common.util.HoneyCauldronInteraction;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Howny.MOD_ID)
public class Howny {
    public static final String MOD_ID = "howny";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Howny() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BlockRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        EnchantmentRegistry.register(modEventBus);
        VillagerRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(HoneyCauldronInteraction::bootStrap);
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == ModCreativeModeTabs.HOWNY_TAB) {
            event.accept(ItemRegistry.BEE_HEART.get());
            event.accept(ItemRegistry.HONEY_COOKIE.get());
            event.accept(BlockRegistry.HONEY_TAP.get());
            event.accept(BlockRegistry.APIARY_BLOCK.get());
            addEnchantmentBooks(EnchantmentRegistry.STING.get(), event);
        }
    }

    // Used to add an enchanted book item with every level of it
    void addEnchantmentBooks(Enchantment enchantment , CreativeModeTabEvent.BuildContents event) {
        for(int i = 1; i < enchantment.getMaxLevel(); i++) {
            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
        }
    }
}
