package de.shyrik.golemancy.entity.golem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelBox;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelGolem extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer arms;
	private final ModelRenderer leg0;
	private final ModelRenderer leg1;
	private final ModelRenderer head;

	public ModelGolem() {
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 17.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -2.5F, -3.0F, -1.5F, 5, 6, 3, 0.0F, false));

		arms = new ModelRenderer(this);
		arms.setRotationPoint(0.0F, 14.5F, 0.0F);
		arms.cubeList.add(new ModelBox(arms, 6, 15, -3.5F, -0.5F, -1.0F, 1, 5, 2, 0.0F, false));
		arms.cubeList.add(new ModelBox(arms, 0, 15, 2.5F, -0.5F, -1.0F, 1, 5, 2, 0.0F, false));

		leg0 = new ModelRenderer(this);
		leg0.setRotationPoint(-1.5F, 20.0F, 0.0F);
		leg0.cubeList.add(new ModelBox(leg0, 8, 9, -0.5F, 0.0F, -1.0F, 2, 4, 2, 0.0F, false));

		leg1 = new ModelRenderer(this);
		leg1.setRotationPoint(1.5F, 20.0F, 0.0F);
		leg1.cubeList.add(new ModelBox(leg1, 0, 9, -1.5F, 0.0F, -1.0F, 2, 4, 2, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 12.5F, 0.0F);
		head.cubeList.add(new ModelBox(head, 16, 0, -1.5F, -1.5F, -1.0F, 3, 3, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef(arms.offsetX, arms.offsetY, arms.offsetZ);
		GlStateManager.translatef(arms.rotationPointX * scale, arms.rotationPointY * scale, arms.rotationPointZ * scale);
		GlStateManager.scaled(0.9D, 1.0D, 0.9D);
		GlStateManager.translatef(-arms.offsetX, -arms.offsetY, -arms.offsetZ);
		GlStateManager.translatef(-arms.rotationPointX * scale, -arms.rotationPointY * scale, -arms.rotationPointZ * scale);
		arms.render(scale);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(leg0.offsetX, leg0.offsetY, leg0.offsetZ);
		GlStateManager.translatef(leg0.rotationPointX * scale, leg0.rotationPointY * scale, leg0.rotationPointZ * scale);
		GlStateManager.scaled(0.9D, 1.0D, 0.9D);
		GlStateManager.translatef(-leg0.offsetX, -leg0.offsetY, -leg0.offsetZ);
		GlStateManager.translatef(-leg0.rotationPointX * scale, -leg0.rotationPointY * scale, -leg0.rotationPointZ * scale);
		leg0.render(scale);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(body.offsetX, body.offsetY, body.offsetZ);
		GlStateManager.translatef(body.rotationPointX * scale, body.rotationPointY * scale, body.rotationPointZ * scale);
		GlStateManager.scaled(0.9D, 1.0D, 0.9D);
		GlStateManager.translatef(-body.offsetX, -leg1.offsetY, -body.offsetZ);
		GlStateManager.translatef(-body.rotationPointX * scale, -body.rotationPointY * scale, -body.rotationPointZ * scale);
		body.render(scale);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(leg1.offsetX, leg1.offsetY, leg1.offsetZ);
		GlStateManager.translatef(leg1.rotationPointX * scale, leg1.rotationPointY * scale, leg1.rotationPointZ * scale);
		GlStateManager.scaled(0.9D, 1.0D, 0.9D);
		GlStateManager.translatef(-leg1.offsetX, -leg1.offsetY, -leg1.offsetZ);
		GlStateManager.translatef(-leg1.rotationPointX * scale, -leg1.rotationPointY * scale, -leg1.rotationPointZ * scale);
		leg1.render(scale);
		GlStateManager.popMatrix();
		head.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		head.rotateAngleY = netHeadYaw * 0.0017453292F;
		head.rotateAngleX = headPitch * 0.0017453292F;

		body.rotateAngleY = 0.0F;

		float auxLimbSwing = limbSwing * 5.0F * 0.6662F;
		float armLimbSwingAmount = 2.0F * limbSwingAmount;
		float legLimbSwingAmount = 2.8F * limbSwingAmount;

		arms.rotateAngleX = MathHelper.cos(auxLimbSwing + (float) Math.PI) * armLimbSwingAmount;
		arms.rotateAngleZ = 0.0F;
		leg1.rotateAngleZ = 0.0F;
		leg0.rotateAngleX = MathHelper.cos(auxLimbSwing) * legLimbSwingAmount;
		leg1.rotateAngleX = MathHelper.cos(auxLimbSwing + (float) Math.PI) * legLimbSwingAmount;
		leg0.rotateAngleY = 0.0F;
		leg1.rotateAngleY = 0.0F;
		leg0.rotateAngleZ = 0.0F;
		leg1.rotateAngleZ = 0.0F;

		arms.rotateAngleY = 0.0F;
		arms.rotateAngleZ = 0.0F;

		body.rotateAngleX = 0.0F;

		// Arms idle movement
		//arms.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		//arms.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}
}