package com.latmod.warp_gates.net;

import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.latmod.warp_gates.WarpGates;

/**
 * @author LatvianModder
 */
public class WarpGatesNetHandler
{
	static final NetworkWrapper NET = NetworkWrapper.newWrapper(WarpGates.MOD_ID);

	public static void init()
	{
		NET.register(1, new MessageOpenWarpGateGui());
		NET.register(2, new MessageSelectTeleporter());
		NET.register(3, new MessageSetName());
		NET.register(4, new MessageToggleActive());
		NET.register(5, new MessageTogglePrivacy());
	}
}