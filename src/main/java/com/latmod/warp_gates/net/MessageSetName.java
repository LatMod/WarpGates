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
public class MessageSetName extends MessageToServer<MessageSetName>
{
	private BlockPos pos;
	private String name;

	public MessageSetName()
	{
	}

	public MessageSetName(BlockPos p, String n)
	{
		pos = p;
		name = n;
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
		data.writeString(name);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		name = data.readString();
	}

	@Override
	public void onMessage(MessageSetName m, EntityPlayer player)
	{
		TileEntity te = player.world.getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			TileWarpGate teleporter = (TileWarpGate) te;

			if (teleporter.isOwner(player.getGameProfile().getId()))
			{
				teleporter.setName(m.name);
			}
		}
	}
}