package com.latmod.warp_gates.block;

import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author LatvianModder
 */
public class WarpGateNode
{
	public BlockDimPos pos;
	public String name;
	public int energy;
	public boolean available;

	public void write(ByteBuf io)
	{
		NetUtils.writeDimPos(io, pos);
		ByteBufUtils.writeUTF8String(io, name);
		io.writeInt(energy);
		io.writeBoolean(available);
	}

	public void read(ByteBuf io)
	{
		pos = NetUtils.readDimPos(io);
		name = ByteBufUtils.readUTF8String(io);
		energy = io.readInt();
		available = io.readBoolean();
	}

	public int hashCode()
	{
		return pos.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (o == this)
		{
			return true;
		}
		else if (o instanceof WarpGateNode)
		{
			return ((WarpGateNode) o).pos.equals(pos);
		}
		else if (o instanceof BlockDimPos)
		{
			return o.equals(pos);
		}

		return false;
	}

	public String toString()
	{
		return name;
	}
}