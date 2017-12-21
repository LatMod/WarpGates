package com.latmod.warp_gates;

import com.feed_the_beast.ftblib.lib.block.ItemBlockBase;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.latmod.warp_gates.block.BlockWarpGate;
import com.latmod.warp_gates.block.TileWarpGate;
import com.latmod.warp_gates.client.RenderWarpGate;
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
@GameRegistry.ObjectHolder(WarpGates.MOD_ID)
@Mod.EventBusSubscriber(modid = WarpGates.MOD_ID)
public class WarpGatesItems
{
	public static final Block WARP_GATE = Blocks.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new BlockWarpGate("warp_gate")
		);

		GameRegistry.registerTileEntity(TileWarpGate.class, WarpGates.MOD_ID + ":warp_gate");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlockBase(WARP_GATE)
		);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientUtils.registerModel(WARP_GATE);

		ClientRegistry.bindTileEntitySpecialRenderer(TileWarpGate.class, new RenderWarpGate());
	}
}