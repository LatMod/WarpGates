package com.latmod.warp_gates;

import com.latmod.warp_gates.net.WarpGatesNetHandler;

/**
 * @author LatvianModder
 */
public class WarpGatesCommon
{
	public void preInit()
	{
		WarpGatesConfig.sync();
		WarpGatesNetHandler.init();
	}
}