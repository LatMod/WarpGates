package com.latmod.warp_pads;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = WarpPads.MOD_ID)
@Config(modid = WarpPads.MOD_ID, category = "config")
public class WarpPadsConfig
{
	public static final Levels levels = new Levels();

	public static class Levels
	{
		@Config.RangeDouble(min = 0, max = 5)
		@Config.Comment("Levels required to teleport in same dimension")
		public double per_block = 0.001D;

		@Config.RangeInt(min = 0, max = 200)
		@Config.Comment("Levels required to teleport to another dimension")
		public int crossdim = 30;
	}

	public static void sync()
	{
		ConfigManager.sync(WarpPads.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(WarpPads.MOD_ID))
		{
			sync();
		}
	}

	public static int getEnergyRequired(double distance)
	{
		if (distance < 0D)
		{
			return levels.crossdim;
		}

		return (int) (distance * levels.per_block);
	}
}