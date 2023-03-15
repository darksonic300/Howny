package dev.rosyo.howny.common.tab;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.EnchantmentRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Howny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HownyTab {
    public static CreativeModeTab HOWNY_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        HOWNY_TAB = event.registerCreativeModeTab(new ResourceLocation(Howny.MOD_ID, "howny_tab"),
                builder -> builder.icon(() -> new ItemStack(ItemRegistry.BEE_HEART.get()))
                        .title(Component.translatable("creativemodetab.howny_tab")));
    }


    public static void addItemsToTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == HownyTab.HOWNY_TAB) {
            for (RegistryObject<Block> block : BlockRegistry.BLOCKS.getEntries()) {
                ItemStack displayedBlock = block.get().asItem().getDefaultInstance();
                displayedBlock.setCount(1);

                if (displayedBlock.getCount() != 1) {
                    Howny.LOGGER.error("Block " + displayedBlock.getDisplayName() + " can't be added to creative tab. Count is not equal to 1");
                    continue;
                }

                event.accept(displayedBlock);
            }

            for (RegistryObject<Item> item : ItemRegistry.ITEMS.getEntries()) {
                ItemStack displayedBlock = item.get().asItem().getDefaultInstance();
                displayedBlock.setCount(1);

                if (displayedBlock.getCount() != 1) {
                    Howny.LOGGER.error("Item " + displayedBlock.getDisplayName() + " can't be added to creative tab. Count is not equal to 1");
                    continue;
                }

                event.accept(displayedBlock);            }

            //Add book registry
            for (RegistryObject<Enchantment> enchantment : EnchantmentRegistry.ENCHANTMENTS.getEntries()) {
                for (int registeredLevel = 1; registeredLevel < enchantment.get().getMaxLevel(); registeredLevel++) {
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment.get(), registeredLevel)));
                }
            }
        }
    }
}
