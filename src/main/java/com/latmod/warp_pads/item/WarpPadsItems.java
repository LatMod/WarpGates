package com.latmod.warp_pads.item;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.latmod.warp_pads.WarpPads;
import com.latmod.warp_pads.block.BlockPortal;
import com.latmod.warp_pads.block.TilePortal;
import com.latmod.warp_pads.client.RenderPortal;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(WarpPads.MOD_ID)
@Mod.EventBusSubscriber(modid = WarpPads.MOD_ID)
public class WarpPadsItems
{
	public static final Block PORTAL = Blocks.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockPortal()
		);

		GameRegistry.registerTileEntity(TilePortal.class, WarpPads.MOD_ID + ":portal");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBase(PORTAL)
		);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientUtils.registerModel(PORTAL);

		ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new RenderPortal());
	}
}