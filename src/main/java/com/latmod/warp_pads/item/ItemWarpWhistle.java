package com.latmod.warp_pads.item;

import com.latmod.warp_pads.WarpPads;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author LatvianModder
 */
public class ItemWarpWhistle extends Item
{
	public ItemWarpWhistle()
	{
		setRegistryName(WarpPads.MOD_ID + ":warp_whistle");
		setUnlocalizedName(WarpPads.MOD_ID + ".warp_whistle");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
	}
}