package com.latmod.warp_pads.client;

import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.latmod.warp_pads.block.TileWarpPad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class RenderWarpPad extends TileEntitySpecialRenderer<TileWarpPad>
{
	private static long debugTimer = 0L;

	@Override
	public void func_192841_a(TileWarpPad te, double rx, double ry, double rz, float partialTicks, int destroyStage, float p_192841_10_)
	{
		double distanceSq = te.getDistanceSq(FTBLibClient.playerX, FTBLibClient.playerY + 1D, FTBLibClient.playerZ);
		double a = getAlpha(distanceSq);

		if (a <= 0.01D)
		{
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(rx + 0.5D, ry, rz + 0.5D);
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		//render
		String name = te.getName();

		GlStateManager.pushMatrix();
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		//GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.enableTexture2D();
		float f1 = 0.02F;
		GlStateManager.translate(0D, 1.5D, 0D);

		double rot = -MathHelper.atan2((te.getPos().getZ() + 0.5F) - FTBLibClient.playerZ, (te.getPos().getX() + 0.5F) - FTBLibClient.playerX) * MathUtils.DEG + 90D;
		GlStateManager.rotate((float) rot, 0F, 1F, 0F);
		GlStateManager.scale(-f1, -f1, f1);

		GlStateManager.rotate(0F, 0F, 1F, 0F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().fontRendererObj.drawString(name, -(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2), -8, 0xFFFFFF | ((int) (a * 255D)) << 24);
		GlStateManager.popMatrix();

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableBlend();
	}

	private void drawRect(double x, double y, double w, double h, double z)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		buffer.pos(x, y, z).endVertex();
		buffer.pos(x + w, y, z).endVertex();
		buffer.pos(x + w, y + h, z).endVertex();
		buffer.pos(x, y + h, z).endVertex();
		tessellator.draw();
	}

	private double getAlpha(double distSq)
	{
		if (distSq < 6.1D * 6.1D)
		{
			return 1D;
		}

		if (distSq > 7.9D * 7.9D)
		{
			return 0D;
		}

		return MathHelper.clamp(MathUtils.map(MathUtils.sqrt(distSq), 6D, 8D, 1D, 0D), 0D, 1D);
	}
}