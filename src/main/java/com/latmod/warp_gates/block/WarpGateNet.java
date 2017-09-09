package com.latmod.warp_gates.block;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class WarpGateNet
{
	private static final Map<BlockDimPos, TileWarpGate> NET = new HashMap<>();
	private static final Comparator<TileWarpGate> COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());

	public static void clear()
	{
		NET.clear();
	}

	@Nullable
	public static TileWarpGate get(BlockDimPos pos)
	{
		return NET.get(pos);
	}

	public static void add(TileWarpGate gate)
	{
		if (gate.getOwner() != null && gate.hasWorld() && gate.isServerSide())
		{
			NET.put(gate.getDimPos(), gate);
		}
	}

	public static void remove(TileWarpGate gate)
	{
		if (gate.hasWorld() && gate.isServerSide())
		{
			NET.remove(gate.getDimPos());
		}
	}

	public static Collection<TileWarpGate> getGates(EntityPlayerMP player)
	{
		IUniverse world = FTBLibAPI.API.getUniverse();

		IForgePlayer p = world.getPlayer(player);
		List<TileWarpGate> list = new ArrayList<>();

		if (p == null || NET.isEmpty())
		{
			return list;
		}

		for (TileWarpGate gate : NET.values())
		{
			if (gate.getOwner() != null && gate.hasWorld() && !gate.isInvalid())
			{
				IForgePlayer owner = world.getPlayer(gate.getOwner());

				if (owner != null)
				{
					if (p.canInteract(owner, gate.privacyLevel))
					{
						list.add(gate);
					}
				}
			}
		}

		if (list.size() > 1)
		{
			list.sort(COMPARATOR);
		}

		return list;
	}
}