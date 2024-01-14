package dev.rosyo.howny.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.rosyo.howny.common.entity.FloweringLogAltarEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class FloweringLogAltarEntityRenderer implements BlockEntityRenderer<FloweringLogAltarEntity> {

    public FloweringLogAltarEntityRenderer(BlockEntityRendererProvider.Context context){

    }


    @Override
    public void render(FloweringLogAltarEntity altarEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack itemStack = altarEntity.getRenderStack();
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.1f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
    }
}
