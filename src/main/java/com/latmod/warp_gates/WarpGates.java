package com.latmod.warp_gates;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.latmod.warp_gates.block.WarpGateNet;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = WarpGates.MOD_ID, name = WarpGates.MOD_NAME, version = WarpGates.VERSION, acceptedMinecraftVersions = "[1.12,)", dependencies = "required-after:" + FTBLibFinals.MOD_ID)
public class WarpGates
{
	public static final String MOD_ID = "warp_gates";
	public static final String MOD_NAME = "Warp Gates";
	public static final String VERSION = "@VERSION@";

	@Mod.Instance(WarpGates.MOD_ID)
	public static WarpGates INST;

	@SidedProxy(serverSide = "com.latmod.warp_gates.WarpGatesCommon", clientSide = "com.latmod.warp_gates.client.WarpGatesClient")
	public static WarpGatesCommon PROXY;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		PROXY.preInit();
	}

	@Mod.EventHandler
	public void onServerStopped(FMLServerStoppedEvent event)
	{
		WarpGateNet.clear();
	}
}