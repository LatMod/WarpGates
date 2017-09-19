package com.latmod.warp_gates.net;

import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.latmod.warp_gates.block.TileWarpGate;
import com.latmod.warp_gates.block.WarpGateNet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class MessageSelectTeleporter extends MessageToServer<MessageSelectTeleporter>
{
	private BlockPos pos;
	private BlockDimPos dst;

	public MessageSelectTeleporter()
	{
	}

	public MessageSelectTeleporter(BlockPos p, BlockDimPos p1)
	{
		pos = p;
		dst = p1;
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
		data.writeDimPos(dst);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		dst = data.readDimPos();
	}

	@Override
	public void onMessage(MessageSelectTeleporter m, EntityPlayer player)
	{
		TileEntity te = player.world.getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			TileWarpGate teleporter0 = (TileWarpGate) te;
			TileWarpGate teleporter = WarpGateNet.get(m.dst);

			if (teleporter != null)
			{
				int levels = teleporter0.getEnergyRequired(teleporter);

				if (teleporter0.consumeEnergy(player, levels, true))
				{
					teleporter0.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
					ServerUtils.teleportPlayer((EntityPlayerMP) player, teleporter.getDimPos());
					teleporter0.consumeEnergy(player, levels, false);
					teleporter.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
				}
			}
		}
	}
}