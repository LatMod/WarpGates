package com.latmod.warp_gates.net;

import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.latmod.warp_gates.block.TileWarpGate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class MessageToggleActive extends MessageToServer<MessageToggleActive>
{
	private BlockPos pos;

	public MessageToggleActive()
	{
	}

	public MessageToggleActive(BlockPos p)
	{
		pos = p;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return WarpGatesNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writePos(pos);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
	}

	@Override
	public void onMessage(MessageToggleActive m, EntityPlayer player)
	{
		TileEntity te = player.world.getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			TileWarpGate teleporter = (TileWarpGate) te;

			if (teleporter.isOwner(player.getGameProfile().getId()))
			{
				teleporter.active = !teleporter.active;
				teleporter.markDirty();
			}
		}
	}
}