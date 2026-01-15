package net.guzhenren.devkit.example;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;

import net.guzhenren.init.GuzhenrenModItems;
import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Example（附属模组）- 杀招“绑定阶段”示例。
 *
 * <p>目标：复用蛊真人原生的 4 页杀招 UI（对应 Z/X/C/V 四个槽位），把“放入杀招物品并确认绑定”这条链路
 * 扩展成：附属模组可以把自己的杀招绑定到指定键位。</p>
 *
 * <p>触发时机：玩家在杀招 UI 中点击“确认绑定”（不同页面对应不同键位槽）。DevKit 通过 Mixin 拦截
 * {@code SanZhaoQueRen*Procedure}，并 dispatch 到 {@code ShaZhaoHooks.BIND}。</p>
 *
 * <p>本例做的事：当 UI 的 slot10 放入指定的主模组杀招卷轴时，把对应键位的 {@code sanzhaoN} 写成一个
 * “外部杀招ID”（这里用 {@code 100001}），后续按键触发阶段会识别该 ID 并执行附属模组逻辑。</p>
 *
 * <p>为什么示例用“杀招卷轴”而不是“蛊虫”：主模组杀招 UI 的 slot10 只能放 {@code guzhenren:shazhao} tag 的物品。
 * 蛊虫通常是 {@code guzhenren:guchong}，默认放不进去；若要支持蛊虫作为绑定物品，需要额外注入 slot 放置规则。</p>
 */
public final class ExampleShaZhaoBind {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final double EXAMPLE_SANZHAO_ID = 100001d;

	private ExampleShaZhaoBind() {
	}

	public static void bootstrap(String modId) {
		java.util.Set<Integer> slots = java.util.Set.of(1, 2, 3, 4);
		ShaZhaoHooks.registerBind(modId, 0, slots, ctx -> {
			Player player = ctx.player();
			if (player == null) {
				return HookResult.PASS;
			}

			ItemStack item = ctx.shazhaoItem();
			if (item.isEmpty()) {
				return HookResult.PASS;
			}
			if (item.getItem() != GuzhenrenModItems.SHA_ZHAO_GU_JUAN_HUN_YAN_JIN_JING.get()) {
				return HookResult.PASS;
			}

			GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
			int slot = ctx.keySlotIndex();
			net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.setBoundId(vars, slot, EXAMPLE_SANZHAO_ID);
			vars.markSyncDirty();

			if (!player.level().isClientSide()) {
				String key = slot == 1 ? "Z" : slot == 2 ? "X" : slot == 3 ? "C" : slot == 4 ? "V" : ("slot" + slot);
				LOGGER.info("[{}] bound example shazhao to {}: id={}", modId, key, EXAMPLE_SANZHAO_ID);
			}
			return HookResult.CONSUME;
		});
	}

	public static double exampleId() {
		return EXAMPLE_SANZHAO_ID;
	}
}
