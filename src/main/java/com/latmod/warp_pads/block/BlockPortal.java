package com.latmod.warp_pads.block;

import com.feed_the_beast.ftbl.lib.block.BlockBase;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.latmod.warp_pads.WarpPads;
import com.latmod.warp_pads.net.MessageOpenWarpPadGui;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockPortal extends BlockBase
{
	private static final AxisAlignedBB AABB[] = MathUtils.getRotatedBoxes(new AxisAlignedBB(-1D, 0.5D - 0.0625D, -1D, 2D, 0.5D + 0.0625D, 2D));

	public enum PortalType
	{
		NONE,
		QUARTZ_X,
		QUARTZ_Y,
		QUARTZ_Z,
		PURPUR_X,
		PURPUR_Y,
		PURPUR_Z;

		private static final IBlockState[] QUARTZ = {
				Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_X),
				Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Y),
				Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Z)
		};

		private static final IBlockState[] PURPUR = {
				Blocks.PURPUR_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X),
				Blocks.PURPUR_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y),
				Blocks.PURPUR_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z)
		};

		public static PortalType get(IBlockAccess world, BlockPos core)
		{
			IBlockState down = world.getBlockState(core.add(0, -2, 0));
			IBlockState up = world.getBlockState(core.add(0, 2, 0));

			if (down == PURPUR[0] && up == PURPUR[0])
			{
			}

			return NONE;
		}
	}

	public BlockPortal()
	{
		super(WarpPads.MOD_ID, "portal", Material.GLASS, MapColor.LIGHT_BLUE);
		setBlockUnbreakable();
		setResistance(10000000F);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.NORTH));
		setUnlocalizedName("portal");
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World w, IBlockState state)
	{
		return new TilePortal(state.getValue(BlockDirectional.FACING));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockDirectional.FACING);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.VALUES[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockDirectional.FACING).getIndex();
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess w, BlockPos pos)
	{
		return AABB[state.getValue(BlockDirectional.FACING).getIndex()];
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(BlockDirectional.FACING, rot.rotate(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(BlockDirectional.FACING, facing);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			pos = pos.offset(state.getValue(BlockDirectional.FACING).getOpposite());
			TileEntity te = worldIn.getTileEntity(pos);

			if (te instanceof TilePortal && !(playerIn instanceof FakePlayer))
			{
				TilePortal teleporter = (TilePortal) te;
				EntityPlayerMP ep = (EntityPlayerMP) playerIn;
				List<WarpPadNode> teleporters = new ArrayList<>();

				for (TilePortal teleporter1 : WarpPadsNet.getTeleporters(ep))
				{
					if (teleporter1 != teleporter)
					{
						WarpPadNode n = new WarpPadNode();
						n.pos = teleporter1.getDimPos();
						n.name = teleporter1.getName();
						n.energy = teleporter.getEnergyRequired(teleporter1);
						n.available = teleporter1.active && teleporter.consumeEnergy(playerIn, n.energy, true);
						teleporters.add(n);
					}
				}

				new MessageOpenWarpPadGui(pos, teleporters).sendTo(ep);
			}
		}

		return true;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return canExist(side, worldIn, pos);
	}

	@Override
	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		return facing.getAxis() == state.getValue(BlockDirectional.FACING).getAxis();
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss())
		{
			//entityIn.setPortal(pos);
		}
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == state.getValue(BlockDirectional.FACING).getOpposite() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	private static int get(IBlockAccess world, BlockPos pos, EnumFacing.Axis axis)
	{
		IBlockState state = world.getBlockState(pos);

		if (state == PortalType.PURPUR[axis.ordinal()])
		{
			return 2;
		}

		return state == PortalType.QUARTZ[axis.ordinal()] ? 1 : 0;
	}

	public static boolean canExist(EnumFacing facing, IBlockAccess world, BlockPos pos)
	{
		int t = 2;
		switch (facing.getAxis())
		{
			case X:
			{
				for (int i = -1; i <= 1; i++)
				{
					for (int j = 0; j <= 1; j++)
					{
						if (!(i == 0 && j == 0))
						{
							BlockPos pos1 = pos.add(0, i, j);
							IBlockState state1 = world.getBlockState(pos1);
							if (!state1.getBlock().isReplaceable(world, pos1))
							{
								return false;
							}
						}
					}
					t = Math.min(t, get(world, pos.add(0, i, -2), EnumFacing.Axis.Y));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(0, i, 2), EnumFacing.Axis.Y));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(0, -2, i), EnumFacing.Axis.Z));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(0, 2, i), EnumFacing.Axis.Z));
					if (t == 0)
					{
						return false;
					}
				}
				break;
			}
			case Y:
			{
				for (int i = -1; i <= 1; i++)
				{
					for (int j = 0; j <= 1; j++)
					{
						if (!(i == 0 && j == 0))
						{
							BlockPos pos1 = pos.add(i, 0, j);
							IBlockState state1 = world.getBlockState(pos1);
							if (!state1.getBlock().isReplaceable(world, pos1))
							{
								return false;
							}
						}
					}
					t = Math.min(t, get(world, pos.add(i, 0, -2), EnumFacing.Axis.X));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(i, 0, 2), EnumFacing.Axis.X));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(-2, 0, i), EnumFacing.Axis.Z));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(2, 0, i), EnumFacing.Axis.Z));
					if (t == 0)
					{
						return false;
					}
				}
				break;
			}
			case Z:
			{
				for (int i = -1; i <= 1; i++)
				{
					for (int j = 0; j <= 1; j++)
					{
						if (!(i == 0 && j == 0))
						{
							BlockPos pos1 = pos.add(i, i, 0);
							IBlockState state1 = world.getBlockState(pos1);
							if (!state1.getBlock().isReplaceable(world, pos1))
							{
								return false;
							}
						}
					}
					t = Math.min(t, get(world, pos.add(-2, i, 0), EnumFacing.Axis.Y));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(2, i, 0), EnumFacing.Axis.Y));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(i, -2, 0), EnumFacing.Axis.X));
					if (t == 0)
					{
						return false;
					}
					t = Math.min(t, get(world, pos.add(i, 2, 0), EnumFacing.Axis.X));
					if (t == 0)
					{
						return false;
					}
				}
				break;
			}
		}

		return t > 0;
	}
}