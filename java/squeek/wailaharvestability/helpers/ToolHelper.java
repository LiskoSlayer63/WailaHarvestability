package squeek.wailaharvestability.helpers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Set;

public class ToolHelper
{
	public static Set<String> getToolClassesOf(@Nonnull ItemStack tool)
	{
		return tool.getItem().getToolClasses(tool);
	}

	public static boolean isToolOfClass(@Nonnull ItemStack tool, String toolClass)
	{
		return getToolClassesOf(tool).contains(toolClass);
	}

	public static boolean toolHasAnyToolClass(@Nonnull ItemStack tool)
	{
		return !getToolClassesOf(tool).isEmpty();
	}

	public static boolean isToolEffectiveAgainst(@Nonnull ItemStack tool, IBlockAccess blockAccess, BlockPos blockPos, String effectiveToolClass)
	{
		return ForgeHooks.isToolEffective(blockAccess, blockPos, tool) || (toolHasAnyToolClass(tool) ? isToolOfClass(tool, effectiveToolClass) : tool.getItem().getStrVsBlock(tool, blockAccess.getBlockState(blockPos)) > 1.5f);
	}

	public static boolean canToolHarvestLevel(@Nonnull ItemStack tool, IBlockAccess blockAccess, BlockPos blockPos, EntityPlayer player, int harvestLevel)
	{
		IBlockState state = blockAccess.getBlockState(blockPos);
		state = state.getBlock().getActualState(state, blockAccess, blockPos);

		String harvestTool = state.getBlock().getHarvestTool(state);

		return !tool.isEmpty() && harvestTool != null && tool.getItem().getHarvestLevel(tool, harvestTool, player, state) >= harvestLevel;
	}

	public static boolean canToolHarvestBlock(@Nonnull ItemStack tool, IBlockState blockState)
	{
		return blockState.getMaterial().isToolNotRequired() || tool.canHarvestBlock(blockState);
	}

	public static int getToolHarvestLevel(ItemTool tool, @Nonnull ItemStack toolStack)
	{
		Set<String> toolClasses = ToolHelper.getToolClassesOf(toolStack);
		if (toolClasses.isEmpty())
			return 0;

		String toolClass = toolClasses.iterator().next();
		return tool.getHarvestLevel(toolStack, toolClass, null, null);
	}
}