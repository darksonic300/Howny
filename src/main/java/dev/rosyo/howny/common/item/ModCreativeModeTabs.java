package dev.rosyo.howny.common.item;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.EnchantmentRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LightBlock;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Howny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab HOWNY_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        HOWNY_TAB = event.registerCreativeModeTab(new ResourceLocation(Howny.MOD_ID, "howny_tab"),
                builder -> builder.icon(() -> new ItemStack(ItemRegistry.BEE_HEART.get()))
                        .title(Component.translatable("creativemodetab.howny_tab")));
    }
}
