package dev.rosyo.howny.common.client.model;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.entity.HoneyGolem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HoneyGolemModel extends DefaultedEntityGeoModel<HoneyGolem> {
    public HoneyGolemModel() {
        super(new ResourceLocation(Howny.MOD_ID, "honey_golem"), true);
    }
}
