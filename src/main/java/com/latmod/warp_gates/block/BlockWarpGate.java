package com.latmod.warp_gates.block;

import com.feed_the_beast.ftbl.lib.block.BlockBase;
import com.latmod.warp_gates.WarpGates;
import com.latmod.warp_gates.net.MessageOpenWarpGateGui;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockWarpGate extends BlockBase
{

	public BlockWarpGate(String s)
	{
		super(WarpGates.MOD_ID, s, Material.GLASS, MapColor.LIGHT_BLUE);
		setBlockUnbreakable();
		setResistance(10000000F);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
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
		return new TileWarpGate();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockHorizontal.FACING);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return !(entity instanceof EntityWither || entity instanceof EntityDragon);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileWarpGate && ((TileWarpGate) tileEntity).hasPortal())
		{
			BlockPos pos1 = pos.add(0, 4, 0);
			IBlockState state1 = world.getBlockState(pos1);
			return state1.getBlock().getActualState(state1, world, pos1);
		}

		return state;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileWarpGate && ((TileWarpGate) tileEntity).hasPortal())
		{
			BlockPos pos1 = pos.add(0, 4, 0);
			IBlockState state1 = world.getBlockState(pos1);
			return state1.getBlock().getBoundingBox(state1, world, pos1);
		}

		return FULL_BLOCK_AABB;
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
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileWarpGate)
		{
			((TileWarpGate) tileEntity).setOwner(placer.getUniqueID());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			pos = pos.offset(state.getValue(BlockHorizontal.FACING).getOpposite());
			TileEntity te = worldIn.getTileEntity(pos);

			if (te instanceof TileWarpGate && !(playerIn instanceof FakePlayer))
			{
				TileWarpGate teleporter = (TileWarpGate) te;
				EntityPlayerMP ep = (EntityPlayerMP) playerIn;
				List<WarpGateNode> teleporters = new ArrayList<>();

				for (TileWarpGate teleporter1 : WarpGateNet.getGates(ep))
				{
					if (teleporter1 != teleporter)
					{
						WarpGateNode n = new WarpGateNode();
						n.pos = teleporter1.getDimPos();
						n.name = teleporter1.getName();
						n.energy = teleporter.getEnergyRequired(teleporter1);
						n.available = teleporter1.active && teleporter.consumeEnergy(playerIn, n.energy, true);
						teleporters.add(n);
					}
				}

				new MessageOpenWarpGateGui(pos, teleporters).sendTo(ep);
			}
		}

		return true;
	}
}