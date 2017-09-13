package com.latmod.warp_gates.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.warp_gates.block.TileWarpGate;
import io.netty.buffer.ByteBuf;
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
	public void toBytes(ByteBuf io)
	{
		NetUtils.writePos(io, pos);
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		pos = NetUtils.readPos(io);
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