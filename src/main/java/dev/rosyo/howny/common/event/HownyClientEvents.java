package dev.rosyo.howny.common.event;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.client.renderer.FloweringLogAltarEntityRenderer;
import dev.rosyo.howny.common.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class HownyClientEvents {
    @Mod.EventBusSubscriber(modid = Howny.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BlockEntityRegistry.FLOWERING_LOG_ALTAR.get(),
                    FloweringLogAltarEntityRenderer::new);
        }
    }
}
