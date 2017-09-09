package com.latmod.warp_gates.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.warp_gates.block.TileWarpGate;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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
	public void toBytes(ByteBuf io)
	{
		NetUtils.writePos(io, pos);
		ByteBufUtils.writeUTF8String(io, name);
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		pos = NetUtils.readPos(io);
		name = ByteBufUtils.readUTF8String(io);
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