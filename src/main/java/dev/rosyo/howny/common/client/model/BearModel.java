package dev.rosyo.howny.common.client.model;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.entity.Bear;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BearModel extends DefaultedEntityGeoModel<Bear> {
    public BearModel() {
        super(new ResourceLocation(Howny.MOD_ID, "bear"), true);
    }
}
