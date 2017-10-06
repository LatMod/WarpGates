package com.latmod.warp_gates.block;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.tile.TileBase;
import com.feed_the_beast.ftbl.lib.util.misc.EnumPrivacyLevel;
import com.latmod.warp_gates.WarpGatesConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class TileWarpGate extends TileBase implements ITickable
{
	public static final AxisAlignedBB AABB_X = new AxisAlignedBB(0, 1, -1, 1, 4, 2);
	public static final AxisAlignedBB AABB_Z = new AxisAlignedBB(-1, 1, 0, 2, 4, 1);

	private UUID owner, uuid;
	public boolean active = true;
	private String name = "";
	public EnumPrivacyLevel privacyLevel = EnumPrivacyLevel.TEAM;
	public int tick;
	public AxisAlignedBB aabb = null;

	private EnumFacing facing = null;

	public UUID getUUID()
	{
		if (uuid == null)
		{
			uuid = UUID.randomUUID();
		}

		return uuid;
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		nbt.setUniqueId("Owner", owner);
		EnumPrivacyLevel.NAME_MAP.writeToNBT(nbt, "Privacy", type, privacyLevel);

		if (type.save || !name.isEmpty())
		{
			nbt.setString("Name", name);
		}

		if (type.save || active)
		{
			nbt.setBoolean("Active", active);
		}

		nbt.setUniqueId("UUID", getUUID());
		nbt.setInteger("Tick", tick);
		MathUtils.FACING_MAP.writeToNBT(nbt, "Facing", type, getFacing());
		nbt.setBoolean("HasPortal", aabb != null);
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		owner = nbt.getUniqueId("Owner");
		privacyLevel = EnumPrivacyLevel.NAME_MAP.readFromNBT(nbt, "Privacy", type);
		name = nbt.getString("Name");
		active = nbt.getBoolean("Active");
		uuid = nbt.getUniqueId("UUID");

		if (uuid != null && uuid.getLeastSignificantBits() == 0L && uuid.getMostSignificantBits() == 0L)
		{
			uuid = null;
		}

		tick = nbt.getInteger("Tick");
		facing = MathUtils.FACING_MAP.readFromNBT(nbt, "Facing", type);
		aabb = nbt.getBoolean("HasPortal") ? (getFacing().getAxis() == EnumFacing.Axis.X) ? AABB_X.offset(pos) : AABB_Z.offset(pos) : null;
	}

	@Override
	public String getName()
	{
		return name.isEmpty() ? "Unnamed" : name;
	}

	@Override
	public boolean hasCustomName()
	{
		return !name.isEmpty();
	}

	public boolean hasPortal()
	{
		return aabb != null;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}

	public void setName(String n)
	{
		name = n;
		markDirty();
	}

	@Nullable
	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID id)
	{
		owner = id;
	}

	public boolean isOwner(UUID id)
	{
		return owner == null || owner.equals(id);
	}

	public void togglePrivacyLevel()
	{
		privacyLevel = EnumPrivacyLevel.VALUES[privacyLevel.ordinal() % EnumPrivacyLevel.VALUES.length];
		markDirty();
	}

	public EnumFacing getFacing()
	{
		if (facing == null)
		{
			facing = getBlockState().getValue(BlockHorizontal.FACING);
		}

		return facing;
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		facing = null;
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		WarpGateNet.add(this);
	}

	@Override
	public void invalidate()
	{
		WarpGateNet.remove(this);
		super.invalidate();
	}

	private boolean hasPortal0()
	{
		BlockPos toppos = pos.add(0, 4, 0);
		IBlockState topstate = world.getBlockState(toppos);
		Block top = topstate.getBlock();
		EnumFacing.Axis axis = getFacing().getAxis();

		if (top.isAir(topstate, world, toppos) || top.hasTileEntity(topstate))
		{
			return false;
		}

		for (int i = 0; i < 3; i++)
		{
			if (axis == EnumFacing.Axis.X)
			{
				if (world.isAirBlock(pos.add(0, i + 1, -2)) || world.isAirBlock(pos.add(0, i + 1, 2)))
				{
					return false;
				}
				else if (i != 1 && (world.isAirBlock(pos.add(0, 0, i - 1)) || world.isAirBlock(pos.add(0, 4, i - 1))))
				{
					return false;
				}
				for (int j = 0; j < 3; j++)
				{
					BlockPos pos1 = pos.add(0, i + 1, j - 1);
					if (!world.getBlockState(pos1).getBlock().isReplaceable(world, pos1))
					{
						return false;
					}
				}
			}
			else if (world.isAirBlock(pos.add(-2, i + 1, 0)) || world.isAirBlock(pos.add(2, i + 1, 0)))
			{
				return false;
			}
			else if (i != 1 && (world.isAirBlock(pos.add(i - 1, 0, 0)) || world.isAirBlock(pos.add(i - 1, 4, 0))))
			{
				return false;
			}
			for (int j = 0; j < 3; j++)
			{
				BlockPos pos1 = pos.add(j - 1, i + 1, 0);
				if (!world.getBlockState(pos1).getBlock().isReplaceable(world, pos1))
				{
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void update()
	{
		if (tick % 5 == 0)
		{
			boolean hasPortal = aabb != null;
			boolean prevHasPortal = hasPortal;
			hasPortal = hasPortal0();

			if (hasPortal)
			{
				aabb = (getFacing().getAxis() == EnumFacing.Axis.X) ? AABB_X.offset(pos) : AABB_Z.offset(pos);
			}
			else
			{
				aabb = null;
			}

			if (prevHasPortal != hasPortal)
			{
				markDirty();
			}
		}

		if (aabb != null && world.isRemote)
		{
			double offsetX = getFacing().getFrontOffsetX() * 0.3D;
			double offsetZ = getFacing().getFrontOffsetZ() * 0.3D;

			for (int i = 0; i < 20; i++)
			{
				double x = MathUtils.map(MathUtils.RAND.nextDouble(), 0D, 1D, aabb.minX, aabb.maxX);
				double y = MathUtils.map(MathUtils.RAND.nextDouble(), 0D, 1D, aabb.minY + 0.5D, aabb.maxY + 1.2D) - 1D;
				double z = MathUtils.map(MathUtils.RAND.nextDouble(), 0D, 1D, aabb.minZ, aabb.maxZ);

				world.spawnParticle(EnumParticleTypes.PORTAL, x - offsetX, y, z - offsetZ, offsetX, 0D, offsetZ);
			}
		}

		checkIfDirty();
		tick++;
	}

	public int getEnergyRequired(TileWarpGate teleporter)
	{
		if (teleporter.world.provider.getDimension() == world.provider.getDimension())
		{
			return WarpGatesConfig.getEnergyRequired(Math.sqrt(teleporter.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)));
		}

		return WarpGatesConfig.getEnergyRequired(-1D);
	}

	public boolean consumeEnergy(EntityPlayer ep, int levels, boolean simulate)
	{
		if (levels <= 0 || ep.capabilities.isCreativeMode || ep.experienceLevel >= levels)
		{
			if (!simulate && levels > 0)
			{
				ep.addExperienceLevel(-levels);
			}

			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos.getX() - 2D, pos.getY() - 1D, pos.getZ() - 2D, pos.getX() + 3D, pos.getY() + 50D, pos.getZ() + 3D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 10000D;
	}
}