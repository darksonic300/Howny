package dev.rosyo.howny.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.client.model.HoneyGolemModel;
import dev.rosyo.howny.common.entity.HoneyGolem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HoneyGolemRenderer extends GeoEntityRenderer<HoneyGolem> {

    public HoneyGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HoneyGolemModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(HoneyGolem instance) {
        return new ResourceLocation(Howny.MOD_ID, "textures/entity/honey_golem.png");
    }

    @Override
    public void preRender(PoseStack poseStack, HoneyGolem animatable,
                          BakedGeoModel model, MultiBufferSource bufferSource,
                          VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {

        poseStack.scale(0.8f, 0.8f,0.8f);
        if (animatable.isPassenger())
            poseStack.translate(0.0, 0.25, 0.0);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}