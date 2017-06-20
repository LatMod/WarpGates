package com.latmod.warp_pads.block;

import com.feed_the_beast.ftbl.lib.block.EnumHorizontalOffset;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TileWarpPadPart extends TileWarpPadBase
{
	private EnumHorizontalOffset part;
	private TileWarpPad warpPad = null;

	public TileWarpPadPart()
	{
	}

	public TileWarpPadPart(EnumHorizontalOffset p)
	{
		part = p;
		checkUpdates = false;
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type.save)
		{
			nbt.setByte("Part", (byte) getPart().ordinal());
		}
		else
		{
			nbt.setString("Part", getPart().getName());
		}
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		if (type.save || nbt.hasKey("Part", Constants.NBT.TAG_BYTE))
		{
			part = EnumHorizontalOffset.VALUES[nbt.getByte("Part")];
		}
		else
		{
			part = EnumHorizontalOffset.NAME_MAP.get(nbt.getString("Part"));
		}
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		warpPad = null;
	}

	@Override
	public EnumHorizontalOffset getPart()
	{
		if (part == null)
		{
			part = EnumHorizontalOffset.VALUES[getBlockMetadata()];
		}

		return part;
	}

	@Nullable
	private TileWarpPad getWarpPad()
	{
		if (warpPad == null)
		{
			TileEntity tileEntity = world.getTileEntity(getPart().offset(pos));

			if (tileEntity instanceof TileWarpPad)
			{
				warpPad = (TileWarpPad) tileEntity;
			}
		}

		return warpPad;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, net.minecraft.util.EnumFacing facing)
	{
		TileWarpPad pad = getWarpPad();
		return pad != null && pad.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
	{
		TileWarpPad pad = getWarpPad();
		return pad != null ? pad.getCapability(capability, facing) : null;
	}
}