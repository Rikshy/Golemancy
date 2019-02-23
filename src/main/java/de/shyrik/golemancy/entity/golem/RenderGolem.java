package de.shyrik.golemancy.entity.golem;

import de.shyrik.golemancy.Golemancy;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderGolem extends RenderLiving<EntityModGolem> {

    private static final ResourceLocation TEX = new ResourceLocation(Golemancy.MODID, "textures/entity/golem.png");

    public RenderGolem(RenderManager manager) {
        super(manager, new ModelGolem(), 0.2F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityModGolem entityModGolem) {
        return TEX;
    }
}
