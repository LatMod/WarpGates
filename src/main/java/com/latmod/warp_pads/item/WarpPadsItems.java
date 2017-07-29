package com.latmod.warp_pads.item;

import com.latmod.warp_pads.WarpPads;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(WarpPads.MOD_ID)
public class WarpPadsItems
{
	public static final Block WARP_PAD = Blocks.AIR;

	public static final Item WARP_WHISTLE = Items.AIR;
}