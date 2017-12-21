package com.latmod.warp_gates.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.warp_gates.block.TileWarpGate;
import com.latmod.warp_gates.block.WarpGateNode;
import com.latmod.warp_gates.client.GuiWarpGate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageOpenWarpGateGui extends MessageToClient<MessageOpenWarpGateGui>
{
	private BlockPos pos;
	private List<WarpGateNode> nodes;

	public MessageOpenWarpGateGui()
	{
	}

	public MessageOpenWarpGateGui(BlockPos p, List<WarpGateNode> t)
	{
		pos = p;
		nodes = t;
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
		data.writeCollection(nodes, WarpGateNode.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		pos = data.readPos();
		data.readCollection(nodes, WarpGateNode.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageOpenWarpGateGui m, EntityPlayer player)
	{
		TileEntity te = player.getEntityWorld().getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			new GuiWarpGate((TileWarpGate) te, m.nodes).openGuiLater();
		}
	}
}