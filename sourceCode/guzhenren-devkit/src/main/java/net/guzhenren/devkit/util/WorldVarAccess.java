package net.guzhenren.devkit.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import net.guzhenren.network.GuzhenrenModVariables;

/**
 * 世界变量（{@code GuzhenrenModVariables.MapVariables}）的安全访问入口。
 *
 * <p>本体（mod本体_魂道智道更新）将拍卖行等系统改为世界级别存储（{@code MapVariables}），
 * 不再放在玩家变量里。本类暴露其中与附属模组最相关的拍卖行字段。</p>
 */
public final class WorldVarAccess {
	/**
	 * 构造方法。
	 */
	private WorldVarAccess() {
	}

	/**
	 * 执行 map vars 操作。
	 */
	public static GuzhenrenModVariables.MapVariables mapVars(LevelAccessor world) {
		return GuzhenrenModVariables.MapVariables.get(world);
	}

	/**
	 * 标记玩家变量为脏并触发同步。
	 */
	public static void markDirty(LevelAccessor world) {
		mapVars(world).markSyncDirty();
	}

	/** 当前拍卖行正在展示/竞拍的拍品。 */
	public static ItemStack getAuctionItem(LevelAccessor world) {
		return mapVars(world).PaiMaiHang_PaiPin.copy();
	}

	/**
	 * 设置 auction item 的值。
	 */
	public static void setAuctionItem(LevelAccessor world, ItemStack item) {
		mapVars(world).PaiMaiHang_PaiPin = item == null ? ItemStack.EMPTY : item.copy();
		markDirty(world);
	}

	/** 拍卖行状态机状态：0=空闲/预备, 1=竞拍中, 2=结算冷却。 */
	public static double getAuctionState(LevelAccessor world) {
		return mapVars(world).PaiMaiHang_ZhuangTai;
	}

	/**
	 * 设置 auction state 的值。
	 */
	public static void setAuctionState(LevelAccessor world, double state) {
		mapVars(world).PaiMaiHang_ZhuangTai = state;
		markDirty(world);
	}
}
