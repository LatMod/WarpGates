package com.latmod.warp_pads.client;

import com.latmod.warp_pads.WarpPads;
import com.latmod.warp_pads.WarpPadsCommon;
import com.latmod.warp_pads.block.TileWarpPad;
import com.latmod.warp_pads.item.WarpPadsItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = WarpPads.MOD_ID, value = Side.CLIENT)
public class WarpPadsClient extends WarpPadsCommon
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(WarpPadsItems.WARP_PAD), 0, new ModelResourceLocation(WarpPadsItems.WARP_PAD.getRegistryName() + "#inventory"));
		ModelLoader.setCustomModelResourceLocation(WarpPadsItems.WARP_WHISTLE, 0, new ModelResourceLocation(WarpPadsItems.WARP_WHISTLE.getRegistryName() + "#inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileWarpPad.class, new RenderWarpPad());

		OBJLoader.INSTANCE.addDomain(WarpPads.MOD_ID);
	}
}