package com.latmod.warp_pads.net;

import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.warp_pads.block.TilePortal;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class MessageTogglePrivacy extends MessageToServer<MessageTogglePrivacy>
{
	private BlockPos pos;
	private boolean next;

	public MessageTogglePrivacy()
	{
	}

	public MessageTogglePrivacy(BlockPos p, boolean n)
	{
		pos = p;
		next = n;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return WarpPadsNetHandler.NET;
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		NetUtils.writePos(io, pos);
		io.writeBoolean(next);
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		pos = NetUtils.readPos(io);
		next = io.readBoolean();
	}

	@Override
	public void onMessage(MessageTogglePrivacy m, EntityPlayer player)
	{
		TileEntity te = player.world.getTileEntity(m.pos);

		if (te instanceof TilePortal)
		{
			TilePortal teleporter = (TilePortal) te;

			if (teleporter.isOwner(player.getGameProfile().getId()))
			{
				teleporter.togglePrivacyLevel();
			}
		}
	}
}