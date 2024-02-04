package dev.rosyo.howny.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.rosyo.howny.common.block.entity.FloweringLogEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;


public class FloweringLogAltarEntityRenderer implements BlockEntityRenderer<FloweringLogEntity> {

    public FloweringLogAltarEntityRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public void render(FloweringLogEntity altarEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
