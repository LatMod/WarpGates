package com.latmod.warp_gates;

import com.latmod.warp_gates.block.WarpGateNet;
import com.latmod.warp_gates.net.WarpGatesNetHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = WarpGates.MOD_ID, name = "Warp Gates", version = "@VERSION@", acceptedMinecraftVersions = "[1.12,)", dependencies = "required-after:ftbl")
public class WarpGates
{
	public static final String MOD_ID = "warp_gates";

	@Mod.Instance(WarpGates.MOD_ID)
	public static WarpGates INST;

	@SidedProxy(serverSide = "com.latmod.warp_gates.WarpGatesCommon", clientSide = "com.latmod.warp_gates.client.WarpGatesClient")
	public static WarpGatesCommon PROXY;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		WarpGatesConfig.sync();
		WarpGatesNetHandler.init();
	}

	@Mod.EventHandler
	public void onServerStopped(FMLServerStoppedEvent event)
	{
		WarpGateNet.clear();
	}
}