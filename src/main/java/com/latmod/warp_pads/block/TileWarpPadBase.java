package com.latmod.warp_pads.block;

import com.feed_the_beast.ftbl.lib.block.EnumHorizontalOffset;
import com.feed_the_beast.ftbl.lib.tile.TileBase;

/**
 * @author LatvianModder
 */
public abstract class TileWarpPadBase extends TileBase
{
	public boolean checkUpdates = true;

	public abstract EnumHorizontalOffset getPart();

	public void onNeighborChange()
	{
		if (checkUpdates && !BlockWarpPad.canExist(world, pos))
		{
			world.setBlockToAir(pos);
		}
	}
}