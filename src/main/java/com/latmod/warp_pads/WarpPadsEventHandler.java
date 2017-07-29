package com.latmod.warp_pads;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.FTBLibRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class WarpPadsEventHandler
{
	@SubscribeEvent
	public static void registerCommon(FTBLibRegistryEvent event)
	{
		WarpPadsConfig.init(event.getRegistry());
	}
}