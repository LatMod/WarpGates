package com.latmod.warp_gates.block;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;

/**
 * @author LatvianModder
 */
public class WarpGateNode
{
	public static final DataOut.Serializer<WarpGateNode> SERIALIZER = (data, node) ->
	{
		data.writeDimPos(node.pos);
		data.writeString(node.name);
		data.writeInt(node.energy);
		data.writeBoolean(node.available);
	};

	public static final DataIn.Deserializer<WarpGateNode> DESERIALIZER = data ->
	{
		WarpGateNode node = new WarpGateNode();
		node.pos = data.readDimPos();
		node.name = data.readString();
		node.energy = data.readInt();
		node.available = data.readBoolean();
		return node;
	};

	public BlockDimPos pos;
	public String name;
	public int energy;
	public boolean available;

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