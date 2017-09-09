package com.latmod.warp_gates.net;

import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.latmod.warp_gates.block.TileWarpGate;
import com.latmod.warp_gates.block.WarpGateNode;
import com.latmod.warp_gates.client.GuiWarpGate;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
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
	public void toBytes(ByteBuf io)
	{
		NetUtils.writePos(io, pos);
		io.writeInt(nodes.size());

		for (WarpGateNode t : nodes)
		{
			t.write(io);
		}
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		pos = NetUtils.readPos(io);
		int s = io.readInt();
		nodes = new ArrayList<>();

		for (int i = 0; i < s; i++)
		{
			WarpGateNode n = new WarpGateNode();
			n.read(io);
			nodes.add(n);
		}
	}

	@Override
	public void onMessage(MessageOpenWarpGateGui m, EntityPlayer player)
	{
		TileEntity te = player.getEntityWorld().getTileEntity(m.pos);

		if (te instanceof TileWarpGate)
		{
			new GuiWarpGate((TileWarpGate) te, m.nodes).openGui();
		}
	}
}