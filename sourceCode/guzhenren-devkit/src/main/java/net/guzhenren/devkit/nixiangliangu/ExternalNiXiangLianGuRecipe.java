package net.guzhenren.devkit.nixiangliangu;

import net.minecraft.world.entity.player.Player;

/**
 * 逆向炼蛊（逆炼 / NiLian）外部配方接口。
 *
 * <p>设计完全镜像 {@code net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe}：
 * DevKit 在主模组“逆炼开始”与“逆炼 GUI 每 tick”两条路径上各有一个 Mixin 注入点，
 * 当本接口的 {@link #matches(Player)} / {@link #isActive(Player)} 命中时，接管原版逻辑。
 *
 * <p>注意：主模组的逆炼（NiLian）与普通炼蛊共用同一套玩家变量
 * {@code vars.LianGu} / {@code vars.GuFang} / {@code vars.LianGuJinDu}，
 * 因此“是否正在逆向炼蛊”由 {@code GuFang} 的取值区分（见 {@link #isRunning}）。
 */
public interface ExternalNiXiangLianGuRecipe {
	/**
	 * 返回该配方的唯一标识。
	 */
	String id();

	/** 在“逆炼开始”按钮点击时调用。返回 true 表示由本配方接管，DevKit 会取消原版开始逻辑。 */
	boolean matches(Player player);

	/** 接管开始时调用（设置 vars.LianGu / GuFang / LianGuJinDu 等）。 */
	void onStart(Player player);

	/** 逆炼 GUI 每 tick 调用（推进进度 / 产出 / 结束）。 */
	void onTick(Player player);

	/** 在“逆炼 GUI 每 tick”时调用，判断当前是否由本配方接管。 */
	boolean isActive(Player player);

	/** 判断玩家是否正在运行某个特定 GuFangId 的逆炼。 */
	static boolean isRunning(Player player, double guFangId) {
		net.guzhenren.network.GuzhenrenModVariables.PlayerVariables vars =
				player.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES);
		return vars.LianGu && vars.GuFang == guFangId;
	}
}
