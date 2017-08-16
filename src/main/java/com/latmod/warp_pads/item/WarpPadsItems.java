package com.latmod.warp_pads.item;

import com.feed_the_beast.ftbl.lib.block.ItemBlockBase;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.latmod.warp_pads.WarpPads;
import com.latmod.warp_pads.block.BlockWarpPad;
import com.latmod.warp_pads.block.TileWarpPad;
import com.latmod.warp_pads.block.TileWarpPadPart;
import com.latmod.warp_pads.client.RenderWarpPad;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
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
	public static final Block WARP_PAD = Blocks.AIR;

	public static final Item WARP_WHISTLE = Items.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockWarpPad()
		);

		GameRegistry.registerTileEntity(TileWarpPad.class, WarpPads.MOD_ID + ":warp_pad");
		GameRegistry.registerTileEntity(TileWarpPadPart.class, WarpPads.MOD_ID + ":warp_pad_part");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBase(WARP_PAD),
				new ItemWarpWhistle());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientUtils.registerModel(WARP_PAD);
		ClientUtils.registerModel(WARP_WHISTLE);

		ClientRegistry.bindTileEntitySpecialRenderer(TileWarpPad.class, new RenderWarpPad());

		OBJLoader.INSTANCE.addDomain(WarpPads.MOD_ID);
	}
}