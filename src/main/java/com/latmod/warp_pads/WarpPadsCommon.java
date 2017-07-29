package com.latmod.warp_pads;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.latmod.warp_pads.block.BlockWarpPad;
import com.latmod.warp_pads.block.TileWarpPad;
import com.latmod.warp_pads.block.TileWarpPadPart;
import com.latmod.warp_pads.item.ItemWarpWhistle;
import com.latmod.warp_pads.item.WarpPadsItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = WarpPads.MOD_ID)
public class WarpPadsCommon
{
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockWarpPad()
		);

		GameRegistry.registerTileEntity(TileWarpPad.class, "warp_pads.warp_pad");
		GameRegistry.registerTileEntity(TileWarpPadPart.class, "warp_pads.warp_pad_part");

		System.out.println("BLOCK REGISTRY");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBase(WarpPadsItems.WARP_PAD),
				new ItemWarpWhistle());
	}
}