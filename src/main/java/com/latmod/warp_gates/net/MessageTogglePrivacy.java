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
		return WarpGatesNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writePos(pos);
		data.writeBoolean(next);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		next = data.readBoolean();
	}

	@Override
	public void onMessage(MessageTogglePrivacy m, EntityPlayer player)
	{
		TileEntity te = player.world.getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			TileWarpGate teleporter = (TileWarpGate) te;

			if (teleporter.isOwner(player.getGameProfile().getId()))
			{
				teleporter.togglePrivacyLevel();
			}
		}
	}
}