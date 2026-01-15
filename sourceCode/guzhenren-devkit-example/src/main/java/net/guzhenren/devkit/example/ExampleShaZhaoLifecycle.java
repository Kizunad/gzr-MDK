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
 * Example（附属模组）- 杀招“生命周期/一致性”示例。
 *
 * <p>问题：只写 {@code sanzhaoN} 外部ID 会导致“解绑不生效”（玩家把物品取走，但按键仍能触发）。</p>
 *
 * <p>解决：在杀招 UI 的每 tick 刷新流程中检查槽位状态，如果发现绑定物品不匹配/为空，则清理对应的 {@code sanzhaoN}。
 * DevKit 已注入四个 UI tick Procedure（对应四页），并在这里通过 {@code ShaZhaoHooks.TICK} 接收回调。</p>
 *
 * <p>注意：这里做的是“UI 内即时收口”；触发时也有兜底清理（见 {@code ExampleShaZhaoKey}），两者结合才能避免幽灵状态。</p>
 */
public final class ExampleShaZhaoLifecycle {
	private static final Logger LOGGER = LogUtils.getLogger();

	private ExampleShaZhaoLifecycle() {
	}

	public static void bootstrap(String modId) {
		java.util.Set<Integer> slots = java.util.Set.of(1, 2, 3, 4);
		ShaZhaoHooks.registerTick(modId, 0, slots, ctx -> {
			Player player = ctx.player();
			if (player == null) {
				return HookResult.PASS;
			}
			GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
			validateSlot(modId, vars, ctx.keySlotIndex());
			return HookResult.PASS;
		});
	}

	private static void validateSlot(String modId, GuzhenrenModVariables.PlayerVariables vars, int slot) {
		double current = net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.getBoundId(vars, slot);
		ItemStack bound = net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.getBoundItem(vars, slot);

		if (current != ExampleShaZhaoBind.exampleId()) {
			return;
		}
		if (bound != null && !bound.isEmpty() && bound.getItem() == GuzhenrenModItems.SHA_ZHAO_GU_JUAN_HUN_YAN_JIN_JING.get()) {
			return;
		}

		net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.setBoundId(vars, slot, 0);
		vars.markSyncDirty();
		LOGGER.info("[{}] example shazhao auto-unbound by UI tick for slot{}", modId, slot);
	}
}
