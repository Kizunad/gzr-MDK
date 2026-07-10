package net.guzhenren.devkit.example;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.guzhenren.devkit.liangu.LianGuMenuSlots;
import net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe;
import net.guzhenren.devkit.nixiangliangu.NiXiangLianGuRecipes;
import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 逆向炼蛊（逆炼 / NiLian）示例配方。
 *
 * <p>演示如何通过 DevKit 的逆向炼蛊 Hook 接管主模组的“开始逆炼 / 逆炼推进”逻辑：
 * <ul>
 *   <li>在逆炼 GUI 的 slot0 放入钻石并点击“开始逆炼”，即由本配方接管；</li>
 *   <li>进度（vars.LianGuJinDu）每 tick +1，约 5 秒涨到 100；</li>
 *   <li>满进度后在逆炼 GUI 的 slot2（产出槽）放 1 个泥土，并复位炼蛊状态。</li>
 * </ul>
 *
 * <p>注意：本示例为<b>演示/验证</b>用途，会接管逆炼 GUI 的“开始”按钮——
 * 只要 slot0 是钻石就会优先于原版逻辑触发。验证完毕后如需恢复原版逆炼，移除本注册即可。
 */
public final class ExampleNiXiangLianGu {
	private ExampleNiXiangLianGu() {
	}

	private static final Logger LOGGER = LogUtils.getLogger();

	// 外部 GuFang 约定：addon 从 100000 起。炼蛊示例用了 100000，逆炼示例用 100001，避免冲突。
	private static final double NIXIANG_GUFANG_ID = 100001d;

	// 进度条满值（文本显示“逆炼进度:X/100”）。
	private static final double MAX = 100d;
	// 每 tick 进度 +1（20 tick/秒 → 约 5 秒填满 100），1 个 1 个地涨。
	private static final double STEP = 1d;
	// 逆炼 GUI 的产出槽位（主模组 NiLian3Procedure 把产出放入 slot2/3/4，此处取 slot2 作演示输出：泥土）。
	private static final int OUTPUT_SLOT = 2;

	public static void bootstrap(String modId) {
		NiXiangLianGuRecipes.register(new ExternalNiXiangLianGuRecipe() {
			@Override
			public String id() {
				return modId + ":nixiang_example";
			}

			@Override
			public boolean matches(Player player) {
				// 已经在炼蛊/逆炼中不允许重新开始。
				if (player.getData(GuzhenrenModVariables.PLAYER_VARIABLES).LianGu) {
					return false;
				}
				// 必须在逆炼 GUI（或其同源 MenuAccessor 菜单）里。
				if (!LianGuMenuSlots.isInLianGuMenu(player)) {
					return false;
				}
				// slot0 必须放入钻石才触发演示（对应主模组逆炼产出逻辑）。
				ItemStack slot0 = LianGuMenuSlots.getSlot(player, 0);
				return !slot0.isEmpty() && slot0.getItem() == Items.DIAMOND;
			}

			@Override
			public void onStart(Player player) {
				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
				vars.LianGuJinDu = 0;
				vars.GuFang = NIXIANG_GUFANG_ID;
				vars.LianGu = true;
				vars.markSyncDirty();

				if (!player.level().isClientSide()) {
					LOGGER.info("[{}] nixiang start ok: GuFang={}", modId, NIXIANG_GUFANG_ID);
				}
			}

			@Override
			public void onTick(Player player) {
				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);

				// 每 tick 进度 +1，1 个 1 个地涨，直到满值。
				if (vars.LianGuJinDu < MAX) {
					vars.LianGuJinDu = vars.LianGuJinDu + STEP;
					if (vars.LianGuJinDu > MAX) {
						vars.LianGuJinDu = MAX; // 防浮点误差越界
					}
				}

				// 进度达到满值（100/100）立即在 slot2 产出 1 个泥土，并复位状态。
				if (vars.LianGuJinDu >= MAX) {
					vars.LianGuJinDu = 0;
					vars.GuFang = 0;
					vars.LianGu = false;
					vars.markSyncDirty();

					LianGuMenuSlots.setSlot(player, OUTPUT_SLOT, new ItemStack(Items.DIRT, 1));

					if (!player.level().isClientSide()) {
						LOGGER.info("[{}] nixiang finish ok: produced 1x DIRT (进度条已满)", modId);
					}
					return;
				}

				vars.markSyncDirty();
			}

			@Override
			public boolean isActive(Player player) {
				return ExternalNiXiangLianGuRecipe.isRunning(player, NIXIANG_GUFANG_ID);
			}
		});
	}
}
